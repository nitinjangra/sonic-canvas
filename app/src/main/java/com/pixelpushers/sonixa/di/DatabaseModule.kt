package com.pixelpushers.sonixa.di

import android.content.Context
import androidx.room.Room
import com.pixelpushers.sonixa.data.local.SonixaDatabase
import com.pixelpushers.sonixa.data.local.dao.LyricsDao
import com.pixelpushers.sonixa.data.local.dao.PlaylistDao
import com.pixelpushers.sonixa.data.local.dao.SongDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SonixaDatabase {
        return Room.databaseBuilder(
            context,
            SonixaDatabase::class.java,
            "sonic_canvas.db"
        ).fallbackToDestructiveMigration(true).build()
    }

    @Provides
    fun provideSongDao(database: SonixaDatabase): SongDao = database.songDao()

    @Provides
    fun providePlaylistDao(database: SonixaDatabase): PlaylistDao = database.playlistDao()

    @Provides
    fun provideLyricsDao(database: SonixaDatabase): LyricsDao = database.lyricsDao()
}
