package com.infix.musicappv1.data.model.playlist

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity("playlist_song_cross_ref", primaryKeys = ["playlist_id", "song_id"])
data class PlaylistSongCrossRef(
    @ColumnInfo("playlist_id")
    var playlistId: Long = 0,
    @ColumnInfo("song_id")
    var songId: String = ""
)