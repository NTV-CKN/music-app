package com.infix.musicappv1.data.repository.song

import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.model.song.SongList
import com.infix.musicappv1.data.source.Result
import kotlinx.coroutines.flow.Flow

interface SongRepository {
    suspend fun loadSongsRemote(): Result<SongList>
    suspend fun getAllSongs(): List<Song>
    suspend fun insert(vararg song: Song)
     fun getSongsFavoriteWithLimit(limit: Int = 10): Flow<List<Song>>
    fun getSongsFavoriteFlow(): Flow<List<Song>>
    fun getAllSongsFlow(): Flow<List<Song>>
}