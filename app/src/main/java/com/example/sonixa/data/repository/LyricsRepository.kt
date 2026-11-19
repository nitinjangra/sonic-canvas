package com.example.sonixa.data.repository

import com.example.sonixa.data.local.dao.LyricsDao
import com.example.sonixa.data.local.entity.LyricsEntity
import javax.inject.Inject

interface LyricsRepository {
    suspend fun getLyricsForSongOnce(songId: Long): LyricsEntity?
    suspend fun insertLyrics(lyrics: LyricsEntity)
}

class LyricsRepositoryImpl @Inject constructor(
    private val lyricsDao: LyricsDao
) : LyricsRepository {
    override suspend fun getLyricsForSongOnce(songId: Long): LyricsEntity? = lyricsDao.getLyricsForSongOnce(songId)
    override suspend fun insertLyrics(lyrics: LyricsEntity) = lyricsDao.insertLyrics(lyrics)
}
