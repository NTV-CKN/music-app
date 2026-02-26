package com.infix.musicappv1.data.repository.album

import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.model.album.AlbumList
import com.infix.musicappv1.data.source.AlbumDataSource
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.source.local.album.AlbumLocalDataSource
import com.infix.musicappv1.data.source.remote.album.AlbumRemoteDataSource
import com.infix.musicappv1.data.source.remote.param.PagingParam

class AlbumRepositoryImpl(
    private val remote: AlbumDataSource.Remote,
    private val local: AlbumDataSource.Local
) : AlbumRepository {
    override suspend fun loadAlbumsPaging(pagingParam: PagingParam): List<Album>? {
        val result = remote.loadAlbumsPaging(pagingParam)
        return if (result is Result.Success) result.data else null
    }
}