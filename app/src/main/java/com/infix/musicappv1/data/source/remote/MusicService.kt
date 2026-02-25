package com.infix.musicappv1.data.source.remote

import com.infix.musicappv1.data.model.artist.ArtistList
import com.infix.musicappv1.data.model.playlist.PlaylistList
import com.infix.musicappv1.data.model.song.SongList
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

//provide api for album, artist, song
/*
paging:
    https://thantrieu.com/services/services.php/songs
    https://thantrieu.com/services/services.php/artists

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
}