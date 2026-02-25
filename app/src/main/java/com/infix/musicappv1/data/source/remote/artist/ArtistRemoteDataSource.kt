package com.infix.musicappv1.data.source.remote.artist

import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.data.source.ArtistDataSource
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.source.remote.PagingParam
import com.infix.musicappv1.data.source.remote.RetrofitHelper

class ArtistRemoteDataSource : ArtistDataSource.Remote {
    override suspend fun loadArtistsRemote(): Result<List<Artist>> {
        val response = RetrofitHelper.musicService.loadArtists()
        return if (response.isSuccessful) {
            val body = response.body()
            if (body != null)
                Result.Success(body.artists)
            else
                Result.Error(Exception("Body of response artists is null"))
        } else
            Result.Error(Exception(response.message()))
    }

    override suspend fun loadArtistsPaging(pagingParam: PagingParam): List<Artist>? {
        return RetrofitHelper.musicService.loadArtistsPaging(pagingParam).body()?.artists
    }
}