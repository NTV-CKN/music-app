package com.infix.musicappv1.data.source.local.playlist

import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.model.playlist.PlaylistSong
import com.infix.musicappv1.data.model.playlist.PlaylistWithSongs
import com.infix.musicappv1.data.source.PlaylistDataSource
import kotlinx.coroutines.flow.Flow

class PlaylistLocalDataSource(
    private val playlistDao: PlaylistDao,
    private val playlistSongDao: PlaylistSongDao
) : PlaylistDataSource.Local {
    override suspend fun insertPlaylist(playlist: Playlist) {
        playlistDao.insert(playlist)
    }

    override suspend fun insertPlaylistStrict(playlist: Playlist) {
        playlistDao.insertStrict(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistDao.delete(playlist)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistDao.update(playlist)
    }

    override suspend fun getPlaylistWithName(name: String): Playlist? {
        return playlistDao.getPlaylistWithName(name)
    }

    override suspend fun insertPlaylistSongStrict(playlistSong: PlaylistSong) {
        playlistSongDao.insertStrict(playlistSong)
    }

    override fun getPlaylistCustomWithLimit(limit: Int): Flow<List<Playlist>> {
        return playlistDao.getPlaylistCustomWithLimit(limit)
    }

    override fun getAllPlaylistCustoms(): Flow<List<Playlist>> {
        return playlistDao.getAllPlaylistCustom()
    }

    override fun getPlaylistCustomWithSong(): Flow<List<PlaylistWithSongs>?> {
        return playlistDao.getPlaylistCustomWithSong()
    }

    override fun getLimitPlaylistCustomWithSong(limit: Int): Flow<List<PlaylistWithSongs>?> {
        return playlistDao.getLimitPlaylistCustomWithSong(limit)
    }
}