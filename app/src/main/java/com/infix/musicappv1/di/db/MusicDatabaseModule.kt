package com.infix.musicappv1.di.db

import android.content.Context
import androidx.room.Room
import com.infix.musicappv1.data.source.local.album.AlbumDao
import com.infix.musicappv1.data.source.local.album.AlbumRemoteKeysDao
import com.infix.musicappv1.data.source.local.artist.ArtistDao
import com.infix.musicappv1.data.source.local.artist.ArtistRemoteKeysDao
import com.infix.musicappv1.data.source.local.db.MusicDatabase
import com.infix.musicappv1.data.source.local.playlist.PlaylistDao
import com.infix.musicappv1.data.source.local.playlist.PlaylistSongDao
import com.infix.musicappv1.data.source.local.recent.SongRecentDao
import com.infix.musicappv1.data.source.local.search.song.RecentSearchSongDao
import com.infix.musicappv1.data.source.local.search.song.SearchKeySongDao
import com.infix.musicappv1.data.source.local.song.SongDao
import com.infix.musicappv1.data.source.local.song.SongRemoteKeysDao
import com.infix.musicappv1.data.source.local.tracking.TrackingUpdateDao
import com.infix.musicappv1.data.source.local.user.UserDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
object MusicDatabaseModule {
    //cached object music database and double check - safe thread
    @Provides
    @Singleton
    fun getMusicDatabase(@ApplicationContext context: Context): MusicDatabase {
        return Room.databaseBuilder(
            context,
            MusicDatabase::class.java,
            "mydb"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideSongRecentDao(db: MusicDatabase): SongRecentDao = db.songRecentDao()

    @Provides
    fun provideUserDao(db: MusicDatabase): UserDAO = db.userDao()

    @Provides
    fun provideSongDao(db: MusicDatabase): SongDao = db.songDao()

    @Provides
    fun providePlaylistDao(db: MusicDatabase): PlaylistDao = db.playlistDao()

    @Provides
    fun provideAlbumDao(db: MusicDatabase): AlbumDao = db.albumDao()

    @Provides
    fun providePlaylistSongDao(db: MusicDatabase): PlaylistSongDao = db.playlistSongDao()

    @Provides
    fun provideArtistDao(db: MusicDatabase): ArtistDao = db.artistDao()

    @Provides
    fun provideSongRemoteKeysDao(db: MusicDatabase): SongRemoteKeysDao = db.songRemoteKeysDao()

    @Provides
    fun provideTrackingUpdateDao(db: MusicDatabase): TrackingUpdateDao = db.trackingUpdateDao()

    @Provides
    fun provideArtistRemoteKeysDao(db: MusicDatabase): ArtistRemoteKeysDao =
        db.artistRemoteKeysDao()

    @Provides
    fun provideAlbumRemoteKeysDao(db: MusicDatabase): AlbumRemoteKeysDao = db.albumRemoteKeysDao()

    @Provides
    fun provideRecentSearchSongDao(db: MusicDatabase): RecentSearchSongDao =
        db.recentSearchSongDao()

    @Provides
    fun provideSearchKeySongDao(db: MusicDatabase): SearchKeySongDao = db.searchKeySongDao()
}