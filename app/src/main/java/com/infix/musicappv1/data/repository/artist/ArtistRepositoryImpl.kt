package com.infix.musicappv1.data.repository.artist

import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.data.source.ArtistDataSource
import com.infix.musicappv1.data.source.Result
import kotlinx.coroutines.flow.Flow

class ArtistRepositoryImpl(
    private val local: ArtistDataSource.Local,
    private val remote: ArtistDataSource.Remote
) : ArtistRepository {
    override suspend fun loadArtistsRemote(): Result<List<Artist>> {
        return remote.loadArtistsRemote()
    }

    override fun getAllArtists(): Flow<List<Artist>> {
        return local.getAllArtists()
    }

    override fun getLimitArtists(limit: Int): Flow<List<Artist>> {
        return local.getLimitArtists(limit)
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
}