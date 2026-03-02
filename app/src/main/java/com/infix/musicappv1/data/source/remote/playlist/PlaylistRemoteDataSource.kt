package com.infix.musicappv1.data.source.remote.playlist

import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.source.PlaylistDataSource
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.source.remote.RetrofitHelper
import javax.inject.Inject

class PlaylistRemoteDataSource @Inject constructor() : PlaylistDataSource.Remote {
    override suspend fun loadSystemPlaylists(): Result<List<Playlist>> {
        val musicService = RetrofitHelper.musicService
        val response = musicService.loadSystemPlaylists()
        return if (response.isSuccessful) {
            if (response.body() != null)
                Result.Success(response.body()!!.playlists)
            else
                Result.Error<List<Playlist>>(Exception(response.message()))
        } else
            Result.Error<List<Playlist>>(Exception("Cannot load system playlists!"))
    }
}