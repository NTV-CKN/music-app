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

//provide api for album, artist, song
/*
paging:
    https://thantrieu.com/services/services.php/songs
    https://thantrieu.com/services/services.php/artists
    https://thantrieu.com/services/services.php/albums

get song by name artist or name song
https://thantrieu.com/services/services.php?queryType=search&query=mr. siro
get song by album id
https://thantrieu.com/services/services.php?queryType=albumWithSongs&albumId=10001

https://thantrieu.com/resources/braniumapis/playlist.json
https://thantrieu.com/resources/braniumapis/songs.json
https://thantrieu.com/resources/braniumapis/artists.json
 */
interface MusicService {
    @GET("/resources/braniumapis/songs.json")
    suspend fun loadSongs(): Response<SongList>

    @GET("/resources/braniumapis/playlist.json")
    suspend fun loadSystemPlaylists(): Response<PlaylistList>

    @GET("/resources/braniumapis/artists.json")
    suspend fun loadArtists(): Response<ArtistList>

    @POST("/services/services.php/songs")
    suspend fun loadSongsPaging(@Body pagingParam: PagingParam): Response<SongList>

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