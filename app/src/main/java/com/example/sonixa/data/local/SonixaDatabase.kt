package com.example.sonixa.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sonixa.data.local.dao.LyricsDao
import com.example.sonixa.data.local.dao.PlaylistDao
import com.example.sonixa.data.local.dao.SongDao
import com.example.sonixa.data.local.entity.LyricsEntity
import com.example.sonixa.data.local.entity.PlaylistEntity
import com.example.sonixa.data.local.entity.PlaylistSongCrossRef
import com.example.sonixa.data.local.entity.SongEntity

@Database(
    entities = [
        SongEntity::class,
        PlaylistEntity::class,
        PlaylistSongCrossRef::class,
        LyricsEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SonixaDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun lyricsDao(): LyricsDao
}
