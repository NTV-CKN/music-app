package com.infix.musicappv1.data.source.remote.song

import android.util.Log
import com.infix.musicappv1.data.model.song.SongList
import com.infix.musicappv1.data.source.SongDataSource
import com.infix.musicappv1.data.source.remote.RetrofitHelper
import com.infix.musicappv1.data.source.remote.param.PagingParam
import javax.inject.Inject

class SongRemoteDataSource @Inject constructor() : SongDataSource.Remote {
    override suspend fun loadSongs(pagingParam: PagingParam): SongList? {
        return try {
            val response = RetrofitHelper.musicService.loadSongsPaging(pagingParam)
            Log.d("SongRemoteDataSource", "response: " + response.body())
            response.body()
        } catch (_: Exception) {
            null
        }
    }
}