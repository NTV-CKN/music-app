package com.infix.musicappv1.data.repository.song

import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.model.song.SongList
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.source.SongDataSource
import com.infix.musicappv1.data.source.remote.PagingParam
import kotlinx.coroutines.flow.Flow

class SongRepositoryImpl(
    private val remote: SongDataSource.Remote,
    private val local: SongDataSource.Local
) : SongRepository {
    override suspend fun loadSongsPaging(pagingParam: PagingParam): SongList? {
        return remote.loadSongs(pagingParam)
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

    override fun getTop15SongMostHeard(): Flow<List<Song>> {
        return local.getTop15SongMostHeard()
    }

    override fun getTop40SongMostHeard(): Flow<List<Song>> {
        return local.getTop40SongMostHeard()
    }

    override fun getAllSongsFlow(): Flow<List<Song>> {
        return local.getAllSongsFlow()
    }
}