package com.infix.musicappv1.data.model.artist

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.infix.musicappv1.data.model.song.Song

class ArtistWithSongs(
    @Embedded
    val artist: Artist,
    @Relation(
        parentColumn = "artist_id",
        entityColumn = "song_id",
        associateBy = Junction(ArtistSongCrossRef::class)
    )
    val songs: List<Song>
)