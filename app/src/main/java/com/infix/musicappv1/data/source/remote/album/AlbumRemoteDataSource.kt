package com.infix.musicappv1.data.source.remote.album

import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.source.AlbumDataSource
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.source.remote.RetrofitHelper
import com.infix.musicappv1.data.source.remote.param.PagingParam

class AlbumRemoteDataSource : AlbumDataSource.Remote {
    override suspend fun loadAlbumsPaging(pagingParam: PagingParam): Result<List<Album>> {
        val response =
            RetrofitHelper.musicService.loadAlbumsPaging(pagingParam)
        return if (response.isSuccessful) {
            if (response.body() != null)
                Result.Success(response.body()!!.albums)
            else
                Result.Error(Exception("Body is null"))
        } else
            Result.Error(Exception("Unknown error"))
    }
}