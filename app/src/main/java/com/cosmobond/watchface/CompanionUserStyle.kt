package com.cosmobond.watchface

import android.content.res.Resources
import androidx.wear.watchface.style.UserStyle
import androidx.wear.watchface.style.UserStyleSchema
import androidx.wear.watchface.style.UserStyleSetting
import androidx.wear.watchface.style.WatchFaceLayer

internal object CompanionUserStyle {
    val paletteSettingId = UserStyleSetting.Id("palette")

    internal val cosmicBlueOptionId = UserStyleSetting.Option.Id("cosmic_blue")
    internal val starlightOptionId = UserStyleSetting.Option.Id("starlight")

    private var paletteSetting: UserStyleSetting.ListUserStyleSetting? = null

    fun createSchema(resources: Resources): UserStyleSchema {
        val palette =
            UserStyleSetting.ListUserStyleSetting(
                paletteSettingId,
                resources,
                R.string.user_style_palette,
                R.string.user_style_palette_description,
                null,
                listOf(
                    UserStyleSetting.ListUserStyleSetting.ListOption(
                        cosmicBlueOptionId,
                        resources,
                        R.string.palette_cosmic_blue,
                        null,
                    ),
                    UserStyleSetting.ListUserStyleSetting.ListOption(
                        starlightOptionId,
                        resources,
                        R.string.palette_starlight,
                        null,
                    ),
                ),
                listOf(
                    WatchFaceLayer.BASE,
                    WatchFaceLayer.COMPLICATIONS,
                    WatchFaceLayer.COMPLICATIONS_OVERLAY,
                ),
            )
        paletteSetting = palette
        return UserStyleSchema(listOf(palette))
    }

    fun accentOption(userStyle: UserStyle): UserStyleSetting.Option? {
        val setting = paletteSetting ?: return null
        return userStyle[setting]
    }
}
