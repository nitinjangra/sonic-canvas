package com.example.soniccanvas.data.repository

import com.example.soniccanvas.data.local.dao.SongDao
import com.example.soniccanvas.data.local.entity.SongEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface SongRepository {
    fun getAllSongs(): Flow<List<SongEntity>>
    fun getFavoriteSongs(): Flow<List<SongEntity>>
    fun getRecentlyPlayedSongs(): Flow<List<SongEntity>>
    fun getMostPlayedSongs(): Flow<List<SongEntity>>
    suspend fun setFavorite(songId: Long, isFavorite: Boolean)
    suspend fun deleteSongsByIds(songIds: List<Long>)
    suspend fun deleteSong(song: SongEntity)
    suspend fun insertSongs(songs: List<SongEntity>)
}

class SongRepositoryImpl @Inject constructor(
    private val songDao: SongDao
) : SongRepository {
    override fun getAllSongs(): Flow<List<SongEntity>> = songDao.getAllSongs()
    override fun getFavoriteSongs(): Flow<List<SongEntity>> = songDao.getFavoriteSongs()
    override fun getRecentlyPlayedSongs(): Flow<List<SongEntity>> = songDao.getRecentlyPlayedSongs()
    override fun getMostPlayedSongs(): Flow<List<SongEntity>> = songDao.getMostPlayedSongs()
    override suspend fun setFavorite(songId: Long, isFavorite: Boolean) = songDao.setFavorite(songId, isFavorite)
    override suspend fun deleteSongsByIds(songIds: List<Long>) = songDao.deleteSongsByIds(songIds)
    override suspend fun deleteSong(song: SongEntity) = songDao.deleteSongById(song.id)
    override suspend fun insertSongs(songs: List<SongEntity>) = songDao.insertSongs(songs)
}
