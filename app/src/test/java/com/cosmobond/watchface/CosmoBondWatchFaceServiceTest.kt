package com.cosmobond.watchface

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.SurfaceTexture
import android.view.Surface
import android.view.SurfaceHolder
import androidx.test.core.app.ApplicationProvider
import androidx.wear.watchface.RenderParameters
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.style.CurrentUserStyleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

private class FakeSurfaceHolder : SurfaceHolder {
    private val surface = Surface(SurfaceTexture(0))
    private val frame = Rect(0, 0, 480, 480)

    override fun addCallback(callback: SurfaceHolder.Callback?) = Unit

    override fun removeCallback(callback: SurfaceHolder.Callback?) = Unit

    override fun isCreating(): Boolean = false

    override fun setType(type: Int) = Unit

    override fun setFixedSize(
        width: Int,
        height: Int,
    ) {
        frame.right = width
        frame.bottom = height
    }

    override fun setSizeFromLayout() = Unit

    override fun setFormat(format: Int) = Unit

    override fun setKeepScreenOn(keepScreenOn: Boolean) = Unit

    override fun lockCanvas(): Canvas = Canvas()

    override fun lockCanvas(dirty: Rect?): Canvas = Canvas()

    override fun unlockCanvasAndPost(canvas: Canvas?) = Unit

    override fun getSurface(): Surface = surface

    override fun getSurfaceFrame(): Rect = frame
}

private fun createWatchState(): WatchState {
    val interruption: StateFlow<Int> = MutableStateFlow(android.app.NotificationManager.INTERRUPTION_FILTER_ALL)
    val ambient: StateFlow<Boolean> = MutableStateFlow(false)
    val battery: StateFlow<Boolean> = MutableStateFlow(false)
    val visible: StateFlow<Boolean> = MutableStateFlow(true)
    val instanceId: StateFlow<String> = MutableStateFlow("renderer-test")
    val locked: StateFlow<Boolean> = MutableStateFlow(false)
    val ctor =
        WatchState::class.java.getConstructor(
            StateFlow::class.java,
            StateFlow::class.java,
            StateFlow::class.java,
            StateFlow::class.java,
            Boolean::class.javaPrimitiveType,
            Boolean::class.javaPrimitiveType,
            Long::class.javaPrimitiveType,
            Long::class.javaPrimitiveType,
            Int::class.javaPrimitiveType,
            Boolean::class.javaPrimitiveType,
            StateFlow::class.java,
            StateFlow::class.java,
        )
    return ctor.newInstance(
        interruption,
        ambient,
        battery,
        visible,
        false,
        false,
        0L,
        0L,
        0,
        false,
        instanceId,
        locked,
    )
}
