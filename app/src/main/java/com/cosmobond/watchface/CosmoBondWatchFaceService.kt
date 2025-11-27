package com.cosmobond.watchface

import android.view.SurfaceHolder
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.WatchFace
import androidx.wear.watchface.WatchFaceService
import androidx.wear.watchface.WatchFaceType
import androidx.wear.watchface.style.CurrentUserStyleRepository

/**
 * Kotlin renderer host for the CosmoBond watch face.
 * The declarative WFF layout remains in res/raw/watchface.xml, but this service now wires
 * a Jetpack Watch Face renderer so complications and style schema can be exercised in tests.
 */
class CosmoBondWatchFaceService : WatchFaceService() {
    override fun createUserStyleSchema() = CompanionUserStyle.createSchema(resources)

    override fun createComplicationSlotsManager(currentUserStyleRepository: CurrentUserStyleRepository): ComplicationSlotsManager =
        CompanionComplicationSlots.create(this, currentUserStyleRepository)

    override suspend fun createWatchFace(
        surfaceHolder: SurfaceHolder,
        watchState: androidx.wear.watchface.WatchState,
        complicationSlotsManager: ComplicationSlotsManager,
        currentUserStyleRepository: CurrentUserStyleRepository,
    ): WatchFace {
        val renderer =
            CompanionWatchFaceRenderer(
                context = applicationContext,
                surfaceHolder = surfaceHolder,
                currentUserStyleRepository = currentUserStyleRepository,
                watchState = watchState,
                complicationSlotsManager = complicationSlotsManager,
            )
        return WatchFace(
            WatchFaceType.DIGITAL,
            renderer,
        )
    }
}
