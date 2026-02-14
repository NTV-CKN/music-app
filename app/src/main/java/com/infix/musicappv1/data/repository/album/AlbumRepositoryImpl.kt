package com.infix.musicappv1.data.repository.album

import com.infix.musicappv1.data.model.album.AlbumList
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.source.local.album.AlbumLocalDataSource
import com.infix.musicappv1.data.source.remote.album.AlbumRemoteDataSource

class AlbumRepositoryImpl(
    private val remoteAlbumSrc: AlbumRemoteDataSource,
    private val localAlbumSrc: AlbumLocalDataSource
) : AlbumRepository {
//    override suspend fun loadAlbums(): Result<AlbumList> {
////        return remoteAlbumSrc.loadAlbumsRemote()
//    }
}