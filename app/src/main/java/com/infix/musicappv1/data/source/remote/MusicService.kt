package com.infix.musicappv1.data.source.remote

import com.infix.musicappv1.data.model.album.AlbumList
import com.infix.musicappv1.data.model.artist.ArtistList
import com.infix.musicappv1.data.model.playlist.PlaylistList
import com.infix.musicappv1.data.model.song.SongList
import com.infix.musicappv1.data.source.remote.param.PagingParam
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MusicService {
    @GET("/resources/braniumapis/songs.json")
    suspend fun loadSongs(): Response<SongList>

    @GET("/resources/braniumapis/playlist.json")
    suspend fun loadSystemPlaylists(): Response<PlaylistList>

    @GET("/resources/braniumapis/artists.json")
    suspend fun loadArtists(): Response<ArtistList>

    @GET("v1/admin-song/songs")
    suspend fun loadSongsPaging(
        @Query("query") query: String,
        @Query("limit") limit: Int,
        @Query("key") key: Int
    ): Response<SongList>

    @POST("/services/services.php/artists")
    suspend fun loadArtistsPaging(@Body pagingParam: PagingParam): Response<ArtistList>

    @POST("/services/services.php/albums")
    suspend fun loadAlbumsPaging(@Body pagingParam: PagingParam): Response<AlbumList>

    @GET("/services/services.php")
    suspend fun getSongsOfArtist(
        @Query("queryType") queryType: String,
        @Query("query") nameArtist: String
    ): Response<SongList>

    @GET("/services/services.php")
    suspend fun getSongsByAlbumId(
        @Query("queryType") queryType: String,
        @Query("albumId") albumId: Int
    ): Response<SongList>
}