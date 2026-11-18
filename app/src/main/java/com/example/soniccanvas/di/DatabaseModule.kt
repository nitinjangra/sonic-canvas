package com.example.soniccanvas.di

import android.content.Context
import androidx.room.Room
import com.example.soniccanvas.data.local.SonicDatabase
import com.example.soniccanvas.data.local.dao.LyricsDao
import com.example.soniccanvas.data.local.dao.PlaylistDao
import com.example.soniccanvas.data.local.dao.SongDao
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
    fun provideDatabase(@ApplicationContext context: Context): SonicDatabase {
        return Room.databaseBuilder(
            context,
            SonicDatabase::class.java,
            "sonic_canvas.db"
        ).fallbackToDestructiveMigration(true).build()
    }

    @Provides
    fun provideSongDao(database: SonicDatabase): SongDao = database.songDao()

    @Provides
    fun providePlaylistDao(database: SonicDatabase): PlaylistDao = database.playlistDao()

    @Provides
    fun provideLyricsDao(database: SonicDatabase): LyricsDao = database.lyricsDao()
}
