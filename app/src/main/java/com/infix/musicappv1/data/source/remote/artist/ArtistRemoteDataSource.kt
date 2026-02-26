package com.infix.musicappv1.data.source.remote.artist

import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.source.ArtistDataSource
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.source.remote.param.PagingParam
import com.infix.musicappv1.data.source.remote.RetrofitHelper
import com.infix.musicappv1.data.source.remote.param.SearchParam

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

    override suspend fun loadSongsByNameArtist(searchParam: SearchParam): Result<List<Song>> {
        val response =
            RetrofitHelper.musicService.getSongsOfArtist(searchParam.queryType, searchParam.query)
        return if (response.isSuccessful) {
            if (response.body() != null)
                Result.Success(response.body()!!.songs)
            else
                Result.Error(Exception("Body is null"))
        } else
            Result.Error(Exception("Unknown error"))
    }
}