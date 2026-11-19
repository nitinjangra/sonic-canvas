package com.example.sonixa.service

import android.media.audiofx.Visualizer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VisualizerController @Inject constructor() {
    private var visualizer: Visualizer? = null

    fun attach(audioSessionId: Int, onWaveformReady: (ByteArray) -> Unit) {
        if (audioSessionId == 0) return
        release()
        try {
            visualizer = Visualizer(audioSessionId).apply {
                enabled = false
                captureSize = Visualizer.getCaptureSizeRange()[1]
                setDataCaptureListener(object : Visualizer.OnDataCaptureListener {
                    override fun onWaveFormDataCapture(vis: Visualizer?, waveform: ByteArray?, rate: Int) {
                        if (waveform != null) {
                            onWaveformReady(waveform)
                        }
                    }
                    override fun onFftDataCapture(vis: Visualizer?, fft: ByteArray?, rate: Int) {}
                }, Visualizer.getMaxCaptureRate() / 2, true, false)
                enabled = true
            }
        } catch (_: Exception) {}
    }

    fun release() {
        try {
            visualizer?.enabled = false
            visualizer?.release()
        } catch (_: Exception) {}
        visualizer = null
    }
}
