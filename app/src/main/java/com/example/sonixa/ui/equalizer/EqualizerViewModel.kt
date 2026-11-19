package com.example.sonixa.ui.equalizer

import androidx.lifecycle.ViewModel
import com.example.sonixa.service.AudioFxController
import com.example.sonixa.service.EqualizerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class EqualizerViewModel @Inject constructor(
    private val audioFxController: AudioFxController
) : ViewModel() {

    private val _state = MutableStateFlow(EqualizerState())
    val state: StateFlow<EqualizerState> = _state.asStateFlow()

    fun toggleEnabled(enabled: Boolean) {
        _state.value = _state.value.copy(isEnabled = enabled)
        audioFxController.setEnabled(enabled)
    }

    fun setBandLevel(bandIndex: Int, levelDb: Int) {
        val updatedLevels = _state.value.bandLevels.toMutableList()
        if (bandIndex in updatedLevels.indices) {
            updatedLevels[bandIndex] = levelDb
            _state.value = _state.value.copy(bandLevels = updatedLevels)
            audioFxController.setBandLevel(bandIndex, levelDb)
        }
    }

    fun selectPreset(presetIndex: Int) {
        _state.value = _state.value.copy(currentPreset = presetIndex)
        audioFxController.setPreset(presetIndex)
    }

    fun setBassBoost(percent: Int) {
        _state.value = _state.value.copy(bassBoostStrength = percent)
        audioFxController.setBassBoost(percent)
    }

    fun setVirtualizer(percent: Int) {
        _state.value = _state.value.copy(virtualizerStrength = percent)
        audioFxController.setVirtualizer(percent)
    }
}
