package com.pixelpushers.sonixa.ui.settings

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pixelpushers.sonixa.data.preferences.AppTheme
import com.pixelpushers.sonixa.data.preferences.SongClickBehavior
import com.pixelpushers.sonixa.data.preferences.UserSettings
import com.pixelpushers.sonixa.data.preferences.UserSettingsRepository
import com.pixelpushers.sonixa.data.scanner.MediaStoreScanner
import com.pixelpushers.sonixa.util.BackupManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: UserSettingsRepository,
    private val scanner: MediaStoreScanner,
    private val backupManager: BackupManager
) : ViewModel() {

    val settings: StateFlow<UserSettings> = settingsRepository.userSettingsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserSettings())

    fun setTheme(theme: AppTheme) {
        viewModelScope.launch { settingsRepository.setTheme(theme) }
    }

    fun setHasScanned(hasScanned: Boolean) {
        viewModelScope.launch { settingsRepository.setHasScanned(hasScanned) }
    }

    fun setVibrationEnabled(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setVibrationEnabled(enabled) }
    }

    fun setPauseOnZeroVolume(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setPauseOnZeroVolume(enabled) }
    }

    fun setMaterialDesignEnabled(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setMaterialDesignEnabled(enabled) }
    }

    fun setMaterialYouEnabled(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setMaterialYouEnabled(enabled) }
    }

    fun togglePinFolder(folderPath: String) {
        viewModelScope.launch { settingsRepository.togglePinFolder(folderPath) }
    }

    fun togglePinArtist(artistName: String) {
        viewModelScope.launch { settingsRepository.togglePinArtist(artistName) }
    }

    fun togglePinAlbum(albumName: String) {
        viewModelScope.launch { settingsRepository.togglePinAlbum(albumName) }
    }

    fun setLibraryViewType(type: com.pixelpushers.sonixa.ui.library.LibraryViewType) {
        viewModelScope.launch { settingsRepository.setLibraryViewType(type.name) }
    }

    fun setPlaylistViewType(type: com.pixelpushers.sonixa.ui.library.LibraryViewType) {
        viewModelScope.launch { settingsRepository.setPlaylistViewType(type.name) }
    }

    fun exportBackup(uri: android.net.Uri) {
        viewModelScope.launch { backupManager.exportBackup(uri) }
    }

    fun restoreBackup(uri: android.net.Uri) {
        viewModelScope.launch { backupManager.restoreBackup(uri) }
    }

    fun setMatchAlbumColors(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setMatchAlbumColors(enabled) }
    }

    fun setGaplessPlayback(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setGaplessPlayback(enabled) }
    }

    fun setCrossfadeSeconds(seconds: Int) {
        viewModelScope.launch { settingsRepository.setCrossfadeSeconds(seconds) }
    }

    fun setHighResOutput(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setHighResOutput(enabled) }
    }

    fun setShowFormatBadges(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setShowFormatBadges(enabled) }
    }

    fun setFilterShortTracks(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setFilterShortTracks(enabled)
            rescanLibrary()
        }
    }

    fun setExcludeHiddenFolders(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setExcludeHiddenFolders(enabled)
            rescanLibrary()
        }
    }

    fun setSongClickBehavior(behavior: SongClickBehavior) {
        viewModelScope.launch {
            settingsRepository.setSongClickBehavior(behavior)
        }
    }

    fun setDefaultThumbnailIndex(index: Int) {
        viewModelScope.launch {
            settingsRepository.setDefaultThumbnailIndex(index)
        }
    }

    fun setResumePlaybackOnStart(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setResumePlaybackOnStart(enabled)
        }
    }

    fun setPauseOnAudioDisconnect(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setPauseOnAudioDisconnect(enabled)
        }
    }

    fun generateRandomVectorTheme() {
        viewModelScope.launch {
            val random = Random(System.currentTimeMillis())
            val color = Color(
                red = random.nextFloat() * 0.7f + 0.3f,
                green = random.nextFloat() * 0.7f + 0.3f,
                blue = random.nextFloat() * 0.7f + 0.3f,
                alpha = 1f
            )
            settingsRepository.setCustomThemeColor(color.value.toLong())
            settingsRepository.setMatchAlbumColors(false)
        }
    }

    fun addIncludedFolder(path: String) {
        viewModelScope.launch {
            settingsRepository.addIncludedFolder(path)
            rescanLibrary()
        }
    }

    fun removeIncludedFolder(path: String) {
        viewModelScope.launch {
            settingsRepository.removeIncludedFolder(path)
            rescanLibrary()
        }
    }

    fun addSkippedFolder(path: String) {
        viewModelScope.launch {
            settingsRepository.addSkippedFolder(path)
            rescanLibrary()
        }
    }

    fun removeSkippedFolder(path: String) {
        viewModelScope.launch {
            settingsRepository.removeSkippedFolder(path)
            rescanLibrary()
        }
    }

    fun rescanLibrary() {
        viewModelScope.launch {
            val s = settings.value
            scanner.scanMusic(
                minDurationMs = if (s.filterShortTracks) 30000L else 0L,
                excludeHiddenFolders = s.excludeHiddenFolders,
                skippedFolderPaths = s.skippedFolderPaths,
                includedFolderPaths = s.includedFolderPaths
            )
        }
    }
}
