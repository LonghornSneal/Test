package com.cosmobond.watchface

import android.content.Context
import android.media.AudioManager
import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.media.session.PlaybackState
import android.os.Handler
import android.os.Looper

private const val METADATA_KEY_BPM = "android.media.metadata.BEATS_PER_MINUTE"
private const val EXTRA_BPM = "android.media.extra.BPM"

internal data class PlaybackSnapshot(val isPlaying: Boolean, val bpm: Int?)

internal class MediaSessionPlaybackMonitor(
    context: Context,
    private val tempoSink: MutablePlaybackTempoSource,
    private val onPlaybackUpdate: (PlaybackSnapshot) -> Unit,
    private val clock: GameClock = SystemGameClock(),
) {
    private val sessionManager: MediaSessionManager? =
        context.getSystemService(MediaSessionManager::class.java)
    private val audioManager: AudioManager? = context.getSystemService(AudioManager::class.java)
    private val handler = Handler(Looper.getMainLooper())
    private var controller: MediaController? = null
    private var lastPositionUpdateTime: Long? = null
    private var lastPositionMs: Long? = null

    private val sessionListener =
        MediaSessionManager.OnActiveSessionsChangedListener { controllers ->
            attachController(controllers?.firstOrNull())
        }

    private val controllerCallback =
        object : MediaController.Callback() {
            override fun onPlaybackStateChanged(state: PlaybackState?) {
                updateFromState(state, controller?.metadata)
            }

            override fun onMetadataChanged(metadata: MediaMetadata?) {
                updateFromState(controller?.playbackState, metadata)
            }
        }

    fun start() {
        try {
            sessionManager?.addOnActiveSessionsChangedListener(sessionListener, null, handler)
            attachController(sessionManager?.getActiveSessions(null)?.firstOrNull())
        } catch (se: SecurityException) {
            fallbackToAudioOnly()
        } catch (iae: IllegalArgumentException) {
            fallbackToAudioOnly()
        }
    }

    fun stop() {
        try {
            sessionManager?.removeOnActiveSessionsChangedListener(sessionListener)
        } catch (_: SecurityException) {
            // Ignore; listener was never registered because access was denied.
        }
        detachController()
    }

    private fun attachController(newController: MediaController?) {
        if (controller === newController) {
            updateFromState(controller?.playbackState, controller?.metadata)
            return
        }
        controller?.unregisterCallback(controllerCallback)
        controller = newController
        resetInference()
        newController?.registerCallback(controllerCallback, handler)
        updateFromState(newController?.playbackState, newController?.metadata)
    }

    private fun detachController() {
        controller?.unregisterCallback(controllerCallback)
        controller = null
    }

    private fun updateFromState(
        state: PlaybackState?,
        metadata: MediaMetadata?,
    ) {
        val playing =
            state?.state == PlaybackState.STATE_PLAYING ||
                state?.state == PlaybackState.STATE_BUFFERING
        val bpmFromMetadata =
            metadata?.getLong(METADATA_KEY_BPM)?.toInt()?.takeIf { it > 0 }
        val bpmFromExtras =
            state?.extras?.getInt(EXTRA_BPM)?.takeIf { it > 0 }
        val inferred = inferTempoFromPosition(state)
        val bpm = bpmFromMetadata ?: bpmFromExtras ?: inferred

        tempoSink.currentBpm = bpm
        tempoSink.playing = playing
        onPlaybackUpdate(PlaybackSnapshot(playing, bpm))

        if (!playing) resetInference()
    }

    private fun inferTempoFromPosition(state: PlaybackState?): Int? {
        if (state == null) return null
        val updateTime = if (state.lastPositionUpdateTime > 0) state.lastPositionUpdateTime else clock.now()
        val lastUpdate = lastPositionUpdateTime
        val position = state.position
        val lastPos = lastPositionMs
        lastPositionUpdateTime = updateTime
        lastPositionMs = position

        if (lastUpdate != null && lastPos != null && updateTime > lastUpdate && position > lastPos) {
            val deltaTime = updateTime - lastUpdate
            if (deltaTime in 350..1_500) {
                val bpm = (60_000f / deltaTime.toFloat()).toInt()
                return bpm.coerceIn(60, 180)
            }
        }
        return null
    }

    private fun fallbackToAudioOnly() {
        val playing = audioManager?.isMusicActive == true
        tempoSink.currentBpm = null
        tempoSink.playing = playing
        onPlaybackUpdate(PlaybackSnapshot(playing, null))
    }

    private fun resetInference() {
        lastPositionMs = null
        lastPositionUpdateTime = null
    }
}
