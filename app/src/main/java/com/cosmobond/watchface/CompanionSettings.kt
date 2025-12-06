package com.cosmobond.watchface

import android.content.Context
import android.content.SharedPreferences

internal data class CompanionSettings(
    val reducedMotion: Boolean = false,
    val hapticsEnabled: Boolean = true,
    val micToggleEnabled: Boolean = false,
    val selectedPet: String = CompanionSettingsRepository.DEFAULT_PET_ID,
)

internal class CompanionSettingsRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val listeners = mutableSetOf<(CompanionSettings) -> Unit>()
    private val prefListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == KEY_REDUCED_MOTION || key == KEY_HAPTICS_ENABLED || key == KEY_MIC_PLACEHOLDER) {
                notifyListeners()
            }
        }

    init {
        prefs.registerOnSharedPreferenceChangeListener(prefListener)
    }

    fun current(): CompanionSettings =
        CompanionSettings(
            reducedMotion = prefs.getBoolean(KEY_REDUCED_MOTION, false),
            hapticsEnabled = prefs.getBoolean(KEY_HAPTICS_ENABLED, true),
            micToggleEnabled = prefs.getBoolean(KEY_MIC_PLACEHOLDER, false),
            selectedPet = prefs.getString(KEY_SELECTED_PET, DEFAULT_PET_ID) ?: DEFAULT_PET_ID,
        )

    fun setReducedMotion(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_REDUCED_MOTION, enabled).apply()
    }

    fun setHapticsEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_HAPTICS_ENABLED, enabled).apply()
    }

    fun setMicToggle(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_MIC_PLACEHOLDER, enabled).apply()
    }

    fun setSelectedPet(petId: String) {
        prefs.edit().putString(KEY_SELECTED_PET, petId).apply()
    }

    fun addListener(listener: (CompanionSettings) -> Unit) {
        listeners += listener
    }

    fun removeListener(listener: (CompanionSettings) -> Unit) {
        listeners -= listener
    }

    fun close() {
        prefs.unregisterOnSharedPreferenceChangeListener(prefListener)
        listeners.clear()
    }

    private fun notifyListeners() {
        val snapshot = current()
        listeners.forEach { it.invoke(snapshot) }
    }

    companion object {
        private const val PREFS_NAME = "companion_settings"
        private const val KEY_REDUCED_MOTION = "reduced_motion"
        private const val KEY_HAPTICS_ENABLED = "haptics_enabled"
        private const val KEY_MIC_PLACEHOLDER = "mic_placeholder"
        private const val KEY_SELECTED_PET = "selected_pet"
        const val DEFAULT_PET_ID = "beatbunny"
    }
}
