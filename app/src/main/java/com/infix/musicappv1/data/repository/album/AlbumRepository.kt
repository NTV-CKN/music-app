package com.infix.musicappv1.data.repository.album
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.model.album.AlbumList
import com.infix.musicappv1.data.source.remote.param.PagingParam

interface AlbumRepository {
    suspend fun loadAlbumsPaging(pagingParam: PagingParam): List<Album>?
}