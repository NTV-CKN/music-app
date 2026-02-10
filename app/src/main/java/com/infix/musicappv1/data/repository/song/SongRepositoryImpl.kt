package com.infix.musicappv1.data.repository.song

import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.model.song.SongList
import com.infix.musicappv1.data.source.local.song.SongLocalDataSource
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.source.remote.SongRemoteDataSource
import kotlinx.coroutines.flow.Flow

class SongRepositoryImpl(
    private val remoteSongSrc: SongRemoteDataSource,
    private val localSongSrc: SongLocalDataSource
) : SongRepository {
    override suspend fun loadSongsRemote(): Result<SongList> {
        return remoteSongSrc.loadSongs()
    }

    override suspend fun getAllSongs(): List<Song> {
        return localSongSrc.getAllSongs()
    }


    override suspend fun insert(vararg song: Song) {
        localSongSrc.insert(*song)
    }

    override fun getAllSongsFlow(): Flow<List<Song>> {
        return localSongSrc.getAllSongsFlow()
    }
}