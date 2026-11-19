package com.pixelpushers.sonixa.di

import com.pixelpushers.sonixa.data.repository.LyricsRepository
import com.pixelpushers.sonixa.data.repository.LyricsRepositoryImpl
import com.pixelpushers.sonixa.data.repository.PlaylistRepository
import com.pixelpushers.sonixa.data.repository.PlaylistRepositoryImpl
import com.pixelpushers.sonixa.data.repository.SongRepository
import com.pixelpushers.sonixa.data.repository.SongRepositoryImpl
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
