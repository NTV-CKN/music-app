package com.infix.musicappv1.data.source.remote.artist

import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.data.source.ArtistDataSource
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.source.remote.RetrofitHelper

class ArtistRemoteDataSource : ArtistDataSource.Remote {
    override suspend fun loadArtistsRemote(): com.infix.musicappv1.data.source.Result<List<Artist>> {
        val response = RetrofitHelper.musicService.loadArtists()
        return if (response.isSuccessful) {
            val body = response.body()
            if (body != null)
                com.infix.musicappv1.data.source.Result.Success(body.artists)
            else
                com.infix.musicappv1.data.source.Result.Error(Exception("Body of response artists is null"))
        } else
            Result.Error(Exception(response.message()))
    }
}