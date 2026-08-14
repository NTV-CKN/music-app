package com.infix.musicappv1.data.repository.artist

import androidx.paging.PagingSource
import com.infix.musicappv1.data.dto.BaseResultResponse
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.data.model.artist.ArtistList
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.source.Result
import kotlinx.coroutines.flow.Flow

interface ArtistRepository {
    fun getArtistsPaging(): PagingSource<Int, Artist>
    fun getNArtistsPaging(limit: Int = 10): PagingSource<Int, Artist>
    fun getLimitArtists(limit: Int = 10): Flow<List<Artist>>
    fun getAllInterestedArtist(): Flow<List<Artist>>
    fun getLimitInterestArtist(limit: Int = 10): Flow<List<Artist>>
    suspend fun loadAllArtistLocal(): List<Artist>
    suspend fun delete(vararg artist: Artist)
    suspend fun update(vararg artist: Artist)
    suspend fun insert(vararg artist: Artist)
    suspend fun getArtistById(artistId: Long): Artist?
    suspend fun loadArtistsPaging(query: String, limit: Int, key: Int): ArtistList?
    suspend fun loadSongsByArtistId(artistId: Long): Result<List<Song>>

    //admin
    suspend fun saveArtist(artist: Artist, isUpdate: Boolean): Result<BaseResultResponse>
}