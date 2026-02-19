package com.infix.musicappv1.data.source.remote

import com.infix.musicappv1.data.model.artist.ArtistList
import com.infix.musicappv1.data.model.playlist.PlaylistList
import com.infix.musicappv1.data.model.song.SongList
import retrofit2.Response
import retrofit2.http.GET

//provide api for album, artist, song
/*
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
}