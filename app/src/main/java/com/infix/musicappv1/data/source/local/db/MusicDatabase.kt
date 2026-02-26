package com.infix.musicappv1.data.source.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.data.model.artist.ArtistRemoteKeys
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.model.playlist.PlaylistSongCrossRef
import com.infix.musicappv1.data.model.recent.SongRecent
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.model.song.SongRemoteKeys
import com.infix.musicappv1.data.model.tracking.TrackingUpdate
import com.infix.musicappv1.data.source.local.album.AlbumDao
import com.infix.musicappv1.data.source.local.artist.ArtistDao
import com.infix.musicappv1.data.source.local.artist.ArtistRemoteKeysDao
import com.infix.musicappv1.data.source.local.playlist.PlaylistDao
import com.infix.musicappv1.data.source.local.playlist.PlaylistSongDao
import com.infix.musicappv1.data.source.local.recent.SongRecentDao
import com.infix.musicappv1.data.source.local.song.SongDao
import com.infix.musicappv1.data.source.local.song.SongRemoteKeysDao
import com.infix.musicappv1.data.source.local.tracking.TrackingUpdateDao

@Database(
    entities = [
        Album::class,
        Song::class,
        Playlist::class,
        SongRecent::class,
        PlaylistSongCrossRef::class,
        Artist::class,
        SongRemoteKeys::class,
        TrackingUpdate::class,
        ArtistRemoteKeys::class
    ],
    version = 4,
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

    companion object {
        @Volatile
        private var instance: MusicDatabase? = null

        fun getInstance(context: Context): MusicDatabase {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            MusicDatabase::class.java,
                            "mydb"
                        ).fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return instance!!
        }
    }
}