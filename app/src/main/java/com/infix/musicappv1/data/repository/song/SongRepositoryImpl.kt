package com.infix.musicappv1.data.repository.song

import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.model.song.SongList
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.source.SongDataSource
import kotlinx.coroutines.flow.Flow

class SongRepositoryImpl(
    private val remote: SongDataSource.Remote,
    private val local: SongDataSource.Local
) : SongRepository {
    override suspend fun loadSongsRemote(): Result<SongList> {
        return remote.loadSongs()
    }

    override suspend fun getAllSongs(): List<Song> {
        return local.getAllSongs()
    }


    override suspend fun insert(vararg song: Song) {
        local.insert(*song)
    }

    override fun getSongsFavoriteWithLimit(limit: Int): Flow<List<Song>> {
        return local.getSongsFavoriteWithLimit(limit)
    }

    override fun getSongsFavoriteFlow(): Flow<List<Song>> {
        return local.getAllSongsFlow()
    }

    override fun getAllSongsFlow(): Flow<List<Song>> {
        return local.getAllSongsFlow()
    }
}