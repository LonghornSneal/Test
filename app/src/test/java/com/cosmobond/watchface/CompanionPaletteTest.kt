package com.cosmobond.watchface

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.wear.watchface.DrawMode
import androidx.wear.watchface.style.CurrentUserStyleRepository
import androidx.wear.watchface.style.UserStyle
import androidx.wear.watchface.style.UserStyleSetting
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CompanionPaletteTest {
    private val context: Context = ApplicationProvider.getApplicationContext()
    private val schema = CompanionUserStyle.createSchema(context.resources)

    @Test
    fun usesCosmicBlueByDefault() {
        val repository = CurrentUserStyleRepository(schema)
        val color = CompanionPalette.accentFor(repository.userStyle.value, DrawMode.INTERACTIVE)
        assertEquals(0xFFA6B6FF.toInt(), color)
    }

    @Test
    fun usesStarlightWhenSelected() {
        val repository = CurrentUserStyleRepository(schema)
        val paletteSetting = schema.userStyleSettings.filterIsInstance<UserStyleSetting.ListUserStyleSetting>().first()
        repository.updateUserStyle(
            UserStyle(
                mapOf(
                    paletteSetting to paletteSetting.options.first { it.id == CompanionUserStyle.starlightOptionId },
                ),
            ),
        )
        val color = CompanionPalette.accentFor(repository.userStyle.value, DrawMode.INTERACTIVE)
        assertEquals(0xFFF5E5B9.toInt(), color)
    }

    @Test
    fun usesAmbientAccentInAmbientMode() {
        val repository = CurrentUserStyleRepository(schema)
        val color = CompanionPalette.accentFor(repository.userStyle.value, DrawMode.AMBIENT)
        assertEquals(0xFFB0B0B0.toInt(), color)
    }
}
