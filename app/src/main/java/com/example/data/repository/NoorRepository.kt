package com.example.data.repository

import com.example.data.dao.DhikrDao
import com.example.data.dao.SessionDao
import com.example.data.dao.WiridProgressDao
import com.example.data.database.NoorDatabase
import com.example.data.database.WiridModuleTypes
import com.example.data.database.WiridSourceTypes
import com.example.data.model.DhikrEntity
import com.example.data.model.SessionEntity
import com.example.data.model.WiridProgressEntity
import com.example.data.preferences.SettingsManager
import com.example.data.preferences.UserSettings
import com.example.util.DateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

data class NoorStats(
    val totalDhikr: Int = 0,
    val completedSessions: Int = 0,
    val todayDhikr: Int = 0,
    val weeklyDhikr: Int = 0,
    val monthlyDhikr: Int = 0
)

class NoorRepository(
    private val dhikrDao: DhikrDao,
    private val sessionDao: SessionDao,
    private val wiridProgressDao: WiridProgressDao,
    private val settingsManager: SettingsManager
) {
    val allDhikrs: Flow<List<DhikrEntity>> = dhikrDao.getAllDhikrs()
    val allSessions: Flow<List<SessionEntity>> = sessionDao.getAllSessions()
    val settings: StateFlow<UserSettings> = settingsManager.settings

    val totalDhikrCount: Flow<Int> = sessionDao.getTotalDhikrCount()
    val totalCompletedSessions: Flow<Int> = sessionDao.getTotalCompletedSessions()

    fun getDhikrsByModule(moduleType: String): Flow<List<DhikrEntity>> =
        dhikrDao.getDhikrsByModule(moduleType)

    fun getTodayDhikrCount(): Flow<Int> =
        sessionDao.getTodayDhikrCount(DateUtils.getStartOfTodayMillis())

    fun getWeeklyDhikrCount(): Flow<Int> =
        sessionDao.getDhikrCountSince(DateUtils.getStartOfWeekMillis())

    fun getMonthlyDhikrCount(): Flow<Int> =
        sessionDao.getDhikrCountSince(DateUtils.getStartOfMonthMillis())

    fun getTodayWiridProgress(dateKey: String = DateUtils.todayDateKey()): Flow<List<WiridProgressEntity>> =
        wiridProgressDao.getProgressForDate(dateKey)

    fun getTodayCompletedWiridCount(dateKey: String = DateUtils.todayDateKey()): Flow<Int> =
        wiridProgressDao.getCompletedCountForDate(dateKey)

    suspend fun ensureSeeded() = withContext(Dispatchers.IO) {
        dhikrDao.insertDhikrsIgnore(NoorDatabase.ALL_V2_SEED_DHIKRS)
    }

    suspend fun getDhikrById(id: String): DhikrEntity? = withContext(Dispatchers.IO) {
        var dhikr = dhikrDao.getDhikrById(id)
        if (dhikr == null) {
            dhikr = NoorDatabase.ALL_V2_SEED_DHIKRS.find { it.id == id }
            if (dhikr != null) {
                dhikrDao.insertDhikrsIgnore(listOf(dhikr))
            }
        }
        dhikr
    }

    suspend fun saveCustomDhikr(
        id: String?,
        name: String,
        arabic: String,
        transliteration: String,
        translation: String,
        category: String,
        target: Int,
        isFavorite: Boolean
    ): String = withContext(Dispatchers.IO) {
        val dhikrId = id ?: UUID.randomUUID().toString()
        val existing = id?.let { dhikrDao.getDhikrById(it) }
        val dhikr = DhikrEntity(
            id = dhikrId,
            name = name.trim(),
            arabic = arabic.trim(),
            transliteration = transliteration.trim(),
            translation = translation.trim(),
            category = category.trim().ifEmpty { "Kustom" },
            target = target.coerceAtLeast(1),
            isCustom = existing?.isCustom ?: true,
            isFavorite = isFavorite,
            createdAt = existing?.createdAt ?: System.currentTimeMillis(),
            source = existing?.source?.ifBlank { "Catatan Pribadi" } ?: "Catatan Pribadi",
            moduleType = existing?.moduleType ?: WiridModuleTypes.GENERAL,
            sortOrder = existing?.sortOrder ?: 999,
            sourceType = existing?.sourceType ?: WiridSourceTypes.KUSTOM
        )
        dhikrDao.insertDhikr(dhikr)
        dhikrId
    }

    suspend fun toggleFavorite(dhikrId: String, currentFavorite: Boolean) = withContext(Dispatchers.IO) {
        dhikrDao.updateFavorite(dhikrId, !currentFavorite)
    }

    suspend fun updateDhikrTarget(dhikrId: String, newTarget: Int) = withContext(Dispatchers.IO) {
        dhikrDao.updateTarget(dhikrId, newTarget.coerceAtLeast(1))
    }

    suspend fun deleteCustomDhikr(dhikr: DhikrEntity) = withContext(Dispatchers.IO) {
        if (dhikr.isCustom) {
            dhikrDao.deleteDhikr(dhikr)
        }
    }

    suspend fun saveSession(
        dhikrId: String,
        dhikrName: String,
        arabic: String,
        count: Int,
        target: Int,
        startTime: Long,
        moduleType: String = "TASBIH"
    ): SessionEntity = withContext(Dispatchers.IO) {
        val now = System.currentTimeMillis()
        val duration = ((now - startTime) / 1000L).coerceAtLeast(0L)
        val session = SessionEntity(
            id = UUID.randomUUID().toString(),
            dhikrId = dhikrId,
            dhikrName = dhikrName,
            arabic = arabic,
            count = count,
            target = target,
            isCompleted = count >= target && target > 0,
            timestamp = now,
            durationSeconds = duration,
            moduleType = moduleType
        )
        sessionDao.insertSession(session)
        session
    }

    /**
     * Increment a wirid item for today.
     * Prevents duplicate session entries: records a session only once when target is first reached in a cycle.
     */
    suspend fun incrementWirid(
        dhikr: DhikrEntity,
        effectiveTarget: Int,
        dateKey: String = DateUtils.todayDateKey()
    ): WiridProgressEntity = withContext(Dispatchers.IO) {
        val progressId = "${dateKey}_${dhikr.id}"
        val existing = wiridProgressDao.getProgressById(progressId)
        val newCount = (existing?.currentCount ?: 0) + 1
        val reached = newCount >= effectiveTarget && effectiveTarget > 0
        val alreadyRecorded = existing?.sessionRecorded == true

        var shouldMarkRecorded = alreadyRecorded
        if (reached && !alreadyRecorded) {
            sessionDao.insertSession(
                SessionEntity(
                    id = UUID.randomUUID().toString(),
                    dhikrId = dhikr.id,
                    dhikrName = dhikr.name,
                    arabic = dhikr.arabic,
                    count = effectiveTarget,
                    target = effectiveTarget,
                    isCompleted = true,
                    timestamp = System.currentTimeMillis(),
                    durationSeconds = 0L,
                    moduleType = dhikr.moduleType
                )
            )
            shouldMarkRecorded = true
        }

        val updated = WiridProgressEntity(
            id = progressId,
            wiridId = dhikr.id,
            moduleType = dhikr.moduleType,
            dateKey = dateKey,
            currentCount = newCount,
            target = effectiveTarget,
            isCompleted = reached,
            sessionRecorded = shouldMarkRecorded,
            updatedAt = System.currentTimeMillis()
        )
        wiridProgressDao.upsertProgress(updated)
        updated
    }

    suspend fun resetSingleWirid(
        dhikr: DhikrEntity,
        effectiveTarget: Int,
        dateKey: String = DateUtils.todayDateKey()
    ) = withContext(Dispatchers.IO) {
        val progressId = "${dateKey}_${dhikr.id}"
        val existing = wiridProgressDao.getProgressById(progressId)
        // Save incomplete partial count if user did > 0 and hasn't recorded yet
        if (existing != null && existing.currentCount > 0 && !existing.sessionRecorded) {
            sessionDao.insertSession(
                SessionEntity(
                    id = UUID.randomUUID().toString(),
                    dhikrId = dhikr.id,
                    dhikrName = dhikr.name,
                    arabic = dhikr.arabic,
                    count = existing.currentCount,
                    target = effectiveTarget,
                    isCompleted = false,
                    timestamp = System.currentTimeMillis(),
                    durationSeconds = 0L,
                    moduleType = dhikr.moduleType
                )
            )
        }
        wiridProgressDao.upsertProgress(
            WiridProgressEntity(
                id = progressId,
                wiridId = dhikr.id,
                moduleType = dhikr.moduleType,
                dateKey = dateKey,
                currentCount = 0,
                target = effectiveTarget,
                isCompleted = false,
                sessionRecorded = false,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun resetModuleWirids(
        moduleType: String,
        dateKey: String = DateUtils.todayDateKey()
    ) = withContext(Dispatchers.IO) {
        wiridProgressDao.resetModuleProgressForDate(dateKey, moduleType)
    }

    suspend fun deleteSession(session: SessionEntity) = withContext(Dispatchers.IO) {
        sessionDao.deleteSession(session)
    }

    fun updateActiveCount(count: Int) = settingsManager.updateActiveCount(count)
    fun updateActiveTarget(target: Int) = settingsManager.updateActiveTarget(target)
    fun updateActiveDhikr(dhikrId: String, target: Int) = settingsManager.updateActiveDhikr(dhikrId, target)
    fun resetActiveCount() = settingsManager.resetActiveCount()
    fun updateThemeMode(mode: String) = settingsManager.updateThemeMode(mode)
    fun updateVibration(enabled: Boolean) = settingsManager.updateVibration(enabled)
    fun updateSound(enabled: Boolean) = settingsManager.updateSound(enabled)
    fun updateDefaultTarget(target: Int) = settingsManager.updateDefaultTarget(target)
    fun updateAnimationIntensity(intensity: String) = settingsManager.updateAnimationIntensity(intensity)
    fun updateKeepScreenOn(enabled: Boolean) = settingsManager.updateKeepScreenOn(enabled)
    fun updateAlGhazaliTarget(target: Int) = settingsManager.updateAlGhazaliTarget(target)

    suspend fun exportBackupJson(): String = withContext(Dispatchers.IO) {
        val root = JSONObject()
        root.put("appName", "TASBIH NOOR")
        root.put("version", 2)
        root.put("exportDate", System.currentTimeMillis())

        val allDhikrs = dhikrDao.getAllDhikrsList()
        val customDhikrsArray = JSONArray()
        for (d in allDhikrs.filter { it.isCustom }) {
            val dObj = JSONObject()
            dObj.put("id", d.id)
            dObj.put("name", d.name)
            dObj.put("arabic", d.arabic)
            dObj.put("transliteration", d.transliteration)
            dObj.put("translation", d.translation)
            dObj.put("category", d.category)
            dObj.put("target", d.target)
            dObj.put("isFavorite", d.isFavorite)
            dObj.put("createdAt", d.createdAt)
            dObj.put("source", d.source)
            customDhikrsArray.put(dObj)
        }
        root.put("customDhikrs", customDhikrsArray)

        val sessions = sessionDao.getAllSessionsList()
        val sessionsArray = JSONArray()
        for (s in sessions) {
            val sessionObj = JSONObject()
            sessionObj.put("id", s.id)
            sessionObj.put("dhikrId", s.dhikrId)
            sessionObj.put("dhikrName", s.dhikrName)
            sessionObj.put("arabic", s.arabic)
            sessionObj.put("count", s.count)
            sessionObj.put("target", s.target)
            sessionObj.put("isCompleted", s.isCompleted)
            sessionObj.put("timestamp", s.timestamp)
            sessionObj.put("durationSeconds", s.durationSeconds)
            sessionObj.put("moduleType", s.moduleType)
            sessionsArray.put(sessionObj)
        }
        root.put("sessions", sessionsArray)

        val progressList = wiridProgressDao.getAllProgressList()
        val progressArray = JSONArray()
        for (p in progressList) {
            val pObj = JSONObject()
            pObj.put("id", p.id)
            pObj.put("wiridId", p.wiridId)
            pObj.put("moduleType", p.moduleType)
            pObj.put("dateKey", p.dateKey)
            pObj.put("currentCount", p.currentCount)
            pObj.put("target", p.target)
            pObj.put("isCompleted", p.isCompleted)
            pObj.put("sessionRecorded", p.sessionRecorded)
            pObj.put("updatedAt", p.updatedAt)
            progressArray.put(pObj)
        }
        root.put("wiridProgress", progressArray)

        val currentSettings = settingsManager.settings.value
        val settingsObj = JSONObject()
        settingsObj.put("themeMode", currentSettings.themeMode)
        settingsObj.put("isVibrationEnabled", currentSettings.isVibrationEnabled)
        settingsObj.put("isSoundEnabled", currentSettings.isSoundEnabled)
        settingsObj.put("defaultTarget", currentSettings.defaultTarget)
        settingsObj.put("animationIntensity", currentSettings.animationIntensity)
        settingsObj.put("keepScreenOn", currentSettings.keepScreenOn)
        settingsObj.put("alGhazaliTarget", currentSettings.alGhazaliTarget)
        root.put("settings", settingsObj)

        root.toString(2)
    }

    suspend fun restoreBackupJson(jsonString: String): Result<Int> = withContext(Dispatchers.IO) {
        try {
            val root = JSONObject(jsonString)
            val appName = root.optString("appName")
            if (appName != "NOOR COUNTER" && appName != "TASBIH NOOR") {
                return@withContext Result.failure(IllegalArgumentException("Format file cadangan tidak valid untuk TASBIH NOOR / NOOR COUNTER"))
            }

            var restoredCount = 0

            if (root.has("customDhikrs")) {
                val dArray = root.getJSONArray("customDhikrs")
                val customList = mutableListOf<DhikrEntity>()
                for (i in 0 until dArray.length()) {
                    val obj = dArray.getJSONObject(i)
                    customList.add(
                        DhikrEntity(
                            id = obj.optString("id", UUID.randomUUID().toString()),
                            name = obj.optString("name", "Dzikir Kustom"),
                            arabic = obj.optString("arabic", ""),
                            transliteration = obj.optString("transliteration", ""),
                            translation = obj.optString("translation", ""),
                            category = obj.optString("category", "Kustom"),
                            target = obj.optInt("target", 33).coerceAtLeast(1),
                            isCustom = true,
                            isFavorite = obj.optBoolean("isFavorite", false),
                            createdAt = obj.optLong("createdAt", System.currentTimeMillis()),
                            source = obj.optString("source", "Catatan Pribadi"),
                            moduleType = WiridModuleTypes.GENERAL,
                            sortOrder = 999,
                            sourceType = WiridSourceTypes.KUSTOM
                        )
                    )
                }
                if (customList.isNotEmpty()) {
                    dhikrDao.insertDhikrs(customList)
                    restoredCount += customList.size
                }
            }

            if (root.has("sessions")) {
                val sessionsArray = root.getJSONArray("sessions")
                val sessionList = mutableListOf<SessionEntity>()
                for (i in 0 until sessionsArray.length()) {
                    val obj = sessionsArray.getJSONObject(i)
                    sessionList.add(
                        SessionEntity(
                            id = obj.optString("id", UUID.randomUUID().toString()),
                            dhikrId = obj.optString("dhikrId", "subhanallah"),
                            dhikrName = obj.optString("dhikrName", "Dhikr"),
                            arabic = obj.optString("arabic", ""),
                            count = obj.optInt("count", 0),
                            target = obj.optInt("target", 33),
                            isCompleted = obj.optBoolean("isCompleted", false),
                            timestamp = obj.optLong("timestamp", System.currentTimeMillis()),
                            durationSeconds = obj.optLong("durationSeconds", 0L),
                            moduleType = obj.optString("moduleType", "TASBIH")
                        )
                    )
                }
                if (sessionList.isNotEmpty()) {
                    sessionDao.insertSessions(sessionList)
                    restoredCount += sessionList.size
                }
            }

            if (root.has("wiridProgress")) {
                val pArray = root.getJSONArray("wiridProgress")
                val pList = mutableListOf<WiridProgressEntity>()
                for (i in 0 until pArray.length()) {
                    val obj = pArray.getJSONObject(i)
                    pList.add(
                        WiridProgressEntity(
                            id = obj.getString("id"),
                            wiridId = obj.getString("wiridId"),
                            moduleType = obj.optString("moduleType", WiridModuleTypes.AL_GHAZALI),
                            dateKey = obj.getString("dateKey"),
                            currentCount = obj.optInt("currentCount", 0),
                            target = obj.optInt("target", 100),
                            isCompleted = obj.optBoolean("isCompleted", false),
                            sessionRecorded = obj.optBoolean("sessionRecorded", false),
                            updatedAt = obj.optLong("updatedAt", System.currentTimeMillis())
                        )
                    )
                }
                if (pList.isNotEmpty()) {
                    wiridProgressDao.upsertAll(pList)
                    restoredCount += pList.size
                }
            }

            if (root.has("settings")) {
                val s = root.getJSONObject("settings")
                if (s.has("themeMode")) settingsManager.updateThemeMode(s.getString("themeMode"))
                if (s.has("isVibrationEnabled")) settingsManager.updateVibration(s.getBoolean("isVibrationEnabled"))
                if (s.has("isSoundEnabled")) settingsManager.updateSound(s.getBoolean("isSoundEnabled"))
                if (s.has("defaultTarget")) settingsManager.updateDefaultTarget(s.getInt("defaultTarget"))
                if (s.has("animationIntensity")) settingsManager.updateAnimationIntensity(s.getString("animationIntensity"))
                if (s.has("keepScreenOn")) settingsManager.updateKeepScreenOn(s.getBoolean("keepScreenOn"))
                if (s.has("alGhazaliTarget")) settingsManager.updateAlGhazaliTarget(s.getInt("alGhazaliTarget"))
            }

            Result.success(restoredCount)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun clearAllData() = withContext(Dispatchers.IO) {
        sessionDao.clearAllSessions()
        wiridProgressDao.clearAllProgress()
        dhikrDao.deleteAllCustomDhikrs()
        settingsManager.clearAllPreferences()
    }
}
