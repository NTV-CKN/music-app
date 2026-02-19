package com.infix.musicappv1.utils

import android.content.Context
import com.infix.musicappv1.data.repository.PlaybackRepository
import com.infix.musicappv1.data.repository.artist.ArtistRepository
import com.infix.musicappv1.data.repository.artist.ArtistRepositoryImpl
import com.infix.musicappv1.data.repository.playlist.PlaylistRepository
import com.infix.musicappv1.data.repository.playlist.PlaylistRepositoryImpl
import com.infix.musicappv1.data.repository.song.SongRepository
import com.infix.musicappv1.data.repository.song.SongRepositoryImpl
import com.infix.musicappv1.data.repository.song_recent.SongRecentRepository
import com.infix.musicappv1.data.repository.song_recent.SongRecentRepositoryImpl
import com.infix.musicappv1.data.source.local.artist.ArtistRemoteDataSource
import com.infix.musicappv1.data.source.local.db.MusicDatabase
import com.infix.musicappv1.data.source.local.playlist.PlaylistLocalDataSource
import com.infix.musicappv1.data.source.local.recent.SongRecentLocalSource
import com.infix.musicappv1.data.source.local.song.SongLocalDataSource
import com.infix.musicappv1.data.source.remote.artist.ArtistLocalDataSource
import com.infix.musicappv1.data.source.remote.playlist.PlaylistRemoteDataSource
import com.infix.musicappv1.data.source.remote.song.SongRemoteDataSource

object InjectUtils {
    fun getPlaylistRepository(context: Context): PlaylistRepository {
        val db = MusicDatabase.getInstance(context.applicationContext)
        return PlaylistRepositoryImpl(
            PlaylistLocalDataSource(db.playlistDao(), db.playlistSongDao()),
            PlaylistRemoteDataSource()
        )
    }

    fun getSongRecentRepository(context: Context): SongRecentRepository {
        val db = MusicDatabase.getInstance(context.applicationContext)
        return SongRecentRepositoryImpl(
            SongRecentLocalSource(db.songRecentDao())
        )
    }

    fun getSongLocalDataSource(context: Context): SongLocalDataSource {
        val db = MusicDatabase.getInstance(context)
        return SongLocalDataSource(db.songDao())
    }

    fun getPlaybackRepository(context: Context): PlaybackRepository {
        val db = MusicDatabase.getInstance(context.applicationContext)
        return PlaybackRepository.getInstance(
            db
        )
    }

    fun getSongRepository(context: Context): SongRepository {
        val db = MusicDatabase.getInstance(context.applicationContext)
        return SongRepositoryImpl(
            SongRemoteDataSource(),
            SongLocalDataSource(db.songDao())
        )
    }

    fun getArtistRepository(context: Context): ArtistRepository {
        return ArtistRepositoryImpl(
            ArtistLocalDataSource(),
            ArtistRemoteDataSource()
        )
    }
}