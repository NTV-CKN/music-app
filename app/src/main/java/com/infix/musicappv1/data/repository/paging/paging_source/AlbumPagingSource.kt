package com.infix.musicappv1.data.repository.paging.paging_source

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.repository.album.AlbumRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class AlbumPagingSource @AssistedInject constructor(
    private val albumRepository: AlbumRepository,
    @Assisted
    private val query: String,
) : PagingSource<Int, Album>() {

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(query: String): AlbumPagingSource
    }

    override fun getRefreshKey(state: PagingState<Int, Album>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Album> {
        try {
            val key = params.key ?: 0
            val limit = params.loadSize

            val albumList = albumRepository.loadAlbumsPaging(query, limit, key)?.albums ?: emptyList()

            Log.d("AlbumPagingSource", "Load: $albumList")

            return LoadResult.Page(
                albumList,
                prevKey = if (key == 0) null else key.minus(1),
                nextKey = if (albumList.isEmpty() || albumList.size < params.loadSize) null else key.plus(1)
            )
        } catch (ex: Exception) {
            return LoadResult.Error(ex)
        }
    }
}