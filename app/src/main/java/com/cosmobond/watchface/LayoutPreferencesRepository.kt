package com.cosmobond.watchface

import android.content.Context
import android.content.SharedPreferences

internal data class LayoutPreferences(
    val timePosition: TimePosition = TimePosition.CENTER,
    val fontVariant: FontVariant = FontVariant.DEFAULT,
    val showDate: Boolean = true,
)

internal enum class TimePosition { TOP, CENTER, BOTTOM }

internal enum class FontVariant { DEFAULT, MONO }

internal class LayoutPreferencesRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val listeners = mutableSetOf<(LayoutPreferences) -> Unit>()
    private val prefListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == KEY_TIME_POSITION || key == KEY_FONT_VARIANT || key == KEY_SHOW_DATE) {
                notifyListeners()
            }
        }

    init {
        prefs.registerOnSharedPreferenceChangeListener(prefListener)
    }

    fun current(): LayoutPreferences =
        LayoutPreferences(
            timePosition =
                when (prefs.getString(KEY_TIME_POSITION, TimePosition.CENTER.name)) {
                    TimePosition.TOP.name -> TimePosition.TOP
                    TimePosition.BOTTOM.name -> TimePosition.BOTTOM
                    else -> TimePosition.CENTER
                },
            fontVariant =
                when (prefs.getString(KEY_FONT_VARIANT, FontVariant.DEFAULT.name)) {
                    FontVariant.MONO.name -> FontVariant.MONO
                    else -> FontVariant.DEFAULT
                },
            showDate = prefs.getBoolean(KEY_SHOW_DATE, true),
        )

    fun setTimePosition(position: TimePosition) {
        prefs.edit().putString(KEY_TIME_POSITION, position.name).apply()
    }

    fun setFontVariant(variant: FontVariant) {
        prefs.edit().putString(KEY_FONT_VARIANT, variant.name).apply()
    }

    fun setShowDate(show: Boolean) {
        prefs.edit().putBoolean(KEY_SHOW_DATE, show).apply()
    }

    fun addListener(listener: (LayoutPreferences) -> Unit) {
        listeners += listener
    }

    fun removeListener(listener: (LayoutPreferences) -> Unit) {
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
        private const val PREFS_NAME = "companion_layout"
        private const val KEY_TIME_POSITION = "time_position"
        private const val KEY_FONT_VARIANT = "font_variant"
        private const val KEY_SHOW_DATE = "show_date"
    }
}
