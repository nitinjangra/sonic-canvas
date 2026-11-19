package com.example.soniccanvas.service

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicPlayerManager @Inject constructor(
    @ApplicationContext private val context: Context,
    val visualizerController: VisualizerController
) {
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var controllerFuture: ListenableFuture<MediaController>? = null
    var mediaController: MediaController? = null
        private set

    private val _playbackState = MutableStateFlow(PlaybackStateModel())
    val playbackState: StateFlow<PlaybackStateModel> = _playbackState.asStateFlow()

    init {
        val sessionToken = SessionToken(context, ComponentName(context, PlaybackService::class.java))
        controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture?.addListener({
            try {
                mediaController = controllerFuture?.get()?.apply {
                    addListener(object : Player.Listener {
                        override fun onIsPlayingChanged(isPlaying: Boolean) {
                            _playbackState.value = _playbackState.value.copy(isPlaying = isPlaying)
                        }

                        override fun onPlaybackStateChanged(playbackState: Int) {
                            if (playbackState == Player.STATE_READY) {
                                _playbackState.value = _playbackState.value.copy(
                                    durationMs = duration.coerceAtLeast(0L)
                                )
                            }
                        }
                    })
                }
            } catch (_: Exception) {}
        }, MoreExecutors.directExecutor())

        // Position ticker
        scope.launch {
            while (true) {
                delay(500)
                mediaController?.let { controller ->
                    if (controller.isPlaying) {
                        _playbackState.value = _playbackState.value.copy(
                            currentPositionMs = controller.currentPosition.coerceAtLeast(0L),
                            durationMs = controller.duration.coerceAtLeast(0L)
                        )
                    }
                }
            }
        }
    }

    fun play(uri: String, title: String, artist: String, artworkUri: String?) {
        scope.launch {
            try {
                val controller = mediaController
                if (controller != null) {
                    val metadata = MediaMetadata.Builder()
                        .setTitle(title)
                        .setArtist(artist)
                        .setArtworkUri(if (!artworkUri.isNullOrEmpty()) Uri.parse(artworkUri) else null)
                        .build()

                    val mediaItem = MediaItem.Builder()
                        .setUri(uri)
                        .setMediaMetadata(metadata)
                        .build()

                    controller.setMediaItem(mediaItem)
                    controller.prepare()
                    controller.play()
                }
            } catch (_: Exception) {}
        }
    }

    fun togglePlayPause() {
        val controller = mediaController ?: return
        if (controller.isPlaying) {
            controller.pause()
        } else {
            controller.play()
        }
    }

    fun seekTo(positionMs: Long) {
        mediaController?.seekTo(positionMs)
    }

    fun release() {
        controllerFuture?.let { MediaController.releaseFuture(it) }
        mediaController = null
    }
}

data class PlaybackStateModel(
    val isPlaying: Boolean = false,
    val currentPositionMs: Long = 0L,
    val durationMs: Long = 0L
)
