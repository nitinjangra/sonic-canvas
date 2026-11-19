package com.pixelpushers.sonixa.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pixelpushers.sonixa.data.local.entity.LyricsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LyricsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLyrics(lyrics: LyricsEntity)

    @Query("SELECT * FROM lyrics WHERE songId = :songId")
    fun getLyricsForSong(songId: Long): Flow<LyricsEntity?>

    @Query("SELECT * FROM lyrics WHERE songId = :songId")
    suspend fun getLyricsForSongOnce(songId: Long): LyricsEntity?

    @Query("DELETE FROM lyrics WHERE songId = :songId")
    suspend fun deleteLyrics(songId: Long)
}
