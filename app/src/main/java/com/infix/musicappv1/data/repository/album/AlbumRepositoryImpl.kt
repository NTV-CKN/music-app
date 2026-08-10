package com.infix.musicappv1.data.repository.album

import androidx.paging.PagingSource
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.model.album.AlbumList
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.source.AlbumDataSource
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.source.remote.param.SearchParam
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AlbumRepositoryImpl @Inject constructor(
    private val remote: AlbumDataSource.Remote,
    private val local: AlbumDataSource.Local
) : AlbumRepository {
    override suspend fun loadAlbumsPaging(
        query: String,
        limit: Int,
        key: Int
    ): AlbumList? {
        return remote.loadAlbumsPaging(query, limit, key)
    }


    override suspend fun loadAllAlbums(): List<Album> {
        return local.loadAllAlbums()
    }

    override fun loadAllAlbumsFlow(): Flow<List<Album>> {
        return local.loadAllAlbumsFlow()
    }

    override fun loadAlbumsPaging(): PagingSource<Int, Album> {
        return local.loadAlbumsPaging()
    }

    override fun loadNAlbumPaging(limit: Int): PagingSource<Int, Album> {
        return local.loadNAlbumPaging(limit)
    }

    override suspend fun loadSongsByAlbumId(searchParam: SearchParam): Result<List<Song>> {
        return remote.loadSongsByAlbumId(searchParam)
    }
}