package com.infix.musicappv1.data.source

import androidx.paging.PagingSource
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.source.remote.param.PagingParam

interface AlbumDataSource {
    interface Remote {
        suspend fun loadAlbumsPaging(pagingParam: PagingParam): Result<List<Album>>
    }

    interface Local {
        fun loadAlbumsPaging(): PagingSource<Int, Album>
        fun loadNAlbumPaging(limit: Int = 9): PagingSource<Int, Album>
    }
}