package com.cosmobond.watchface

import android.app.NotificationManager
import android.graphics.Canvas
import android.graphics.Rect
import android.view.SurfaceHolder
import androidx.wear.watchface.WatchState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal class FakeSurfaceHolder(
    width: Int = 480,
    height: Int = 480,
) : SurfaceHolder {
    private val frame = Rect(0, 0, width, height)

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

    override fun getSurface(): android.view.Surface =
        throw UnsupportedOperationException(
            "Surface not available in tests",
        )

    override fun getSurfaceFrame(): Rect = frame
}

internal fun createWatchState(isAmbient: Boolean = false): WatchState {
    val interruption: StateFlow<Int> =
        MutableStateFlow(NotificationManager.INTERRUPTION_FILTER_ALL)
    val ambient: StateFlow<Boolean> = MutableStateFlow(isAmbient)
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
