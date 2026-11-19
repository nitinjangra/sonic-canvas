package com.example.sonixa.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class SongEntity(
    @PrimaryKey val id: Long,
    val mediaStoreId: Long,
    val title: String,
    val artist: String,
    val album: String,
    val albumId: Long,
    val duration: Long,
    val path: String,
    val uri: String,
    val artworkUri: String?,
    val mimeType: String,
    val isFlac: Boolean,
    val sampleRate: Int = 44100,
    val bitDepth: Int = 16,
    val bitRate: Int = 0,
    val isFavorite: Boolean = false,
    val playCount: Int = 0,
    val lastPlayed: Long = 0L,
    val folderPath: String,
    val year: Int = 0
)
