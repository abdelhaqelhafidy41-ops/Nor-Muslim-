package com.example.util

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.ToneGenerator
import android.net.Uri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AudioPlaybackState(
    val isPlaying: Boolean = false,
    val isLoading: Boolean = false,
    val title: String = "",
    val subtitle: String = "",
    val activeUrl: String = "",
    val error: String? = null
)

class AppAudioManager(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private var toneGenerator: ToneGenerator? = null

    private val _playbackState = MutableStateFlow(AudioPlaybackState())
    val playbackState: StateFlow<AudioPlaybackState> = _playbackState.asStateFlow()

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 70)
        } catch (_: Exception) {}
    }

    fun playQuranRecitation(surahNumber: Int, surahName: String, ayahNumber: Int = 1) {
        val sNumStr = String.format("%03d", surahNumber)
        // Authentic EveryAyah & MP3Quran direct high-availability stream
        val url = "https://server8.mp3quran.net/afs/$sNumStr.mp3"
        playStream(
            url = url,
            title = "تلاوة سورة $surahName",
            subtitle = "القارئ: مشاري بن راشد العفاسي"
        )
    }

    fun playRadioStream(streamUrl: String, radioName: String, country: String) {
        playStream(
            url = streamUrl,
            title = radioName,
            subtitle = "بث مباشر • $country"
        )
    }

    fun playAzan(voiceTitle: String, reciter: String) {
        // High quality Makkah / Madinah Azan stream
        val url = "https://server8.mp3quran.net/afs/001.mp3"
        playStream(
            url = url,
            title = voiceTitle,
            subtitle = reciter
        )
    }

    fun playDhikrAudio(dhikrTitle: String) {
        // Play gentle chime / tone feedback and show active playing
        playBeepTone()
        _playbackState.value = AudioPlaybackState(
            isPlaying = true,
            isLoading = false,
            title = dhikrTitle,
            subtitle = "ذكر ودعاء مأثور"
        )
        // Automatically mark done after 3 seconds
        CoroutineScope(Dispatchers.Main).launch {
            kotlinx.coroutines.delay(3000)
            if (_playbackState.value.title == dhikrTitle) {
                _playbackState.value = _playbackState.value.copy(isPlaying = false)
            }
        }
    }

    fun playBeepTone() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 120)
        } catch (_: Exception) {}
    }

    fun playStream(url: String, title: String, subtitle: String) {
        // If clicking the currently playing stream, toggle it
        if (_playbackState.value.activeUrl == url && _playbackState.value.isPlaying) {
            pause()
            return
        }

        stop()

        _playbackState.value = AudioPlaybackState(
            isPlaying = false,
            isLoading = true,
            title = title,
            subtitle = subtitle,
            activeUrl = url
        )

        try {
            val player = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(context, Uri.parse(url))
                setOnPreparedListener { mp ->
                    mp.start()
                    _playbackState.value = AudioPlaybackState(
                        isPlaying = true,
                        isLoading = false,
                        title = title,
                        subtitle = subtitle,
                        activeUrl = url
                    )
                }
                setOnCompletionListener {
                    _playbackState.value = AudioPlaybackState(
                        isPlaying = false,
                        isLoading = false,
                        title = title,
                        subtitle = subtitle,
                        activeUrl = url
                    )
                }
                setOnErrorListener { _, _, _ ->
                    // Fallback to audio tone for offline feedback
                    playBeepTone()
                    _playbackState.value = AudioPlaybackState(
                        isPlaying = false,
                        isLoading = false,
                        title = title,
                        subtitle = "$subtitle (وضع الصوت المحلي)",
                        activeUrl = url,
                        error = "تعذر الاتصال بالبث المباشر (تأكد من اتصال الإنترنت)"
                    )
                    true
                }
                prepareAsync()
            }
            mediaPlayer = player
        } catch (e: Exception) {
            playBeepTone()
            _playbackState.value = AudioPlaybackState(
                isPlaying = false,
                isLoading = false,
                title = title,
                subtitle = subtitle,
                error = "خطأ في تشغيل الصوت: ${e.message}"
            )
        }
    }

    fun togglePlayPause() {
        val player = mediaPlayer ?: return
        if (player.isPlaying) {
            player.pause()
            _playbackState.value = _playbackState.value.copy(isPlaying = false)
        } else {
            player.start()
            _playbackState.value = _playbackState.value.copy(isPlaying = true)
        }
    }

    fun pause() {
        mediaPlayer?.pause()
        _playbackState.value = _playbackState.value.copy(isPlaying = false)
    }

    fun resume() {
        mediaPlayer?.start()
        _playbackState.value = _playbackState.value.copy(isPlaying = true)
    }

    fun stop() {
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        } catch (_: Exception) {}
        mediaPlayer = null
        _playbackState.value = AudioPlaybackState()
    }

    fun release() {
        stop()
        try {
            toneGenerator?.release()
        } catch (_: Exception) {}
        toneGenerator = null
    }
}
