package com.infix.musicappv1.data.repository.artist

import androidx.paging.PagingSource
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.source.ArtistDataSource
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.source.remote.param.PagingParam
import com.infix.musicappv1.data.source.remote.param.SearchParam
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ArtistRepositoryImpl @Inject constructor(
    private val local: ArtistDataSource.Local,
    private val remote: ArtistDataSource.Remote
) : ArtistRepository {
    override suspend fun loadArtistsRemote(): Result<List<Artist>> {
        return remote.loadArtistsRemote()
    }

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

    override suspend fun getArtistById(artistId: Int): Artist? {
        return local.getArtistById(artistId)
    }

    override suspend fun loadArtistsPaging(pagingParam: PagingParam): List<Artist>? {
        return remote.loadArtistsPaging(pagingParam)
    }

    override suspend fun loadSongsByNameArtist(searchParam: SearchParam): Result<List<Song>> {
        return remote.loadSongsByNameArtist(searchParam)
    }
}