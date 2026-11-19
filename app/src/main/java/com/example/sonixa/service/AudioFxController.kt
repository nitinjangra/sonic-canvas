package com.example.sonixa.service

import android.media.audiofx.BassBoost
import android.media.audiofx.Equalizer
import android.media.audiofx.Virtualizer
import javax.inject.Inject
import javax.inject.Singleton

data class EqualizerState(
    val isEnabled: Boolean = false,
    val numberOfBands: Int = 5,
    val bandFrequenciesHz: List<Int> = listOf(60, 230, 910, 3600, 14000),
    val bandLevels: List<Int> = listOf(0, 0, 0, 0, 0), // Levels in dB (-12 to +12)
    val presets: List<String> = listOf("Flat", "Rock", "Pop", "Jazz", "Bass"),
    val currentPreset: Int = 0,
    val bassBoostStrength: Int = 0, // 0 to 100
    val virtualizerStrength: Int = 0 // 0 to 100
)

@Singleton
class AudioFxController @Inject constructor() {

    private var equalizer: Equalizer? = null
    private var bassBoost: BassBoost? = null
    private var virtualizer: Virtualizer? = null

    private var currentAudioSessionId: Int = 0

    fun bindAudioSession(audioSessionId: Int) {
        if (audioSessionId == 0 || audioSessionId == currentAudioSessionId) return
        release()

        try {
            currentAudioSessionId = audioSessionId
            equalizer = Equalizer(0, audioSessionId)
            bassBoost = BassBoost(0, audioSessionId)
            virtualizer = Virtualizer(0, audioSessionId)
        } catch (_: Exception) {
            // Some devices restricted or audioFx unavailable
        }
    }

    fun setEnabled(enabled: Boolean) {
        try {
            equalizer?.enabled = enabled
            bassBoost?.enabled = enabled
            virtualizer?.enabled = enabled
        } catch (_: Exception) {}
    }

    fun setBandLevel(band: Int, levelDb: Int) {
        try {
            val millibels = (levelDb * 100).coerceIn(-1200, 1200).toShort()
            equalizer?.setBandLevel(band.toShort(), millibels)
        } catch (_: Exception) {}
    }

    fun setPreset(presetIndex: Int) {
        try {
            if (presetIndex in 0 until (equalizer?.numberOfPresets ?: 0)) {
                equalizer?.usePreset(presetIndex.toShort())
            }
        } catch (_: Exception) {}
    }

    fun setBassBoost(strengthPercent: Int) {
        try {
            bassBoost?.let {
                val strength = ((strengthPercent / 100f) * 1000).toInt().coerceIn(0, 1000).toShort()
                it.setStrength(strength)
                it.enabled = strengthPercent > 0
            }
        } catch (_: Exception) {}
    }

    fun setVirtualizer(strengthPercent: Int) {
        try {
            virtualizer?.let {
                val strength = ((strengthPercent / 100f) * 1000).toInt().coerceIn(0, 1000).toShort()
                it.setStrength(strength)
                it.enabled = strengthPercent > 0
            }
        } catch (_: Exception) {}
    }

    fun release() {
        try { equalizer?.release() } catch (_: Exception) {}
        try { bassBoost?.release() } catch (_: Exception) {}
        try { virtualizer?.release() } catch (_: Exception) {}
        equalizer = null
        bassBoost = null
        virtualizer = null
        currentAudioSessionId = 0
    }
}
