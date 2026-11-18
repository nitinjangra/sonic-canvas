package com.example.soniccanvas.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.soniccanvas.data.repository.NotificationCategory
import com.example.soniccanvas.data.repository.NotificationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_settings")

enum class AppTheme {
    SYSTEM, LIGHT, DARK, BLACK
}

enum class SongClickBehavior {
    OPEN_NOW_PLAYING,
    PLAY_IN_BACKGROUND
}

data class UserSettings(
    val theme: AppTheme = AppTheme.SYSTEM,
    val matchAlbumColors: Boolean = true,
    val gaplessPlayback: Boolean = true,
    val crossfadeSeconds: Int = 0,
    val highResOutput: Boolean = true,
    val showFormatBadges: Boolean = true,
    val excludeHiddenFolders: Boolean = true,
    val songClickBehavior: SongClickBehavior = SongClickBehavior.OPEN_NOW_PLAYING,
    val skippedFolderPaths: Set<String> = emptySet(),
    val defaultThumbnailIndex: Int = 0,
    val customThemeColor: Long? = null,
    val resumePlaybackOnStart: Boolean = false,
    val pauseOnAudioDisconnect: Boolean = true,
    val lastPlayedSongId: Long = -1L,
    val lastPlayedPositionMs: Long = 0L,
    val minTrackLengthSec: Int = 30,
    val totalListeningMs: Long = 0L,
    val notifiedHours: Int = 0,
    val filterShortTracks: Boolean = true
)

