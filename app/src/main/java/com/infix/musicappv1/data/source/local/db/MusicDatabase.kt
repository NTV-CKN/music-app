package com.infix.musicappv1.data.source.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.model.album.AlbumRemoteKeys
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.data.model.artist.ArtistRemoteKeys
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.model.playlist.PlaylistSongCrossRef
import com.infix.musicappv1.data.model.recent.SongRecent
import com.infix.musicappv1.data.model.search.RecentSearchSong
import com.infix.musicappv1.data.model.search.SearchKeySong
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.model.song.SongRemoteKeys
import com.infix.musicappv1.data.model.tracking.TrackingUpdate
import com.infix.musicappv1.data.model.user.User
import com.infix.musicappv1.data.source.local.album.AlbumDao
import com.infix.musicappv1.data.source.local.album.AlbumRemoteKeysDao
import com.infix.musicappv1.data.source.local.artist.ArtistDao
import com.infix.musicappv1.data.source.local.artist.ArtistRemoteKeysDao
import com.infix.musicappv1.data.source.local.playlist.PlaylistDao
import com.infix.musicappv1.data.source.local.playlist.PlaylistSongDao
import com.infix.musicappv1.data.source.local.recent.SongRecentDao
import com.infix.musicappv1.data.source.local.search.song.RecentSearchSongDao
import com.infix.musicappv1.data.source.local.search.song.SearchKeySongDao
import com.infix.musicappv1.data.source.local.song.SongDao
import com.infix.musicappv1.data.source.local.song.SongRemoteKeysDao
import com.infix.musicappv1.data.source.local.tracking.TrackingUpdateDao
import com.infix.musicappv1.data.source.local.user.UserDAO

@Database(
    entities = [
        Album::class,
        User::class,
        Song::class,
        Playlist::class,
        SongRecent::class,
        PlaylistSongCrossRef::class,
        Artist::class,
        SongRemoteKeys::class,
        TrackingUpdate::class,
        ArtistRemoteKeys::class,
        AlbumRemoteKeys::class,
        SearchKeySong::class,
        RecentSearchSong::class
    ],
    version = 10,
    // autoMigrations = [AutoMigration(from = 3, to = 4)]
)
@TypeConverters(value = [DateConverter::class])
abstract class MusicDatabase : RoomDatabase() {
    abstract fun songRecentDao(): SongRecentDao
    abstract fun songDao(): SongDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun albumDao(): AlbumDao
    abstract fun playlistSongDao(): PlaylistSongDao
    abstract fun artistDao(): ArtistDao
    abstract fun songRemoteKeysDao(): SongRemoteKeysDao
    abstract fun trackingUpdateDao(): TrackingUpdateDao
    abstract fun artistRemoteKeysDao(): ArtistRemoteKeysDao
    abstract fun albumRemoteKeysDao(): AlbumRemoteKeysDao
    abstract fun recentSearchSongDao(): RecentSearchSongDao
    abstract fun searchKeySongDao(): SearchKeySongDao
    abstract fun userDao(): UserDAO
}