package com.infix.musicappv1.data.source.remote.album

import com.infix.musicappv1.data.model.album.AlbumList
import com.infix.musicappv1.data.source.AlbumDataSource
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.source.remote.RetrofitHelper

class AlbumRemoteDataSource : AlbumDataSource.Remote {
    override suspend fun loadAlbumsRemote(): Result<AlbumList> {
        val response = RetrofitHelper.musicService.loadAlbums()
        return if (response.isSuccessful) {
            response.body()?.let { body ->
                Result.Success(body)
            } ?: Result.Error(Exception("Body null"))
        } else {
            Result.Error(Exception(response.message()))
        }
    }
}