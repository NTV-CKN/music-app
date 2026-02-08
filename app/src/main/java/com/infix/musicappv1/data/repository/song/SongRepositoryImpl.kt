package com.infix.musicappv1.data.repository.song

import com.infix.musicappv1.data.model.song.SongList
import com.infix.musicappv1.data.source.local.song.SongLocalDataSource
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.source.remote.SongRemoteDataSource

class SongRepositoryImpl(
    private val remoteSongSrc: SongRemoteDataSource,
    private val localSongSrc: SongLocalDataSource
): SongRepository {
    override suspend fun loadSongs(): Result<SongList> {
        return remoteSongSrc.loadSongs()
    }
}