package com.infix.musicappv1.data.source.remote.album

import com.infix.musicappv1.data.model.album.AlbumList
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.source.AlbumDataSource
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.source.remote.MusicService
import com.infix.musicappv1.data.source.remote.RetrofitHelper
import com.infix.musicappv1.data.source.remote.param.SearchParam
import javax.inject.Inject

class AlbumRemoteDataSource @Inject constructor(
    private val musicService: MusicService
) : AlbumDataSource.Remote {
    override suspend fun loadAlbumsPaging(
        query: String,
        limit: Int,
        key: Int
    ): AlbumList? {
        val result = musicService.loadAlbumsPaging(query, limit, limit)
        return if (result.isSuccessful) {
            result.body() ?: AlbumList()
        } else
            AlbumList()
    }


    override suspend fun loadSongsByAlbumId(searchParam: SearchParam): Result<List<Song>> {
        return try {
            val response =
                RetrofitHelper.musicService.getSongsByAlbumId(
                    searchParam.queryType,
                    searchParam.query.toInt()
                )
            if (response.isSuccessful)
                if (response.body() != null)
                    Result.Success(response.body()!!.songs)
                else
                    Result.Error(Exception("Body is null"))
            else
                Result.Error(Exception("Unknown error"))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}