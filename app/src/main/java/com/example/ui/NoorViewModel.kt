package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.NoorDatabase
import com.example.data.database.WiridModuleTypes
import com.example.data.model.DhikrEntity
import com.example.data.model.SessionEntity
import com.example.data.model.WiridProgressEntity
import com.example.data.preferences.SettingsManager
import com.example.data.preferences.UserSettings
import com.example.data.repository.NoorRepository
import com.example.data.repository.NoorStats
import com.example.util.HapticFeedbackHelper
import com.example.util.SoundFeedbackHelper
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CounterUiState(
    val activeDhikr: DhikrEntity? = null,
    val count: Int = 0,
    val target: Int = 33,
    val isTargetReached: Boolean = false,
    val showTargetReachedDialog: Boolean = false,
    val showResetConfirmation: Boolean = false,
    val showChangeTargetDialog: Boolean = false,
    val pulseTrigger: Long = 0L,
    val sessionSavedForCurrentRound: Boolean = false
)

sealed class NoorUiEvent {
    data class ShowToast(val message: String) : NoorUiEvent()
    data class TargetCompleted(val dhikrName: String, val target: Int) : NoorUiEvent()
}

class NoorViewModel(application: Application) : AndroidViewModel(application) {
    private val database = NoorDatabase.getDatabase(application)
    private val settingsManager = SettingsManager(application)
    private val repository = NoorRepository(
        dhikrDao = database.dhikrDao(),
        sessionDao = database.sessionDao(),
        wiridProgressDao = database.wiridProgressDao(),
        settingsManager = settingsManager
    )

    private val hapticHelper = HapticFeedbackHelper(application)
    private val soundHelper = SoundFeedbackHelper()

    private var lastTapElapsedMs: Long = 0L

    val settings: StateFlow<UserSettings> = repository.settings
    val allDhikrs: StateFlow<List<DhikrEntity>> = repository.allDhikrs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allSessions: StateFlow<List<SessionEntity>> = repository.allSessions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalDhikr: StateFlow<Int> = repository.totalDhikrCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val completedSessionsCount: StateFlow<Int> = repository.totalCompletedSessions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val todayDhikr: StateFlow<Int> = repository.getTodayDhikrCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val weeklyDhikr: StateFlow<Int> = repository.getWeeklyDhikrCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val monthlyDhikr: StateFlow<Int> = repository.getMonthlyDhikrCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val todayWiridProgressMap: StateFlow<Map<String, WiridProgressEntity>> =
        repository.getTodayWiridProgress()
            .map { list -> list.associateBy { it.wiridId } }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    val todayCompletedWiridCount: StateFlow<Int> = repository.getTodayCompletedWiridCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val stats: StateFlow<NoorStats> = combine(
        totalDhikr,
        completedSessionsCount,
        todayDhikr,
        weeklyDhikr,
        monthlyDhikr
    ) { total, completed, today, weekly, monthly ->
        NoorStats(
            totalDhikr = total,
            completedSessions = completed,
            todayDhikr = today,
            weeklyDhikr = weekly,
            monthlyDhikr = monthly
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoorStats())

    private val _counterState = MutableStateFlow(CounterUiState())
    val counterState: StateFlow<CounterUiState> = _counterState.asStateFlow()

    private val _events = MutableSharedFlow<NoorUiEvent>()
    val events: SharedFlow<NoorUiEvent> = _events.asSharedFlow()

    // Dhikr Library Filter State
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Semua")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    init {
        viewModelScope.launch {
            repository.ensureSeeded()
            val currentSettings = settings.value
            var active = repository.getDhikrById(currentSettings.activeDhikrId)
            if (active == null) {
                active = NoorDatabase.DEFAULT_DHIKRS.first()
            }
            val initialTarget = if (currentSettings.activeTarget > 0) currentSettings.activeTarget else active.target
            val initialCount = currentSettings.activeCount

            _counterState.value = CounterUiState(
                activeDhikr = active,
                count = initialCount,
                target = initialTarget,
                isTargetReached = initialCount >= initialTarget && initialTarget > 0
            )
        }
    }

