package com.infix.musicappv1.data.model.playlist

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.infix.musicappv1.data.model.song.Song

class PlaylistWithSongs(
    @Embedded
    val playlist: Playlist,
    @Relation(
        parentColumn = "playlist_id",
        entityColumn = "song_id",
        associateBy = Junction(PlaylistSong::class)
    )
    val songs: List<Song>
)