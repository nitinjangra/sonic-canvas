package com.example.soniccanvas.data.repository

import com.example.soniccanvas.data.local.dao.PlaylistDao
import com.example.soniccanvas.data.local.entity.PlaylistEntity
import com.example.soniccanvas.data.local.entity.PlaylistSongCrossRef
import com.example.soniccanvas.data.local.entity.SongEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface PlaylistRepository {
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>
    suspend fun insertPlaylist(playlist: PlaylistEntity)
    suspend fun deletePlaylist(playlistId: Long)
    suspend fun addSongToPlaylist(crossRef: PlaylistSongCrossRef)
    suspend fun removeSongFromPlaylist(playlistId: Long, songId: Long)
    fun getSongsForPlaylist(playlistId: Long): Flow<List<SongEntity>>
}

class PlaylistRepositoryImpl @Inject constructor(
    private val playlistDao: PlaylistDao
) : PlaylistRepository {
    override fun getAllPlaylists(): Flow<List<PlaylistEntity>> = playlistDao.getAllPlaylists()
    override suspend fun insertPlaylist(playlist: PlaylistEntity) { playlistDao.insertPlaylist(playlist) }
    override suspend fun deletePlaylist(playlistId: Long) = playlistDao.deletePlaylist(playlistId)
    override suspend fun addSongToPlaylist(crossRef: PlaylistSongCrossRef) = playlistDao.addSongToPlaylist(crossRef)
    override suspend fun removeSongFromPlaylist(playlistId: Long, songId: Long) = playlistDao.removeSongFromPlaylist(playlistId, songId)
    override fun getSongsForPlaylist(playlistId: Long): Flow<List<SongEntity>> = playlistDao.getSongsForPlaylist(playlistId)
}