    fun increment() {
        val now = android.os.SystemClock.elapsedRealtime()
        if (now - lastTapElapsedMs < 40L) {
            return // Prevent accidental hardware/frame double counting
        }
        lastTapElapsedMs = now

        val current = _counterState.value
        val newCount = current.count + 1
        val wasReached = current.count >= current.target && current.target > 0
        val isNowReached = newCount >= current.target && current.target > 0

        val pulseTime = System.currentTimeMillis()

        _counterState.value = current.copy(
            count = newCount,
            isTargetReached = isNowReached,
            showTargetReachedDialog = (!wasReached && isNowReached),
            pulseTrigger = pulseTime
        )

        repository.updateActiveCount(newCount)

        val isVibration = settings.value.isVibrationEnabled
        val isSound = settings.value.isSoundEnabled

        if (!wasReached && isNowReached) {
            hapticHelper.vibrateTargetReached(isVibration)
            soundHelper.playCompletionChime(isSound)
            viewModelScope.launch {
                _events.emit(
                    NoorUiEvent.TargetCompleted(
                        dhikrName = current.activeDhikr?.name ?: "Dzikir",
                        target = current.target
                    )
                )
            }
        } else {
            hapticHelper.vibrateTap(isVibration)
            soundHelper.playTapChime(isSound)
        }
    }

    fun undo() {
        val current = _counterState.value
        if (current.count > 0) {
            val newCount = current.count - 1
            _counterState.value = current.copy(
                count = newCount,
                isTargetReached = newCount >= current.target && current.target > 0,
                pulseTrigger = System.currentTimeMillis()
            )
            repository.updateActiveCount(newCount)
            hapticHelper.vibrateTap(settings.value.isVibrationEnabled)
        }
    }

    fun showResetDialog(show: Boolean) {
        _counterState.value = _counterState.value.copy(showResetConfirmation = show)
    }

    fun confirmReset() {
        val current = _counterState.value
        if (current.count > 0 && !current.sessionSavedForCurrentRound) {
            viewModelScope.launch {
                repository.saveSession(
                    dhikrId = current.activeDhikr?.id ?: "unknown",
                    dhikrName = current.activeDhikr?.name ?: "Dzikir",
                    arabic = current.activeDhikr?.arabic ?: "",
                    count = current.count,
                    target = current.target,
                    startTime = settings.value.activeSessionStartTime,
                    moduleType = current.activeDhikr?.moduleType ?: "TASBIH"
                )
            }
        }
        _counterState.value = current.copy(
            count = 0,
            isTargetReached = false,
            showResetConfirmation = false,
            showTargetReachedDialog = false,
            sessionSavedForCurrentRound = false,
            pulseTrigger = System.currentTimeMillis()
        )
        repository.resetActiveCount()
    }

    fun dismissTargetReachedDialog() {
        _counterState.value = _counterState.value.copy(showTargetReachedDialog = false)
    }

    fun saveCurrentSessionAndContinue(restartCount: Boolean) {
        val current = _counterState.value
        if (current.count > 0 && !current.sessionSavedForCurrentRound) {
            viewModelScope.launch {
                repository.saveSession(
                    dhikrId = current.activeDhikr?.id ?: "unknown",
                    dhikrName = current.activeDhikr?.name ?: "Dzikir",
                    arabic = current.activeDhikr?.arabic ?: "",
                    count = current.count,
                    target = current.target,
                    startTime = settings.value.activeSessionStartTime,
                    moduleType = current.activeDhikr?.moduleType ?: "TASBIH"
                )
                _events.emit(NoorUiEvent.ShowToast("Sesi dzikir tersimpan di riwayat"))
            }
        }
        if (restartCount) {
            _counterState.value = current.copy(
                count = 0,
                isTargetReached = false,
                showTargetReachedDialog = false,
                sessionSavedForCurrentRound = false
            )
            repository.resetActiveCount()
        } else {
            _counterState.value = current.copy(
                showTargetReachedDialog = false,
                sessionSavedForCurrentRound = true
            )
        }
    }

