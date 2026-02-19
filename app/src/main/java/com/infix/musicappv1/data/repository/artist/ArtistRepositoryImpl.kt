package com.infix.musicappv1.data.repository.artist

import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.data.source.ArtistDataSource
import com.infix.musicappv1.data.source.Result

class ArtistRepositoryImpl(
    private val local: ArtistDataSource.Local,
    private val remote: ArtistDataSource.Remote
) : ArtistRepository {
    override suspend fun loadArtistsRemote(): Result<List<Artist>> {
        return remote.loadArtistsRemote()
    }
}