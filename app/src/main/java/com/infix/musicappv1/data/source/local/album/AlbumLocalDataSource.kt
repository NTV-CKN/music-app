package com.infix.musicappv1.data.source.local.album

import androidx.paging.PagingSource
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.source.AlbumDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AlbumLocalDataSource @Inject constructor(
    private val albumDao: AlbumDao
): AlbumDataSource.Local {
    override suspend fun loadAllAlbums(): List<Album> {
        return albumDao.loadAllAlbums()
    }

    override fun loadAlbumsPaging(): PagingSource<Int, Album> {
        return albumDao.loadAlbumsPaging()
    }

    override fun loadAllAlbumsFlow(): Flow<List<Album>> {
        return albumDao.loadAllAlbumFlow()
    }

    override fun loadNAlbumPaging(limit: Int): PagingSource<Int, Album> {
       return albumDao.loadNAlbumsPaging(limit)
    }
}