    fun showChangeTargetDialog(show: Boolean) {
        _counterState.value = _counterState.value.copy(showChangeTargetDialog = show)
    }

    fun setTarget(newTarget: Int) {
        if (newTarget <= 0) return
        val current = _counterState.value
        _counterState.value = current.copy(
            target = newTarget,
            isTargetReached = current.count >= newTarget,
            showChangeTargetDialog = false,
            sessionSavedForCurrentRound = false
        )
        repository.updateActiveTarget(newTarget)
        current.activeDhikr?.let {
            viewModelScope.launch {
                repository.updateDhikrTarget(it.id, newTarget)
            }
        }
    }

    fun selectDhikr(dhikr: DhikrEntity, targetOverride: Int? = null) {
        val current = _counterState.value
        if (current.count > 0 && current.activeDhikr?.id != dhikr.id && !current.sessionSavedForCurrentRound) {
            viewModelScope.launch {
                repository.saveSession(
                    dhikrId = current.activeDhikr?.id ?: "unknown",
                    dhikrName = current.activeDhikr?.name ?: "Dzikir",
                    arabic = current.activeDhikr?.arabic ?: "",
                    count = current.count,
                    target = current.target,
                    startTime = settings.value.activeSessionStartTime,
                    moduleType = current.activeDhikr?.moduleType ?: "TASBIH"
                )
            }
        }

        val target = targetOverride ?: dhikr.target
        _counterState.value = CounterUiState(
            activeDhikr = dhikr,
            count = 0,
            target = target,
            isTargetReached = false,
            pulseTrigger = System.currentTimeMillis(),
            sessionSavedForCurrentRound = false
        )
        repository.updateActiveDhikr(dhikr.id, target)
    }

    /**
     * Cycles to the next dhikr in the active module or general tasbih list.
     */
    fun nextDhikr() {
        val current = _counterState.value.activeDhikr
        val list = allDhikrs.value.ifEmpty { NoorDatabase.DEFAULT_DHIKRS }
        val pool = if (current != null && current.moduleType != WiridModuleTypes.GENERAL) {
            list.filter { it.moduleType == current.moduleType }.ifEmpty { list }
        } else {
            list.filter { it.moduleType == WiridModuleTypes.GENERAL || it.isFavorite || it.isCustom }.ifEmpty { list }
        }

        val currentIndex = pool.indexOfFirst { it.id == current?.id }
        val nextItem = if (currentIndex >= 0 && pool.size > 1) {
            pool[(currentIndex + 1) % pool.size]
        } else {
            pool.firstOrNull()
        }

        if (nextItem != null && nextItem.id != current?.id) {
            val overrideTarget = if (nextItem.moduleType == WiridModuleTypes.AL_GHAZALI) {
                settings.value.alGhazaliTarget
            } else {
                null
            }
            selectDhikr(nextItem, overrideTarget)
            viewModelScope.launch {
                _events.emit(NoorUiEvent.ShowToast("Beralih ke: ${nextItem.name}"))
            }
        }
    }

    // ==================== WIRID MODULE ACTIONS ====================

    fun incrementWirid(dhikr: DhikrEntity, effectiveTarget: Int) {
        val now = android.os.SystemClock.elapsedRealtime()
        if (now - lastTapElapsedMs < 40L) return
        lastTapElapsedMs = now

        val prevProgress = todayWiridProgressMap.value[dhikr.id]
        val wasReached = prevProgress?.isCompleted == true

        viewModelScope.launch {
            val updated = repository.incrementWirid(dhikr, effectiveTarget)
            val isVibration = settings.value.isVibrationEnabled
            val isSound = settings.value.isSoundEnabled

            if (!wasReached && updated.isCompleted) {
                hapticHelper.vibrateTargetReached(isVibration)
                soundHelper.playCompletionChime(isSound)
                _events.emit(NoorUiEvent.ShowToast("✨ Wirid selesai (${updated.target}x): ${dhikr.name}"))
            } else {
                hapticHelper.vibrateTap(isVibration)
                soundHelper.playTapChime(isSound)
            }
        }
    }

