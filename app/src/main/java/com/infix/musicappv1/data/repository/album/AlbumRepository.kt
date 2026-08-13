package com.infix.musicappv1.data.repository.album

import androidx.paging.PagingSource
import com.infix.musicappv1.data.dto.BaseResultResponse
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.model.album.AlbumList
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.source.Result
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {
    suspend fun loadAlbumsPaging(query: String, limit: Int, key: Int): AlbumList?
    suspend fun loadAllAlbums(): List<Album>
    fun loadAllAlbumsFlow(): Flow<List<Album>>
    fun loadAlbumsPaging(): PagingSource<Int, Album>
    fun loadNAlbumPaging(limit: Int = 9): PagingSource<Int, Album>
    suspend fun loadSongsByAlbumId(albumId: String): Result<List<Song>>

    //admin
    suspend fun saveAlbum(album: Album, isUpdate: Boolean): Result<BaseResultResponse>
}