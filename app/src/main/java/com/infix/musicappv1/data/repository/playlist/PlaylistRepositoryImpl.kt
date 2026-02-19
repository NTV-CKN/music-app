package com.infix.musicappv1.data.repository.playlist

import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.model.playlist.PlaylistSongCrossRef
import com.infix.musicappv1.data.model.playlist.PlaylistWithSongs
import com.infix.musicappv1.data.source.PlaylistDataSource
import com.infix.musicappv1.data.source.Result
import kotlinx.coroutines.flow.Flow

class PlaylistRepositoryImpl(
    private val local: PlaylistDataSource.Local,
    private val remote: PlaylistDataSource.Remote
) : PlaylistRepository {
    override suspend fun insertPlaylistStrict(playlist: Playlist) {
        local.insertPlaylistStrict(playlist)
    }

    override suspend fun insertPlaylist(playlist: Playlist) {
        local.insertPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        local.deletePlaylist(playlist)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        local.updatePlaylist(playlist)
    }

    override suspend fun getPlaylistWithName(name: String): Playlist? {
        return local.getPlaylistWithName(name)
    }

    override suspend fun insertPlaylistSongStrict(playlistSongCrossRef: PlaylistSongCrossRef) {
        local.insertPlaylistSongStrict(playlistSongCrossRef)
    }

    override fun getPlaylistCustomWithLimit(limit: Int): Flow<List<Playlist>> {
        return local.getPlaylistCustomWithLimit(limit)
    }

    override fun getAllPlaylistCustoms(): Flow<List<Playlist>> {
        return local.getAllPlaylistCustoms()
    }

    override fun getPlaylistCustomWithSong(): Flow<List<PlaylistWithSongs>?> {
        return local.getPlaylistCustomWithSong()
    }

    override fun getLimitPlaylistCustomWithSong(limit: Int): Flow<List<PlaylistWithSongs>?> {
        return local.getLimitPlaylistCustomWithSong(limit)
    }

    override suspend fun loadSystemPlaylists(): Result<List<Playlist>> {
        return remote.loadSystemPlaylists()
    }
}