    fun resetSingleWirid(dhikr: DhikrEntity, effectiveTarget: Int) {
        viewModelScope.launch {
            repository.resetSingleWirid(dhikr, effectiveTarget)
            _events.emit(NoorUiEvent.ShowToast("Hitungan '${dhikr.name}' diatur ulang"))
        }
    }

    fun resetModuleWirids(moduleType: String) {
        viewModelScope.launch {
            repository.resetModuleWirids(moduleType)
            _events.emit(NoorUiEvent.ShowToast("Progres wirid hari ini diatur ulang"))
        }
    }

    fun setAlGhazaliTarget(target: Int) {
        repository.updateAlGhazaliTarget(target)
    }

    fun toggleFavorite(dhikr: DhikrEntity) {
        viewModelScope.launch {
            repository.toggleFavorite(dhikr.id, dhikr.isFavorite)
        }
    }

    fun saveCustomDhikr(
        id: String?,
        name: String,
        arabic: String,
        transliteration: String,
        translation: String,
        category: String,
        target: Int,
        isFavorite: Boolean
    ) {
        viewModelScope.launch {
            val savedId = repository.saveCustomDhikr(
                id = id,
                name = name,
                arabic = arabic,
                transliteration = transliteration,
                translation = translation,
                category = category,
                target = target,
                isFavorite = isFavorite
            )
            _events.emit(NoorUiEvent.ShowToast("Dzikir berhasil disimpan"))
            if (_counterState.value.activeDhikr?.id == savedId) {
                val updated = repository.getDhikrById(savedId)
                if (updated != null) {
                    _counterState.value = _counterState.value.copy(activeDhikr = updated)
                }
            }
        }
    }

    fun deleteCustomDhikr(dhikr: DhikrEntity) {
        viewModelScope.launch {
            repository.deleteCustomDhikr(dhikr)
            _events.emit(NoorUiEvent.ShowToast("Dzikir '${dhikr.name}' dihapus"))
            if (_counterState.value.activeDhikr?.id == dhikr.id) {
                val defaultDhikr = NoorDatabase.DEFAULT_DHIKRS.first()
                selectDhikr(defaultDhikr)
            }
        }
    }

    fun deleteSession(session: SessionEntity) {
        viewModelScope.launch {
            repository.deleteSession(session)
            _events.emit(NoorUiEvent.ShowToast("Catatan riwayat dihapus"))
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
    }

    fun updateThemeMode(mode: String) = repository.updateThemeMode(mode)
    fun updateVibration(enabled: Boolean) = repository.updateVibration(enabled)
    fun updateSound(enabled: Boolean) = repository.updateSound(enabled)
    fun updateDefaultTarget(target: Int) = repository.updateDefaultTarget(target)
    fun updateAnimationIntensity(intensity: String) = repository.updateAnimationIntensity(intensity)
    fun updateKeepScreenOn(enabled: Boolean) = repository.updateKeepScreenOn(enabled)

    suspend fun exportBackupJson(): String = repository.exportBackupJson()

    suspend fun restoreBackupJson(json: String): Result<Int> {
        val result = repository.restoreBackupJson(json)
        if (result.isSuccess) {
            _events.emit(NoorUiEvent.ShowToast("Cadangan berhasil dipulihkan"))
        }
        return result
    }

    fun clearAllData() {
        viewModelScope.launch {
            repository.clearAllData()
            val defaultDhikr = NoorDatabase.DEFAULT_DHIKRS.first()
            _counterState.value = CounterUiState(
                activeDhikr = defaultDhikr,
                count = 0,
                target = defaultDhikr.target
            )
            _events.emit(NoorUiEvent.ShowToast("Semua data aplikasi telah dibersihkan"))
        }
    }

    override fun onCleared() {
        super.onCleared()
        soundHelper.release()
    }
}
