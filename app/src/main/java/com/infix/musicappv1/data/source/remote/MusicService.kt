package com.infix.musicappv1.data.source.remote

import com.infix.musicappv1.data.dto.BaseResultResponse
import com.infix.musicappv1.data.model.album.AlbumList
import com.infix.musicappv1.data.model.artist.ArtistList
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.model.song.SongList
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MusicService {
    @GET("v1/admin/songs")
    suspend fun loadSongsPaging(
        @Query("query") query: String,
        @Query("limit") limit: Int,
        @Query("key") key: Int
    ): Response<SongList>

    @GET("v1/admin/artists")
    suspend fun loadArtistsPaging(
        @Query("query") query: String,
        @Query("limit") limit: Int,
        @Query("key") key: Int
    ): Response<ArtistList>

    @GET("v1/admin/albums")
    suspend fun loadAlbumsPaging(
        @Query("query") query: String,
        @Query("limit") limit: Int,
        @Query("key") key: Int
    ): Response<AlbumList>

    @POST("v1/admin/save-song")
    suspend fun saveSong(@Body song: Song): Response<BaseResultResponse>

    @POST("v1/admin/update-song")
    suspend fun updateSong(@Body song: Song): Response<BaseResultResponse>
}