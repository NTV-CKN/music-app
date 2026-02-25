package com.infix.musicappv1.data.source

import androidx.paging.PagingSource
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.data.model.artist.ArtistSongCrossRef
import com.infix.musicappv1.data.model.artist.ArtistWithSongs
import com.infix.musicappv1.data.source.remote.PagingParam
import kotlinx.coroutines.flow.Flow

interface ArtistDataSource {
    interface Local {
        fun getArtistsPaging(): PagingSource<Int, Artist>
        fun getNArtistsPaging(limit: Int = 10): PagingSource<Int, Artist>
        fun getLimitArtists(limit: Int = 10): Flow<List<Artist>>
        fun getAllInterestedArtist(): Flow<List<Artist>>
        fun getLimitInterestArtist(limit: Int = 10): Flow<List<Artist>>
        suspend fun delete(vararg artist: Artist)
        suspend fun update(vararg artist: Artist)
        suspend fun insert(vararg artist: Artist)
        suspend fun insertArtistSongCrossRef(vararg artistSongCrossRef: ArtistSongCrossRef)
        suspend fun getArtistWithSongsByArtistId(artistId: Int): ArtistWithSongs?
    }

    interface Remote {
        suspend fun loadArtistsRemote(): Result<List<Artist>>
        suspend fun loadArtistsPaging(pagingParam: PagingParam): List<Artist>?
    }
}