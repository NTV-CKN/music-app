package com.infix.musicappv1.data.repository.paging.paging_source

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.song.SongRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class SongPagingSource @AssistedInject constructor(
    private val songRepository: SongRepository,
    @Assisted
    private val query: String,
) : PagingSource<Int, Song>() {

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(query: String): SongPagingSource
    }

    override fun getRefreshKey(state: PagingState<Int, Song>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Song> {
        try {
            val key = params.key ?: 0
            val limit = params.loadSize

            val songList = songRepository.loadSongsPaging(query, limit, key)?.songs ?: emptyList()

            Log.d("SongPagingSource", "Load: $songList")

            return LoadResult.Page(
                songList,
                prevKey = if (key == 0) null else key.minus(1),
                nextKey = if (songList.isEmpty() || songList.size < params.loadSize) null else key.plus(
                    1
                )
            )
        } catch (ex: Exception) {
            return LoadResult.Error(
                ex
            )
        }
    }
}