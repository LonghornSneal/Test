package com.cosmobond.watchface

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import androidx.test.core.app.ApplicationProvider
import androidx.wear.watchface.RenderParameters
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.style.CurrentUserStyleRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.time.ZonedDateTime

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class CosmoBondWatchFaceServiceTest {
    private val context: Context = ApplicationProvider.getApplicationContext()
    private val schema = CompanionUserStyle.createSchema(context.resources)

    @Test
    fun complicationSlotsAreRegistered() {
        val repository = CurrentUserStyleRepository(schema)
        val slotsManager = CompanionComplicationSlots.create(context, repository)

        assertEquals(2, slotsManager.complicationSlots.size)
        val expectedIds = setOf(10, 11)
        assertTrue(slotsManager.complicationSlots.keys.containsAll(expectedIds))
        assertTrue(
            slotsManager.complicationSlots.values.all { slot ->
                slot.supportedTypes.contains(ComplicationType.SHORT_TEXT)
            },
        )
    }

    @Test
    fun rendererProducesFrame() {
        val repository = CurrentUserStyleRepository(schema)
        val slotsManager = CompanionComplicationSlots.create(context, repository)
        val watchState = createWatchState()
        val renderer =
            CompanionWatchFaceRenderer(
                context,
                FakeSurfaceHolder(),
                repository,
                watchState,
                slotsManager,
            )
        renderer.renderParameters = RenderParameters.DEFAULT_INTERACTIVE

        val bitmap = Bitmap.createBitmap(480, 480, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        renderer.render(canvas, Rect(0, 0, 480, 480), ZonedDateTime.now())

        assertNotEquals(0, bitmap.getPixel(0, 0))
    }
}
