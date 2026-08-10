package com.infix.musicappv1.data.source

import androidx.paging.PagingSource
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.data.model.artist.ArtistList
import com.infix.musicappv1.data.model.song.Song
import kotlinx.coroutines.flow.Flow

interface ArtistDataSource {
    interface Local {
        fun getArtistsPaging(): PagingSource<Int, Artist>
        fun getNArtistsPaging(limit: Int = 10): PagingSource<Int, Artist>
        fun getLimitArtists(limit: Int = 10): Flow<List<Artist>>
        fun getAllInterestedArtist(): Flow<List<Artist>>
        fun getLimitInterestArtist(limit: Int = 10): Flow<List<Artist>>
        suspend fun getArtistById(artistId: Int): Artist?
        suspend fun delete(vararg artist: Artist)
        suspend fun update(vararg artist: Artist)
        suspend fun insert(vararg artist: Artist)
        suspend fun loadAllArtistLocal(): List<Artist>
    }

    interface Remote {
//        suspend fun loadArtistsRemote(): Result<List<Artist>>
        suspend fun loadArtistsPaging(
            query: String,
            limit: Int,
            key: Int
        ): ArtistList?

        suspend fun loadSongsByArtistId(artistId: Int): Result<List<Song>>
    }
}