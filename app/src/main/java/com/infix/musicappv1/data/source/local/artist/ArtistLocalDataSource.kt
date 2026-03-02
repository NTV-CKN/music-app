package com.infix.musicappv1.data.source.local.artist

import androidx.paging.PagingSource
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.data.source.ArtistDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ArtistLocalDataSource @Inject constructor(
    private val artistDao: ArtistDao
) : ArtistDataSource.Local {
    override fun getArtistsPaging(): PagingSource<Int, Artist> {
        return artistDao.getArtistsPaging()
    }

    override fun getNArtistsPaging(limit: Int): PagingSource<Int, Artist> {
        return artistDao.getNArtistsPaging(limit)
    }


    override fun getLimitArtists(limit: Int): Flow<List<Artist>> {
        return artistDao.getLimitArtists(limit)
    }

    override fun getAllInterestedArtist(): Flow<List<Artist>> {
        return artistDao.getAllArtists()
    }

    override fun getLimitInterestArtist(limit: Int): Flow<List<Artist>> {
        return artistDao.getLimitArtistInterested(limit)
    }

    override suspend fun getArtistById(artistId: Int): Artist? {
       return artistDao.getArtistById(artistId)
    }

    override suspend fun delete(vararg artist: Artist) {
        artistDao.delete(*artist)
    }

    override suspend fun update(vararg artist: Artist) {
        artistDao.update(*artist)
    }


    override suspend fun insert(vararg artist: Artist) {
        artistDao.insert(*artist)
    }
}