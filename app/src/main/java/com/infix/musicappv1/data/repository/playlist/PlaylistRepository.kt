package com.infix.musicappv1.data.repository.playlist

import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.model.playlist.PlaylistSongCrossRef
import com.infix.musicappv1.data.model.playlist.PlaylistWithSongs
import com.infix.musicappv1.data.source.Result
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun insertPlaylistStrict(playlist: Playlist)
    suspend fun insertPlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun getPlaylistWithName(name: String): Playlist?
    suspend fun insertPlaylistSongStrict(playlistSongCrossRef: PlaylistSongCrossRef)
    fun getPlaylistCustomWithLimit(limit: Int = 10): Flow<List<Playlist>>
    fun getAllPlaylistCustoms(): Flow<List<Playlist>>
    fun getPlaylistCustomWithSong(): Flow<List<PlaylistWithSongs>?>
    fun getLimitPlaylistCustomWithSong(limit: Int = 10): Flow<List<PlaylistWithSongs>?>

    suspend fun loadSystemPlaylists(): Result<List<Playlist>>
}