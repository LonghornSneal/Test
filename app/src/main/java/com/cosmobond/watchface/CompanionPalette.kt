package com.cosmobond.watchface

import androidx.wear.watchface.DrawMode
import androidx.wear.watchface.style.UserStyle

internal object CompanionPalette {
    private const val COSMIC_BLUE = 0xFFA6B6FF.toInt()
    private const val STARLIGHT = 0xFFF5E5B9.toInt()
    private const val AMBIENT_ACCENT = 0xFFB0B0B0.toInt()
    private const val BACKGROUND_COLOR = 0xFF0A0D18.toInt()
    private const val AMBIENT_BACKGROUND_COLOR = 0xFF000000.toInt()

    fun accentFor(
        userStyle: UserStyle,
        drawMode: DrawMode = DrawMode.INTERACTIVE,
    ): Int {
        if (drawMode == DrawMode.AMBIENT) return AMBIENT_ACCENT
        return when (CompanionUserStyle.accentOption(userStyle)?.id) {
            CompanionUserStyle.starlightOptionId -> STARLIGHT
            else -> COSMIC_BLUE
        }
    }

    fun backgroundFor(drawMode: DrawMode): Int {
        return if (drawMode == DrawMode.AMBIENT) AMBIENT_BACKGROUND_COLOR else BACKGROUND_COLOR
    }
}
