package com.infix.musicappv1.data.source

import com.infix.musicappv1.data.model.artist.Artist
import kotlinx.coroutines.flow.Flow

interface ArtistDataSource {
    interface Local {
        fun getAllArtists(): Flow<List<Artist>>
        fun getLimitArtists(limit: Int = 10): Flow<List<Artist>>
        suspend fun delete(vararg artist: Artist)
        suspend fun update(vararg artist: Artist)
        suspend fun insert(vararg artist: Artist)
    }

    interface Remote {
        suspend fun loadArtistsRemote(): Result<List<Artist>>
    }
}