package com.infix.musicappv1.data.source.remote.playlist

import com.infix.musicappv1.data.source.PlaylistDataSource
import javax.inject.Inject

class PlaylistRemoteDataSource @Inject constructor() : PlaylistDataSource.Remote {
//    override suspend fun loadSystemPlaylists(): Result<List<Playlist>> {
//        return try {
//            val musicService = RetrofitHelper.musicService
//            val response = musicService.loadSystemPlaylists()
//            if (response.isSuccessful) {
//                if (response.body() != null)
//                    Result.Success(response.body()!!.playlists)
//                else
//                    Result.Error<List<Playlist>>(Exception(response.message()))
//            } else
//                Result.Error<List<Playlist>>(Exception("Cannot load system playlists!"))
//        } catch (ex: Exception) {
//            Result.Error<List<Playlist>>(Exception(ex.message ?: "Cannot load system playlists!"))
//        }
//    }
}