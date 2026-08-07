package com.infix.musicappv1.data.source.remote.song

import android.util.Log
import com.infix.musicappv1.data.model.song.SongList
import com.infix.musicappv1.data.source.SongDataSource
import com.infix.musicappv1.data.source.remote.MusicService
import javax.inject.Inject

class SongRemoteDataSource @Inject constructor(
    private val musicService: MusicService
) : SongDataSource.Remote {
    override suspend fun loadSongs(query: String, limit: Int, key: Int): SongList? {
        return try {
            val response = musicService.loadSongsPaging(query, limit, key)
            Log.d("SongRemoteDataSource", "response: " + response.body())
            response.body()
        } catch (_: Exception) {
            null
        }
    }
}