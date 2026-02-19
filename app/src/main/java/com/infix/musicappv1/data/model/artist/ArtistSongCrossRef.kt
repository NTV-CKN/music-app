package com.infix.musicappv1.data.model.artist

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "artist_song_cross_ref",
    primaryKeys = ["artist_id", "song_id"]
)
class ArtistSongCrossRef(
    @ColumnInfo("artist_id")
    val artistId: Int,
    @ColumnInfo("song_id")
    val songId: String
)