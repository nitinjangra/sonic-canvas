package com.example.sonixa.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lyrics")
data class LyricsEntity(
    @PrimaryKey val songId: Long,
    val syncedLyrics: String?,
    val plainLyrics: String?,
    val isSynced: Boolean
)
