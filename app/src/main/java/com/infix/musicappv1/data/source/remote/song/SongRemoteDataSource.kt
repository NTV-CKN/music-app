package com.infix.musicappv1.data.source.remote.song

import android.util.Log
import com.infix.musicappv1.data.model.song.SongList
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.source.SongDataSource
import com.infix.musicappv1.data.source.remote.PagingParam
import com.infix.musicappv1.data.source.remote.RetrofitHelper

class SongRemoteDataSource : SongDataSource.Remote {
    override suspend fun loadSongs(pagingParam: PagingParam): SongList? {
        val response = RetrofitHelper.musicService.loadSongsPaging(pagingParam)
        Log.d("SongRemoteDataSource", "response: " + response.body())
        return response.body()
    }
}