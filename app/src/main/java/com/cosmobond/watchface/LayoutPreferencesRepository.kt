package com.cosmobond.watchface

import android.content.Context
import android.content.SharedPreferences
import android.graphics.PointF

internal data class LayoutPreferences(
    val timePosition: PointF = PointF(0.5f, 0.34f),
    val datePosition: PointF = PointF(0.5f, 0.43f),
    val musicPosition: PointF = PointF(0.5f, 0.75f),
    val timeFont: FontVariant = FontVariant.DEFAULT,
    val dateFont: FontVariant = FontVariant.DEFAULT,
    val dateFormat: DateFormatVariant = DateFormatVariant.SHORT,
    val showDate: Boolean = true,
    val showSeconds: Boolean = false,
    val use24Hour: Boolean = true,
    val timeTextSizeSp: Float = 64f,
    val dateTextSizeSp: Float = 28f,
    val musicTextSizeSp: Float = 22f,
)

internal enum class FontVariant { DEFAULT, MONO, SERIF }

internal enum class DateFormatVariant { SHORT, LONG, NUMERIC }

internal class LayoutPreferencesRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val listeners = mutableSetOf<(LayoutPreferences) -> Unit>()
    private val prefListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key in trackedKeys) {
                notifyListeners()
            }
        }

    init {
        prefs.registerOnSharedPreferenceChangeListener(prefListener)
    }

    fun current(): LayoutPreferences =
        LayoutPreferences(
            timePosition = PointF(prefs.getFloat(KEY_TIME_X, 0.5f), prefs.getFloat(KEY_TIME_Y, 0.34f)),
            datePosition = PointF(prefs.getFloat(KEY_DATE_X, 0.5f), prefs.getFloat(KEY_DATE_Y, 0.43f)),
            musicPosition = PointF(prefs.getFloat(KEY_MUSIC_X, 0.5f), prefs.getFloat(KEY_MUSIC_Y, 0.75f)),
            timeFont = prefs.getString(KEY_TIME_FONT, FontVariant.DEFAULT.name).toFontVariant(),
            dateFont = prefs.getString(KEY_DATE_FONT, FontVariant.DEFAULT.name).toFontVariant(),
            dateFormat = prefs.getString(KEY_DATE_FORMAT, DateFormatVariant.SHORT.name).toDateFormatVariant(),
            showDate = prefs.getBoolean(KEY_SHOW_DATE, true),
            showSeconds = prefs.getBoolean(KEY_SHOW_SECONDS, false),
            use24Hour = prefs.getBoolean(KEY_USE_24_HOUR, true),
            timeTextSizeSp = prefs.getFloat(KEY_TIME_TEXT_SIZE, 64f),
            dateTextSizeSp = prefs.getFloat(KEY_DATE_TEXT_SIZE, 28f),
            musicTextSizeSp = prefs.getFloat(KEY_MUSIC_TEXT_SIZE, 22f),
        )

    fun setTimePosition(position: PointF) {
        prefs.edit().putFloat(KEY_TIME_X, position.x).putFloat(KEY_TIME_Y, position.y).apply()
    }

    fun setDatePosition(position: PointF) {
        prefs.edit().putFloat(KEY_DATE_X, position.x).putFloat(KEY_DATE_Y, position.y).apply()
    }

    fun setMusicPosition(position: PointF) {
        prefs.edit().putFloat(KEY_MUSIC_X, position.x).putFloat(KEY_MUSIC_Y, position.y).apply()
    }

    fun setTimeFont(variant: FontVariant) {
        prefs.edit().putString(KEY_TIME_FONT, variant.name).apply()
    }

    fun setDateFont(variant: FontVariant) {
        prefs.edit().putString(KEY_DATE_FONT, variant.name).apply()
    }

    fun setDateFormat(variant: DateFormatVariant) {
        prefs.edit().putString(KEY_DATE_FORMAT, variant.name).apply()
    }

    fun setShowDate(show: Boolean) {
        prefs.edit().putBoolean(KEY_SHOW_DATE, show).apply()
    }

    fun setShowSeconds(show: Boolean) {
        prefs.edit().putBoolean(KEY_SHOW_SECONDS, show).apply()
    }

    fun setUse24Hour(use24: Boolean) {
        prefs.edit().putBoolean(KEY_USE_24_HOUR, use24).apply()
    }

    fun setTimeTextSize(sizeSp: Float) {
        prefs.edit().putFloat(KEY_TIME_TEXT_SIZE, sizeSp).apply()
    }

    fun setDateTextSize(sizeSp: Float) {
        prefs.edit().putFloat(KEY_DATE_TEXT_SIZE, sizeSp).apply()
    }

    fun setMusicTextSize(sizeSp: Float) {
        prefs.edit().putFloat(KEY_MUSIC_TEXT_SIZE, sizeSp).apply()
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

    private fun String?.toFontVariant(): FontVariant =
        when (this) {
            FontVariant.MONO.name -> FontVariant.MONO
            FontVariant.SERIF.name -> FontVariant.SERIF
            else -> FontVariant.DEFAULT
        }

    private fun String?.toDateFormatVariant(): DateFormatVariant =
        when (this) {
            DateFormatVariant.LONG.name -> DateFormatVariant.LONG
            DateFormatVariant.NUMERIC.name -> DateFormatVariant.NUMERIC
            else -> DateFormatVariant.SHORT
        }

    companion object {
        private const val PREFS_NAME = "companion_layout"
        private const val KEY_TIME_X = "time_x"
        private const val KEY_TIME_Y = "time_y"
        private const val KEY_DATE_X = "date_x"
        private const val KEY_DATE_Y = "date_y"
        private const val KEY_MUSIC_X = "music_x"
        private const val KEY_MUSIC_Y = "music_y"
        private const val KEY_TIME_FONT = "time_font"
        private const val KEY_DATE_FONT = "date_font"
        private const val KEY_DATE_FORMAT = "date_format"
        private const val KEY_SHOW_DATE = "show_date"
        private const val KEY_SHOW_SECONDS = "show_seconds"
        private const val KEY_USE_24_HOUR = "use_24_hour"
        private const val KEY_TIME_TEXT_SIZE = "time_text_size"
        private const val KEY_DATE_TEXT_SIZE = "date_text_size"
        private const val KEY_MUSIC_TEXT_SIZE = "music_text_size"

        private val trackedKeys =
            setOf(
                KEY_TIME_X,
                KEY_TIME_Y,
                KEY_DATE_X,
                KEY_DATE_Y,
                KEY_MUSIC_X,
                KEY_MUSIC_Y,
                KEY_TIME_FONT,
                KEY_DATE_FONT,
                KEY_DATE_FORMAT,
                KEY_SHOW_DATE,
                KEY_SHOW_SECONDS,
                KEY_USE_24_HOUR,
                KEY_TIME_TEXT_SIZE,
                KEY_DATE_TEXT_SIZE,
                KEY_MUSIC_TEXT_SIZE,
            )
    }
}
