package com.infix.musicappv1.data.repository.paging.paging_source

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.data.repository.artist.ArtistRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class ArtistPagingSource @AssistedInject constructor(
    private val artistRepository: ArtistRepository,
    @Assisted
    private val query: String,
) : PagingSource<Int, Artist>() {

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(query: String): ArtistPagingSource
    }

    override fun getRefreshKey(state: PagingState<Int, Artist>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Artist> {
        try {
            val key = params.key ?: 0
            val limit = params.loadSize

            val artistList = artistRepository.loadArtistsPaging(query, limit, key)?.artists ?: emptyList()

            Log.d("ArtistPagingSource", "Load: $artistList")

            return LoadResult.Page(
                artistList,
                prevKey = if (key == 0) null else key.minus(1),
                nextKey = if (artistList.isEmpty() || artistList.size < params.loadSize) null else key.plus(1)
            )
        } catch (ex: Exception) {
            return LoadResult.Error(ex)
        }
    }
}