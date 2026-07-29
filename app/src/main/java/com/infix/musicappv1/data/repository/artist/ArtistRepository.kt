package com.infix.musicappv1.data.repository.artist

import androidx.paging.PagingSource
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.source.remote.param.PagingParam
import kotlinx.coroutines.flow.Flow

interface ArtistRepository {
    suspend fun loadArtistsRemote(): Result<List<Artist>>
    fun getArtistsPaging(): PagingSource<Int, Artist>
    fun getNArtistsPaging(limit: Int = 10): PagingSource<Int, Artist>
    fun getLimitArtists(limit: Int = 10): Flow<List<Artist>>
    fun getAllInterestedArtist(): Flow<List<Artist>>
    fun getLimitInterestArtist(limit: Int = 10): Flow<List<Artist>>
    suspend fun loadAllArtistLocal(): List<Artist>
    suspend fun delete(vararg artist: Artist)
    suspend fun update(vararg artist: Artist)
    suspend fun insert(vararg artist: Artist)
    suspend fun getArtistById(artistId: Int): Artist?
    suspend fun loadArtistsPaging(pagingParam: PagingParam): List<Artist>?
    suspend fun loadSongsByArtistId(artistId: Int): Result<List<Song>>
}