package com.infix.musicappv1.data.repository.song

import androidx.paging.PagingSource
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.model.song.SongList
import com.infix.musicappv1.data.source.remote.PagingParam
import kotlinx.coroutines.flow.Flow

interface SongRepository {
    suspend fun loadSongsPaging(pagingParam: PagingParam): SongList?
    fun getAllSongsFlow(): Flow<List<Song>>
    suspend fun insert(vararg song: Song)
    fun getSongsFavoriteWithLimit(limit: Int = 10): Flow<List<Song>>
    fun getSongsFavoriteFlow(): Flow<List<Song>>
    fun getNSongsPaging(limit: Int = 10): PagingSource<Int, Song>
    fun getTop15SongMostHeard(): Flow<List<Song>>
    fun getTop40SongMostHeard(): Flow<List<Song>>
    fun getAllSongsPaging(): PagingSource<Int, Song>
}