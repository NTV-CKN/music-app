package com.infix.musicappv1.data.source.local.artist

import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.data.source.ArtistDataSource
import kotlinx.coroutines.flow.Flow

class ArtistLocalDataSource(private val artistDao: ArtistDao) : ArtistDataSource.Local {
    override fun getAllArtists(): Flow<List<Artist>> {
        return artistDao.getAllArtists()
    }

    override fun getLimitArtists(limit: Int): Flow<List<Artist>> {
        return artistDao.getLimitArtists(limit)
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