package com.infix.musicappv1.data.repository.album

import androidx.paging.PagingSource
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.source.AlbumDataSource
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.source.remote.param.PagingParam
import com.infix.musicappv1.data.source.remote.param.SearchParam
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AlbumRepositoryImpl @Inject constructor(
    private val remote: AlbumDataSource.Remote,
    private val local: AlbumDataSource.Local
) : AlbumRepository {
    override suspend fun loadAlbumsPaging(pagingParam: PagingParam): List<Album>? {
        val result = remote.loadAlbumsPaging(pagingParam)
        return if (result is Result.Success) result.data else null
    }

    override fun loadAllAlbumsFlow(): Flow<List<Album>> {
        return local.loadAllAlbumsFlow()
    }

    override fun loadAlbumsPaging(): PagingSource<Int, Album> {
        return local.loadAlbumsPaging()
    }

    override fun loadNAlbumPaging(limit: Int): PagingSource<Int, Album> {
        return local.loadNAlbumPaging(limit)
    }

    override suspend fun loadSongsByAlbumId(searchParam: SearchParam): Result<List<Song>> {
        return remote.loadSongsByAlbumId(searchParam)
    }
}