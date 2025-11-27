package com.cosmobond.watchface

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * Minimal host service for the CosmoBond Watch Face Format v2 experience.
 * The declarative layout lives in res/raw/watchface.xml; this service exists so that
 * configuration and complications can be layered on as the roadmap progresses.
 */
class CosmoBondWatchFaceService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null
}