@Singleton
class UserSettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val THEME = stringPreferencesKey("theme")
        val MATCH_ALBUM_COLORS = booleanPreferencesKey("match_album_colors")
        val GAPLESS_PLAYBACK = booleanPreferencesKey("gapless_playback")
        val CROSSFADE_SECONDS = intPreferencesKey("crossfade_seconds")
        val HIGH_RES_OUTPUT = booleanPreferencesKey("high_res_output")
        val SHOW_FORMAT_BADGES = booleanPreferencesKey("show_format_badges")
        val EXCLUDE_HIDDEN_FOLDERS = booleanPreferencesKey("exclude_hidden_folders")
        val SONG_CLICK_BEHAVIOR = stringPreferencesKey("song_click_behavior")
        val SKIPPED_FOLDER_PATHS = stringSetPreferencesKey("skipped_folder_paths")
        val DEFAULT_THUMBNAIL_INDEX = intPreferencesKey("default_thumbnail_index")
        val CUSTOM_THEME_COLOR = longPreferencesKey("custom_theme_color")
        val RESUME_PLAYBACK_ON_START = booleanPreferencesKey("resume_playback_on_start")
        val PAUSE_ON_AUDIO_DISCONNECT = booleanPreferencesKey("pause_on_audio_disconnect")
        val LAST_PLAYED_SONG_ID = longPreferencesKey("last_played_song_id")
        val LAST_PLAYED_POSITION_MS = longPreferencesKey("last_played_position_ms")
        val MIN_TRACK_LENGTH_SEC = intPreferencesKey("min_track_length_sec")
        val TOTAL_LISTENING_MS = longPreferencesKey("total_listening_ms")
        val NOTIFIED_HOURS = intPreferencesKey("notified_hours")
        val FILTER_SHORT_TRACKS = booleanPreferencesKey("filter_short_tracks")
    }

    val userSettingsFlow: Flow<UserSettings> = context.dataStore.data.map { prefs ->
        UserSettings(
            theme = try {
                AppTheme.valueOf(prefs[Keys.THEME] ?: AppTheme.SYSTEM.name)
            } catch (_: Exception) {
                AppTheme.SYSTEM
            },
            matchAlbumColors = prefs[Keys.MATCH_ALBUM_COLORS] ?: true,
            gaplessPlayback = prefs[Keys.GAPLESS_PLAYBACK] ?: true,
            crossfadeSeconds = prefs[Keys.CROSSFADE_SECONDS] ?: 0,
            highResOutput = prefs[Keys.HIGH_RES_OUTPUT] ?: true,
            showFormatBadges = prefs[Keys.SHOW_FORMAT_BADGES] ?: true,
            excludeHiddenFolders = prefs[Keys.EXCLUDE_HIDDEN_FOLDERS] ?: true,
            songClickBehavior = try {
                SongClickBehavior.valueOf(prefs[Keys.SONG_CLICK_BEHAVIOR] ?: SongClickBehavior.OPEN_NOW_PLAYING.name)
            } catch (_: Exception) {
                SongClickBehavior.OPEN_NOW_PLAYING
            },
            skippedFolderPaths = prefs[Keys.SKIPPED_FOLDER_PATHS] ?: emptySet(),
            defaultThumbnailIndex = prefs[Keys.DEFAULT_THUMBNAIL_INDEX] ?: 0,
            customThemeColor = prefs[Keys.CUSTOM_THEME_COLOR].let { if (it == 0L) null else it },
            resumePlaybackOnStart = prefs[Keys.RESUME_PLAYBACK_ON_START] ?: false,
            pauseOnAudioDisconnect = prefs[Keys.PAUSE_ON_AUDIO_DISCONNECT] ?: true,
            lastPlayedSongId = prefs[Keys.LAST_PLAYED_SONG_ID] ?: -1L,
            lastPlayedPositionMs = prefs[Keys.LAST_PLAYED_POSITION_MS] ?: 0L,
            minTrackLengthSec = prefs[Keys.MIN_TRACK_LENGTH_SEC] ?: 30,
            totalListeningMs = prefs[Keys.TOTAL_LISTENING_MS] ?: 0L,
            notifiedHours = prefs[Keys.NOTIFIED_HOURS] ?: 0,
            filterShortTracks = prefs[Keys.FILTER_SHORT_TRACKS] ?: true
        )
    }

    suspend fun setFilterShortTracks(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.FILTER_SHORT_TRACKS] = enabled
        }
    }

    suspend fun setTheme(theme: AppTheme) {
        context.dataStore.edit { prefs ->
            prefs[Keys.THEME] = theme.name
        }
    }

    suspend fun setMatchAlbumColors(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.MATCH_ALBUM_COLORS] = enabled
        }
    }

    suspend fun setGaplessPlayback(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.GAPLESS_PLAYBACK] = enabled
        }
    }

    suspend fun setCrossfadeSeconds(seconds: Int) {
        context.dataStore.edit { prefs ->
            prefs[Keys.CROSSFADE_SECONDS] = seconds
        }
    }

    suspend fun setHighResOutput(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.HIGH_RES_OUTPUT] = enabled
        }
    }

    suspend fun setShowFormatBadges(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.SHOW_FORMAT_BADGES] = enabled
        }
    }

    suspend fun setExcludeHiddenFolders(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.EXCLUDE_HIDDEN_FOLDERS] = enabled
        }
    }

    suspend fun setSongClickBehavior(behavior: SongClickBehavior) {
        context.dataStore.edit { prefs ->
            prefs[Keys.SONG_CLICK_BEHAVIOR] = behavior.name
        }
    }

    suspend fun addSkippedFolder(path: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.SKIPPED_FOLDER_PATHS] ?: emptySet()
            prefs[Keys.SKIPPED_FOLDER_PATHS] = current + path
        }
    }

    suspend fun removeSkippedFolder(path: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.SKIPPED_FOLDER_PATHS] ?: emptySet()
            prefs[Keys.SKIPPED_FOLDER_PATHS] = current - path
        }
    }

    suspend fun setDefaultThumbnailIndex(index: Int) {
        context.dataStore.edit { prefs ->
            prefs[Keys.DEFAULT_THUMBNAIL_INDEX] = index
        }
    }

    suspend fun setCustomThemeColor(colorLong: Long?) {
        context.dataStore.edit { prefs ->
            if (colorLong != null) {
                prefs[Keys.CUSTOM_THEME_COLOR] = colorLong
            } else {
                prefs.remove(Keys.CUSTOM_THEME_COLOR)
            }
        }
    }

    suspend fun setResumePlaybackOnStart(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.RESUME_PLAYBACK_ON_START] = enabled
        }
    }

    suspend fun setPauseOnAudioDisconnect(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.PAUSE_ON_AUDIO_DISCONNECT] = enabled
        }
    }

    suspend fun saveLastPlayedState(songId: Long, positionMs: Long) {
        context.dataStore.edit { prefs ->
            prefs[Keys.LAST_PLAYED_SONG_ID] = songId
            prefs[Keys.LAST_PLAYED_POSITION_MS] = positionMs
        }
    }

    suspend fun recordListeningTime(ms: Long, notificationRepository: NotificationRepository) {
        context.dataStore.edit { prefs ->
            val currentMs = prefs[Keys.TOTAL_LISTENING_MS] ?: 0L
            val newMs = currentMs + ms
            prefs[Keys.TOTAL_LISTENING_MS] = newMs

            val currentHours = (newMs / (1000 * 60 * 60)).toInt()
            val notified = prefs[Keys.NOTIFIED_HOURS] ?: 0

            if (currentHours > notified) {
                prefs[Keys.NOTIFIED_HOURS] = currentHours
                val title = when (currentHours) {
                    1 -> "🎵 1 Hour of Pure Audio!"
                    2 -> "🎧 2 Hours Strong!"
                    3 -> "🌟 3-Hour Musical Journey!"
                    5 -> "🔥 5 Hours of Sonic Bliss!"
                    10 -> "🏆 10-Hour Audiophile Milestone!"
                    else -> "🎶 $currentHours Hours of Music!"
                }
                val message = when (currentHours) {
                    1 -> "You've spent 1 hour listening to high-res music in SonicCanvas. Keep the groove going!"
                    2 -> "Two hours of musical immersion! Your ears have exceptional taste."
                    3 -> "Three hours! SonicCanvas is your companion for the long haul."
                    else -> "Amazing dedication! You have utilized SonicCanvas for $currentHours hours total."
                }
                notificationRepository.addNotification(
                    title = title,
                    message = message,
                    category = NotificationCategory.FUN_FACTS
                )
            }
        }
    }
}
