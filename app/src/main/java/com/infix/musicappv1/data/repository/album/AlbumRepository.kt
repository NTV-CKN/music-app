package com.infix.musicappv1.data.repository.album
import androidx.paging.PagingSource
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.model.album.AlbumList
import com.infix.musicappv1.data.source.remote.param.PagingParam
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {
    suspend fun loadAlbumsPaging(pagingParam: PagingParam): List<Album>?
    fun loadAllAlbumsFlow(): Flow<List<Album>>
    fun loadAlbumsPaging(): PagingSource<Int, Album>
    fun loadNAlbumPaging(limit: Int = 9): PagingSource<Int, Album>
}