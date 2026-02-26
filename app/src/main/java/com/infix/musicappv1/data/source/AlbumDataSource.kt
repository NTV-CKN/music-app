package com.infix.musicappv1.data.source

import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.source.remote.param.PagingParam

interface AlbumDataSource {
    interface Remote {
        suspend fun loadAlbumsPaging(pagingParam: PagingParam): Result<List<Album>>
    }

    interface Local {
    }
}