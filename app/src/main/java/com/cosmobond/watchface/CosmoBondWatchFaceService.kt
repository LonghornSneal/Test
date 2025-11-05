package com.cosmobond.watchface

import androidx.wear.watchface.WatchFaceService

/**
 * Minimal host service for the CosmoBond Watch Face Format v2 experience.
 * The declarative layout lives in res/raw/watchface.xml; this service exists so that
 * configuration and complications can be layered on as the roadmap progresses.
 */
class CosmoBondWatchFaceService : WatchFaceService() {
    override fun onCreateEngine(): Engine = object : Engine() {}
}
