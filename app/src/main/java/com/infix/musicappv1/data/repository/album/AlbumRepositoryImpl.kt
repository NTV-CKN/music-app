package com.infix.musicappv1.data.repository.album

import androidx.paging.PagingSource
import com.infix.musicappv1.data.dto.BaseResultResponse
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.model.album.AlbumList
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.source.AlbumDataSource
import com.infix.musicappv1.data.source.Result
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

    override suspend fun loadSongsByAlbumId(albumId: String): Result<List<Song>> {
        return remote.loadSongsByAlbumId(albumId)
    }

    override suspend fun saveAlbum(album: Album, isUpdate: Boolean): Result<BaseResultResponse> {
        try {
            val uploadedStorage = remote.uploadArtwork(
                album.artwork,
                album.id
            )

            if (uploadedStorage != null) {
                album.artwork = uploadedStorage
            }

            val response = remote.saveAlbum(album)
            if (response.isSuccessful) {
                return Result.Success(
                    response.body()
                        ?: BaseResultResponse(
                            true,
                            "Unknown message"
                        )
                )
            }

            throw Exception("Lưu album thất bại")
        } catch (ex: Exception) {
            return Result.Error(ex)
        }

    }
}