package com.cosmobond.watchface

import android.content.Intent
import androidx.concurrent.futures.CallbackToFutureAdapter
import androidx.wear.protolayout.ActionBuilders
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.ModifiersBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.ResourceBuilders
import androidx.wear.tiles.TileBuilders
import androidx.wear.tiles.TileService
import com.google.common.util.concurrent.ListenableFuture

class CosmoBondTileService : TileService() {
    override fun onTileRequest(requestParams: RequestBuilders.TileRequest): ListenableFuture<TileBuilders.Tile> =
        CallbackToFutureAdapter.getFuture { completer ->
            val launchAction =
                ActionBuilders.LaunchAction.Builder()
                    .setAndroidActivity(
                        ActionBuilders.AndroidActivity.Builder()
                            .setPackageName(packageName)
                            .setClassName(SetWatchFaceActivity::class.java.name)
                            .build(),
                    )
                    .build()
            val clickable =
                ModifiersBuilders.Clickable.Builder()
                    .setId("open")
                    .setOnClick(launchAction)
                    .build()
            val column =
                LayoutElementBuilders.Column.Builder()
                    .addContent(
                        LayoutElementBuilders.Text.Builder()
                            .setText("CosmoBond")
                            .setModifiers(
                                ModifiersBuilders.Modifiers.Builder()
                                    .setClickable(clickable)
                                    .build(),
                            )
                            .build(),
                    )
                    .addContent(
                        LayoutElementBuilders.Text.Builder()
                            .setText("Launch setup")
                            .build(),
                    )
                    .build()

            val layout =
                LayoutElementBuilders.Layout.Builder()
                    .setRoot(column)
                    .build()

            val tile =
                TileBuilders.Tile.Builder()
                    .setResourcesVersion(RES_VERSION)
                    .setTileTimeline(
                        TimelineBuilders.Timeline.Builder()
                            .addTimelineEntry(
                                TimelineBuilders.TimelineEntry.Builder()
                                    .setLayout(layout)
                                    .build(),
                            )
                            .build(),
                    )
                    .build()
            completer.set(tile)
            "tile"
        }

    override fun onResourcesRequest(requestParams: RequestBuilders.ResourcesRequest): ListenableFuture<ResourceBuilders.Resources> =
        CallbackToFutureAdapter.getFuture { completer ->
            val resources =
                ResourceBuilders.Resources.Builder()
                    .setVersion(RES_VERSION)
                    .build()
            completer.set(resources)
            "tile-resources"
        }

    companion object {
        private const val RES_VERSION = "1"
    }
}
