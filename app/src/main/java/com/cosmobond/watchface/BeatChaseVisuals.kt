package com.cosmobond.watchface

import android.graphics.RectF
import kotlin.math.max
import kotlin.math.min

internal enum class BeatBunnyAnimation {
    AmbientBounce,
    AmbientBounceReduced,
    SlideSwitch,
    VoltagePop,
    VoltagePopAction,
    SilencePause,
    ClubCarousel,
    FadingStage,
    VinylMemorial,
}

internal object BeatBunnyVisuals {
    private const val streakActionThreshold = 5

    private val assetMap =
        mapOf(
            BeatBunnyAnimation.AmbientBounce to "BeatBunny-Idle-action.mp4",
            BeatBunnyAnimation.AmbientBounceReduced to "BeatBunny-Idle.mp4",
            BeatBunnyAnimation.SlideSwitch to "BeatBunny-TempoChange.mp4",
            BeatBunnyAnimation.VoltagePop to "BeatBunny-VoltagePop.mp4",
            BeatBunnyAnimation.VoltagePopAction to "BeatBunny-VoltagePop-action.mp4",
            BeatBunnyAnimation.SilencePause to "BeatBunny-Idle.mp4",
            BeatBunnyAnimation.ClubCarousel to "BeatBunny-VoltagePop-action.mp4",
            BeatBunnyAnimation.FadingStage to "assets/video/pets/beatbunny/idle/beatbunny_idle_1080p24.mp4",
            BeatBunnyAnimation.VinylMemorial to "assets/video/pets/beatbunny/idle/beatbunny_idle_1080p24.mp4",
        )

    fun animationForCue(
        cue: BeatChaseCue,
        streak: Int,
        reducedMotion: Boolean,
    ): BeatBunnyAnimation {
        return when (cue) {
            BeatChaseCue.Countdown -> BeatBunnyAnimation.SlideSwitch
            BeatChaseCue.Hit -> {
                if (!reducedMotion && streak >= streakActionThreshold) {
                    BeatBunnyAnimation.VoltagePopAction
                } else {
                    BeatBunnyAnimation.VoltagePop
                }
            }
            BeatChaseCue.Miss,
            BeatChaseCue.Pause,
            -> BeatBunnyAnimation.SilencePause
            BeatChaseCue.Win -> BeatBunnyAnimation.ClubCarousel
            BeatChaseCue.Lose -> BeatBunnyAnimation.SilencePause
            BeatChaseCue.Idle -> {
                if (reducedMotion) BeatBunnyAnimation.AmbientBounceReduced else BeatBunnyAnimation.AmbientBounce
            }
        }
    }

    fun assetFor(animation: BeatBunnyAnimation): String =
        assetMap[animation] ?: "BeatBunny-Idle.mp4"
}

internal data class TapTargetLayout(
    val centerXFraction: Float,
    val centerYFraction: Float,
    val radiusFraction: Float,
    val strokeFraction: Float,
) {
    fun bounds(): RectF =
        RectF(
            centerXFraction - radiusFraction,
            centerYFraction - radiusFraction,
            centerXFraction + radiusFraction,
            centerYFraction + radiusFraction,
        )
}

internal data class StreakArcLayout(
    val centerXFraction: Float,
    val centerYFraction: Float,
    val radiusFraction: Float,
    val thicknessFraction: Float,
) {
    fun bounds(): RectF {
        val outer = radiusFraction + thicknessFraction
        return RectF(
            centerXFraction - outer,
            centerYFraction - outer,
            centerXFraction + outer,
            centerYFraction + outer,
        )
    }
}

internal data class MissDotsLayout(
    val centerXFraction: Float,
    val centerYFraction: Float,
    val radiusFraction: Float,
    val spacingFraction: Float,
    val maxDots: Int = 3,
) {
    fun bounds(): List<RectF> {
        val clampedDots = max(0, min(maxDots, 3))
        val startX =
            centerXFraction - spacingFraction * (clampedDots - 1) / 2f
        return (0 until clampedDots).map { index ->
            val x = startX + index * spacingFraction
            RectF(
                x - radiusFraction,
                centerYFraction - radiusFraction,
                x + radiusFraction,
                centerYFraction + radiusFraction,
            )
        }
    }
}

internal data class BeatChaseOverlayLayout(
    val tapTarget: TapTargetLayout,
    val streakArc: StreakArcLayout,
    val missDots: MissDotsLayout,
)

internal object BeatChaseOverlayDefaults {
    val layout =
        BeatChaseOverlayLayout(
            tapTarget =
                TapTargetLayout(
                    centerXFraction = 0.5f,
                    centerYFraction = 0.58f,
                    radiusFraction = 0.10f,
                    strokeFraction = 0.018f,
                ),
            streakArc =
                StreakArcLayout(
                    centerXFraction = 0.5f,
                    centerYFraction = 0.56f,
                    radiusFraction = 0.11f,
                    thicknessFraction = 0.012f,
                ),
            missDots =
                MissDotsLayout(
                    centerXFraction = 0.5f,
                    centerYFraction = 0.66f,
                    radiusFraction = 0.012f,
                    spacingFraction = 0.06f,
                ),
        )

    fun isClearOfComplications(
        complicationBounds: Collection<RectF>,
        overlay: BeatChaseOverlayLayout = layout,
    ): Boolean {
        val ring = overlay.tapTarget.bounds()
        val arc = overlay.streakArc.bounds()
        val missDots = overlay.missDots.bounds()
        return complicationBounds.none { comp ->
            RectF.intersects(comp, ring) ||
                RectF.intersects(comp, arc) ||
                missDots.any { dot -> RectF.intersects(comp, dot) }
        }
    }
}
