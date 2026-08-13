package com.infix.musicappv1.data.source

import androidx.paging.PagingSource
import com.infix.musicappv1.data.dto.BaseResultResponse
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.model.album.AlbumList
import com.infix.musicappv1.data.model.song.Song
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface AlbumDataSource {
    interface Remote {
        suspend fun loadAlbumsPaging(
            query: String,
            limit: Int,
            key: Int
        ): AlbumList?

        suspend fun loadSongsByAlbumId(albumId: String): Result<List<Song>>

        //admin
        suspend fun deleteAlbum(body: Map<String, String>): Response<BaseResultResponse>
        suspend fun saveAlbum(album: Album): Response<BaseResultResponse>
        suspend fun uploadArtwork(artwork: String, id: String): String?
    }

    interface Local {
        suspend fun loadAllAlbums(): List<Album>
        fun loadAlbumsPaging(): PagingSource<Int, Album>
        fun loadAllAlbumsFlow(): Flow<List<Album>>
        fun loadNAlbumPaging(limit: Int = 9): PagingSource<Int, Album>
    }
}