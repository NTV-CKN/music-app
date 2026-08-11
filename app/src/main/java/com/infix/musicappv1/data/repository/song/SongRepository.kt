package com.infix.musicappv1.data.repository.song

import androidx.paging.PagingSource
import com.infix.musicappv1.data.dto.BaseResultResponse
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.model.song.SongList
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.ui.admin.song.update_add.AddOrUpdateSongViewModel
import kotlinx.coroutines.flow.Flow

interface SongRepository {
    suspend fun loadSongsPaging(query: String, limit: Int, key: Int): SongList?
    fun getAllSongsFlow(): Flow<List<Song>>
    suspend fun insert(vararg song: Song)
    suspend fun getSongsByNameSongOrNameArtist(key: String): List<Song>
    fun getSongsFavoriteWithLimit(limit: Int = 10): Flow<List<Song>>
    fun getSongsFavoriteFlow(): Flow<List<Song>>
    fun getNSongsPaging(limit: Int = 10): PagingSource<Int, Song>
    fun getTop15SongMostHeard(): Flow<List<Song>>
    fun getTop40SongMostHeard(): Flow<List<Song>>
    fun getAllSongsPaging(): PagingSource<Int, Song>

    //admin
    suspend fun saveSong(song: Song, isUpdate: Boolean): Result<BaseResultResponse>
    suspend fun uploadSourcesSong(
        id: String,
        image: String?,
        source: String?
    ): AddOrUpdateSongViewModel.MediaUploadResult
    suspend fun removeSong(songId: String): Result<BaseResultResponse>
}