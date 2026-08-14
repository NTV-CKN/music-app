package com.infix.musicappv1.data.repository.artist

import androidx.paging.PagingSource
import com.infix.musicappv1.data.dto.BaseResultResponse
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.data.model.artist.ArtistList
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.source.ArtistDataSource
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.utils.GenerateIdHelper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ArtistRepositoryImpl @Inject constructor(
    private val local: ArtistDataSource.Local,
    private val remote: ArtistDataSource.Remote
) : ArtistRepository {
    override fun getArtistsPaging(): PagingSource<Int, Artist> {
        return local.getArtistsPaging()
    }

    override fun getNArtistsPaging(limit: Int): PagingSource<Int, Artist> {
        return local.getNArtistsPaging(limit)
    }

    override fun getLimitArtists(limit: Int): Flow<List<Artist>> {
        return local.getLimitArtists(limit)
    }

    override fun getAllInterestedArtist(): Flow<List<Artist>> {
        return local.getAllInterestedArtist()
    }

    override fun getLimitInterestArtist(limit: Int): Flow<List<Artist>> {
        return local.getLimitInterestArtist(limit)
    }

    override suspend fun loadAllArtistLocal(): List<Artist> {
        return local.loadAllArtistLocal()
    }

    override suspend fun delete(vararg artist: Artist) {
        local.delete(*artist)
    }

    override suspend fun update(vararg artist: Artist) {
        local.update(*artist)
    }

    override suspend fun insert(vararg artist: Artist) {
        local.insert(*artist)
    }

    override suspend fun getArtistById(artistId: Long): Artist? {
        return local.getArtistById(artistId)
    }

    override suspend fun loadArtistsPaging(
        query: String,
        limit: Int,
        key: Int
    ): ArtistList? {
        return remote.loadArtistsPaging(query, limit, key)
    }


    override suspend fun loadSongsByArtistId(artistId: Long): Result<List<Song>> {
        return remote.loadSongsByArtistId(artistId)
    }

    override suspend fun saveArtist(artist: Artist, isUpdate: Boolean): Result<BaseResultResponse> {
        try {
            if(!isUpdate) artist.id = GenerateIdHelper.generateIdLong()

            val uploadedStorage = remote.uploadAvatar(
                artist.avatar,
                artist.id.toString()
            )

            if (uploadedStorage != null) {
                artist.avatar = uploadedStorage
            }

            val response = remote.saveArtist(artist)
            if (response.isSuccessful) {
                return Result.Success(
                    response.body()
                        ?: BaseResultResponse(
                            true,
                            "Unknown message"
                        )
                )
            }

            throw Exception("Lưu artist thất bại")
        } catch (ex: Exception) {
            return Result.Error(ex)
        }
    }
}