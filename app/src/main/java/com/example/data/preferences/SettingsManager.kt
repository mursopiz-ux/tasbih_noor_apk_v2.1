package com.example.data.preferences

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class UserSettings(
    val themeMode: String = "DARK", // DARK, LIGHT, SYSTEM
    val isVibrationEnabled: Boolean = true,
    val isSoundEnabled: Boolean = false,
    val defaultTarget: Int = 33,
    val animationIntensity: String = "FULL", // FULL, GENTLE, MINIMAL
    val keepScreenOn: Boolean = false,
    val alGhazaliTarget: Int = 100, // 10, 70, 100
    val activeDhikrId: String = "subhanallah",
    val activeCount: Int = 0,
    val activeTarget: Int = 33,
    val activeSessionStartTime: Long = System.currentTimeMillis()
)

class SettingsManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("noor_counter_prefs", Context.MODE_PRIVATE)

    private val _settings = MutableStateFlow(loadSettings())
    val settings: StateFlow<UserSettings> = _settings.asStateFlow()

    private fun loadSettings(): UserSettings {
        return UserSettings(
            themeMode = prefs.getString("theme_mode", "DARK") ?: "DARK",
            isVibrationEnabled = prefs.getBoolean("vibration_enabled", true),
            isSoundEnabled = prefs.getBoolean("sound_enabled", false),
            defaultTarget = prefs.getInt("default_target", 33),
            animationIntensity = prefs.getString("anim_intensity", "FULL") ?: "FULL",
            keepScreenOn = prefs.getBoolean("keep_screen_on", false),
            alGhazaliTarget = prefs.getInt("al_ghazali_target", 100),
            activeDhikrId = prefs.getString("active_dhikr_id", "subhanallah") ?: "subhanallah",
            activeCount = prefs.getInt("active_count", 0),
            activeTarget = prefs.getInt("active_target", 33),
            activeSessionStartTime = prefs.getLong("active_start_time", System.currentTimeMillis())
        )
    }

    fun updateThemeMode(mode: String) {
        prefs.edit().putString("theme_mode", mode).apply()
        _settings.value = _settings.value.copy(themeMode = mode)
    }

    fun updateVibration(enabled: Boolean) {
        prefs.edit().putBoolean("vibration_enabled", enabled).apply()
        _settings.value = _settings.value.copy(isVibrationEnabled = enabled)
    }

    fun updateSound(enabled: Boolean) {
        prefs.edit().putBoolean("sound_enabled", enabled).apply()
        _settings.value = _settings.value.copy(isSoundEnabled = enabled)
    }

    fun updateDefaultTarget(target: Int) {
        prefs.edit().putInt("default_target", target).apply()
        _settings.value = _settings.value.copy(defaultTarget = target)
    }

    fun updateAnimationIntensity(intensity: String) {
        prefs.edit().putString("anim_intensity", intensity).apply()
        _settings.value = _settings.value.copy(animationIntensity = intensity)
    }

    fun updateKeepScreenOn(enabled: Boolean) {
        prefs.edit().putBoolean("keep_screen_on", enabled).apply()
        _settings.value = _settings.value.copy(keepScreenOn = enabled)
    }

    fun updateAlGhazaliTarget(target: Int) {
        val valid = if (target in listOf(10, 70, 100)) target else 100
        prefs.edit().putInt("al_ghazali_target", valid).apply()
        _settings.value = _settings.value.copy(alGhazaliTarget = valid)
    }

    fun updateActiveDhikr(dhikrId: String, target: Int) {
        val now = System.currentTimeMillis()
        prefs.edit()
            .putString("active_dhikr_id", dhikrId)
            .putInt("active_count", 0)
            .putInt("active_target", target)
            .putLong("active_start_time", now)
            .apply()
        _settings.value = _settings.value.copy(
            activeDhikrId = dhikrId,
            activeCount = 0,
            activeTarget = target,
            activeSessionStartTime = now
        )
    }

    fun updateActiveCount(count: Int) {
        prefs.edit().putInt("active_count", count).apply()
        _settings.value = _settings.value.copy(activeCount = count)
    }

    fun updateActiveTarget(target: Int) {
        prefs.edit().putInt("active_target", target).apply()
        _settings.value = _settings.value.copy(activeTarget = target)
    }

    fun resetActiveCount() {
        val now = System.currentTimeMillis()
        prefs.edit()
            .putInt("active_count", 0)
            .putLong("active_start_time", now)
            .apply()
        _settings.value = _settings.value.copy(activeCount = 0, activeSessionStartTime = now)
    }

    fun clearAllPreferences() {
        prefs.edit().clear().apply()
        _settings.value = loadSettings()
    }
}
