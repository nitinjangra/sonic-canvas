package com.example.soniccanvas.di

import com.example.soniccanvas.data.repository.LyricsRepository
import com.example.soniccanvas.data.repository.LyricsRepositoryImpl
import com.example.soniccanvas.data.repository.PlaylistRepository
import com.example.soniccanvas.data.repository.PlaylistRepositoryImpl
import com.example.soniccanvas.data.repository.SongRepository
import com.example.soniccanvas.data.repository.SongRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSongRepository(impl: SongRepositoryImpl): SongRepository

    @Binds
    @Singleton
    abstract fun bindPlaylistRepository(impl: PlaylistRepositoryImpl): PlaylistRepository

    @Binds
    @Singleton
    abstract fun bindLyricsRepository(impl: LyricsRepositoryImpl): LyricsRepository
}
