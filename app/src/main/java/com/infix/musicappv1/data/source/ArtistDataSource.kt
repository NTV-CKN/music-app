package com.infix.musicappv1.data.source

import com.infix.musicappv1.data.model.artist.Artist

interface ArtistDataSource {
    interface Local {}
    interface Remote {
        suspend fun loadArtistsRemote(): Result<List<Artist>>
    }
}