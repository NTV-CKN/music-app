package com.infix.musicappv1.data.repository.song

import androidx.paging.PagingSource
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.model.song.SongList
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.source.SongDataSource
import com.infix.musicappv1.data.source.remote.param.PagingParam
import kotlinx.coroutines.flow.Flow

class SongRepositoryImpl(
    private val remote: SongDataSource.Remote,
    private val local: SongDataSource.Local
) : SongRepository {
    override suspend fun loadSongsPaging(pagingParam: PagingParam): SongList? {
        return remote.loadSongs(pagingParam)
    }

    override fun getAllSongsFlow(): Flow<List<Song>> {
        return local.getAllSongsFlow()
    }


    override suspend fun insert(vararg song: Song) {
        local.insert(*song)
    }

    override fun getSongsFavoriteWithLimit(limit: Int): Flow<List<Song>> {
        return local.getSongsFavoriteWithLimit(limit)
    }

    override fun getSongsFavoriteFlow(): Flow<List<Song>> {
        return local.getSongsFavorite()
    }

    override fun getNSongsPaging(limit: Int): PagingSource<Int, Song> {
        return local.getNSongsPaging(limit)
    }

    override fun getTop15SongMostHeard(): Flow<List<Song>> {
        return local.getTop15SongMostHeard()
    }

    override fun getTop40SongMostHeard(): Flow<List<Song>> {
        return local.getTop40SongMostHeard()
    }

    override fun getAllSongsPaging(): PagingSource<Int, Song> {
        return local.getAllSongsPaging()
    }

}