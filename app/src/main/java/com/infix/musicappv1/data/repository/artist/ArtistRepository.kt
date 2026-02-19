package com.infix.musicappv1.data.repository.artist

import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.data.source.Result

interface ArtistRepository {
    suspend fun loadArtistsRemote(): Result<List<Artist>>
}