package com.infix.musicappv1.data.source.remote

import com.infix.musicappv1.data.dto.BaseResultResponse
import com.infix.musicappv1.data.dto.RequestPaymentResponse
import com.infix.musicappv1.data.model.Subscription
import com.infix.musicappv1.data.model.SubscriptionList
import com.infix.musicappv1.data.model.ai_rcm.AiRecommendationResponse
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.model.album.AlbumList
import com.infix.musicappv1.data.model.artist.Artist
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

    @GET("v1/admin/subscriptions")
    suspend fun loadSubscriptionsPaging(
        @Query("query") query: String,
        @Query("limit") limit: Int,
        @Query("key") key: Int
    ): Response<SubscriptionList>

    @POST("v1/admin/save-song")
    suspend fun saveSong(@Body song: Song): Response<BaseResultResponse>

    @POST("v1/admin/update-song")
    suspend fun updateSong(@Body song: Song): Response<BaseResultResponse>

    @POST("v1/admin/remove-song")
    suspend fun removeSong(@Body map: Map<String, String>): Response<BaseResultResponse>

    @POST("v1/admin/save-album")
    suspend fun saveAlbum(@Body album: Album): Response<BaseResultResponse>

    @POST("v1/admin/delete-album")
    suspend fun deleteAlbum(@Body body: Map<String, String>): Response<BaseResultResponse>

    @POST("v1/admin/save-artist")
    suspend fun saveArtist(@Body artist: Artist): Response<BaseResultResponse>

    @POST("v1/admin/delete-artist")
    suspend fun deleteArtist(@Body body: Map<String, Long>): Response<BaseResultResponse>

    @POST("v1/admin/save-subscription")
    suspend fun saveSubscription(@Body subscription: Subscription): Response<BaseResultResponse>

    @POST("v1/admin/update-subscription")
    suspend fun updateSubscription(@Body subscription: Subscription): Response<BaseResultResponse>

    @POST("v1/admin/remove-subscription")
    suspend fun removeSubscription(@Body body: Map<String, String>): Response<BaseResultResponse>

    @POST("v1/create-url")
    suspend fun createPaymentUrl(@Body body: Map<String, String>): Response<RequestPaymentResponse>

    @POST("v1/ai-rcm/recommend")
    suspend fun getSongRecommend(@Body body: Map<String, String>): Response<AiRecommendationResponse>
}
