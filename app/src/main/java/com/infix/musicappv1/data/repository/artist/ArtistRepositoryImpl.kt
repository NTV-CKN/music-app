package com.infix.musicappv1.data.repository.artist

import androidx.paging.PagingSource
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.data.model.artist.ArtistSongCrossRef
import com.infix.musicappv1.data.model.artist.ArtistWithSongs
import com.infix.musicappv1.data.source.ArtistDataSource
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.source.remote.PagingParam
import kotlinx.coroutines.flow.Flow

class ArtistRepositoryImpl(
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

    override suspend fun delete(vararg artist: Artist) {
        local.delete(*artist)
    }

    override suspend fun update(vararg artist: Artist) {
        local.update(*artist)
    }

    override suspend fun insert(vararg artist: Artist) {
        local.insert(*artist)
    }

    override suspend fun insertArtistSongCrossRef(vararg artistSongCrossRef: ArtistSongCrossRef) {
        local.insertArtistSongCrossRef(*artistSongCrossRef)
    }

    override suspend fun getArtistWithSongsByArtistId(artistId: Int): ArtistWithSongs? {
        return local.getArtistWithSongsByArtistId(artistId)
    }

    override suspend fun loadArtistsPaging(pagingParam: PagingParam): List<Artist>? {
        return remote.loadArtistsPaging(pagingParam)
    }
}