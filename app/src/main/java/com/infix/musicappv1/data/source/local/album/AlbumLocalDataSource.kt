package com.infix.musicappv1.data.source.local.album

import androidx.paging.PagingSource
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.source.AlbumDataSource

class AlbumLocalDataSource(
    private val albumDao: AlbumDao
): AlbumDataSource.Local {
    override fun loadAlbumsPaging(): PagingSource<Int, Album> {
        return albumDao.loadAlbumsPaging()
    }

    override fun loadNAlbumPaging(limit: Int): PagingSource<Int, Album> {
       return albumDao.loadNAlbumsPaging(limit)
    }
}