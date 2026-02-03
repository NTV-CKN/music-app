package com.infix.musicappv1.data.repository.album
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.model.album.AlbumList

interface AlbumRepository {
    suspend fun loadAlbums(): Result<AlbumList>
}