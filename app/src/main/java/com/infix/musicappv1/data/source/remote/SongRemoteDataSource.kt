package com.infix.musicappv1.data.source.remote

import com.infix.musicappv1.data.model.song.SongList
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.source.SongDataSource

class SongRemoteDataSource : SongDataSource.Remote {
    override suspend fun loadSongs(): Result<SongList> {
        val response = RetrofitHelper.musicService.loadSongs()
        return if (response.isSuccessful) {
            response.body()?.let {
                Result.Success(it)
            } ?: Result.Error(Exception("Body song list null"))
        } else {
            Result.Error(Exception("ERROR: " + response.message()))
        }
    }
}