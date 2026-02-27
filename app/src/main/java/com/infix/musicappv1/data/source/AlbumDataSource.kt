package com.infix.musicappv1.data.source

import androidx.paging.PagingSource
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.source.remote.param.PagingParam
import com.infix.musicappv1.data.source.remote.param.SearchParam
import kotlinx.coroutines.flow.Flow

interface AlbumDataSource {
    interface Remote {
        suspend fun loadAlbumsPaging(pagingParam: PagingParam): Result<List<Album>>
        suspend fun loadSongsByAlbumId(searchParam: SearchParam): Result<List<Song>>
    }

    interface Local {
        fun loadAlbumsPaging(): PagingSource<Int, Album>
        fun loadAllAlbumsFlow(): Flow<List<Album>>
        fun loadNAlbumPaging(limit: Int = 9): PagingSource<Int, Album>
    }
}