package com.rudra.prayerallthetime.util

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(UnstableApi::class)
@Singleton
class QuranAudioPlayer @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var exoPlayer: ExoPlayer? = null
    
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    private val _currentAyahNumber = MutableStateFlow<Int?>(null)
    val currentAyahNumber = _currentAyahNumber.asStateFlow()

    private fun initializePlayer() {
        if (exoPlayer == null) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.AUDIO_CONTENT_TYPE_SPEECH)
                .build()

            exoPlayer = ExoPlayer.Builder(context).build().apply {
                setAudioAttributes(audioAttributes, true)
                repeatMode = Player.REPEAT_MODE_OFF
                
                addListener(object : Player.Listener {
                    override fun onIsPlayingChanged(playing: Boolean) {
                        _isPlaying.value = playing
                    }

                    override fun onPlaybackStateChanged(state: Int) {
                        when (state) {
                            Player.STATE_ENDED, Player.STATE_IDLE -> _isPlaying.value = false
                        }
                    }

                    override fun onPlayerError(error: PlaybackException) {
                        _isPlaying.value = false
                        // Optionally expose error state via StateFlow in the future
                    }
                })
            }
        }
    }

    fun playAyah(url: String, ayahNumber: Int) {
        initializePlayer()

        if (_currentAyahNumber.value == ayahNumber && exoPlayer?.isPlaying == true) {
            return
        }

        _currentAyahNumber.value = ayahNumber
        exoPlayer?.apply {
            setMediaItem(MediaItem.fromUri(url))
            prepare()
            play()
        }
    }

    fun togglePlayPause() {
        exoPlayer?.let {
            if (it.isPlaying) it.pause() else it.play()
        }
    }

    fun pause() {
        exoPlayer?.pause()
    }

    fun stop() {
        exoPlayer?.stop()
        exoPlayer?.clearMediaItems()
        _isPlaying.value = false
        _currentAyahNumber.value = null
    }

    fun release() {
        exoPlayer?.release()
        exoPlayer = null
    }
}
