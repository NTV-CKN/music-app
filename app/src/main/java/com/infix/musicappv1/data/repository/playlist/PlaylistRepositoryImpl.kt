package com.infix.musicappv1.data.repository.playlist

import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.model.playlist.PlaylistWithSongs
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.source.local.playlist.PlaylistLocalDataSource
import com.infix.musicappv1.data.source.remote.playlist.PlaylistRemoteDataSource
import kotlinx.coroutines.flow.Flow

class PlaylistRepositoryImpl(
    private val playlistLocal: PlaylistLocalDataSource,
    private val playlistRemote: PlaylistRemoteDataSource
) : PlaylistRepository {
    override suspend fun insertPlaylist(playlist: Playlist) {
        playlistLocal.insertPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistLocal.deletePlaylist(playlist)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistLocal.updatePlaylist(playlist)
    }

    override suspend fun getPlaylistWithName(name: String): Playlist? {
        return playlistLocal.getPlaylistWithName(name)
    }

    override fun getPlaylistCustomWithLimit(limit: Int): Flow<List<Playlist>> {
        return playlistLocal.getPlaylistCustomWithLimit(limit)
    }

    override fun getAllPlaylistCustoms(): Flow<List<Playlist>> {
        return playlistLocal.getAllPlaylistCustoms()
    }

    override fun getPlaylistCustomWithSong(): Flow<List<PlaylistWithSongs>?> {
        return playlistLocal.getPlaylistCustomWithSong()
    }

    override fun getLimitPlaylistCustomWithSong(limit: Int): Flow<List<PlaylistWithSongs>?> {
        return playlistLocal.getLimitPlaylistCustomWithSong(limit)
    }

    override suspend fun loadSystemPlaylists(): Result<List<Playlist>> {
        return playlistRemote.loadSystemPlaylists()
    }
}