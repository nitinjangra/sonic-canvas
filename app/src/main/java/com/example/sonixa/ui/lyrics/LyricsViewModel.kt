package com.example.sonixa.ui.lyrics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sonixa.data.local.dao.LyricsDao
import com.example.sonixa.data.local.entity.LyricsEntity
import com.example.sonixa.domain.model.LrcParser
import com.example.sonixa.domain.model.LyricsLine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LyricsUiState(
    val songId: Long = -1L,
    val lyricsLines: List<LyricsLine> = emptyList(),
    val plainText: String = "",
    val isSynced: Boolean = false,
    val isLoading: Boolean = false
)

@HiltViewModel
class LyricsViewModel @Inject constructor(
    private val lyricsDao: LyricsDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(LyricsUiState())
    val uiState: StateFlow<LyricsUiState> = _uiState.asStateFlow()

    fun loadLyricsForSong(songId: Long, embeddedLrc: String? = null) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, songId = songId)

            val cached = lyricsDao.getLyricsForSongOnce(songId)
            if (cached != null) {
                val lines = if (cached.isSynced && cached.syncedLyrics != null) {
                    LrcParser.parse(cached.syncedLyrics)
                } else emptyList()

                _uiState.value = LyricsUiState(
                    songId = songId,
                    lyricsLines = lines,
                    plainText = cached.plainLyrics ?: "",
                    isSynced = cached.isSynced,
                    isLoading = false
                )
            } else if (!embeddedLrc.isNullOrEmpty()) {
                val lines = LrcParser.parse(embeddedLrc.orEmpty())
                val isSynced = lines.isNotEmpty()
                _uiState.value = LyricsUiState(
                    songId = songId,
                    lyricsLines = lines,
                    plainText = if (!isSynced) embeddedLrc else "",
                    isSynced = isSynced,
                    isLoading = false
                )
            } else {
                _uiState.value = LyricsUiState(songId = songId, isLoading = false)
            }
        }
    }

    private fun String?.isNull_or_empty(): Boolean = this == null || this.isEmpty()

    fun saveLyrics(songId: Long, text: String) {
        viewModelScope.launch {
            val lines = LrcParser.parse(text)
            val isSynced = lines.isNotEmpty()

            val entity = LyricsEntity(
                songId = songId,
                syncedLyrics = if (isSynced) text else null,
                plainLyrics = if (!isSynced) text else null,
                isSynced = isSynced
            )

            lyricsDao.insertLyrics(entity)

            _uiState.value = LyricsUiState(
                songId = songId,
                lyricsLines = lines,
                plainText = if (!isSynced) text else "",
                isSynced = isSynced,
                isLoading = false
            )
        }
    }
}
