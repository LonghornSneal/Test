package com.cosmobond.watchface

import android.view.SurfaceHolder
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.WatchFace
import androidx.wear.watchface.WatchFaceService
import androidx.wear.watchface.WatchFaceType
import androidx.wear.watchface.style.CurrentUserStyleRepository

/** Kotlin renderer host for the CosmoBond watch face. */
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
