package com.infix.musicappv1.data.source

import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.model.playlist.PlaylistWithSongs
import kotlinx.coroutines.flow.Flow

interface PlaylistDataSource {
    interface Local {
        suspend fun insertPlaylist(playlist: Playlist)
        suspend fun deletePlaylist(playlist: Playlist)
        suspend fun updatePlaylist(playlist: Playlist)
        fun getPlaylistCustomWithLimit(limit: Int = 10): Flow<List<Playlist>>
        fun getAllPlaylistCustoms(): Flow<List<Playlist>>
        fun getPlaylistCustomWithSong(): Flow<List<PlaylistWithSongs>?>
        fun getLimitPlaylistCustomWithSong(limit: Int = 10): Flow<List<PlaylistWithSongs>?>
    }
    interface Remote{}
}