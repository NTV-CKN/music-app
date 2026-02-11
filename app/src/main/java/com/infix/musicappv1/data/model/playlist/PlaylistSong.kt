package com.infix.musicappv1.data.model.playlist

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity("playlist_songs", primaryKeys = ["playlist_id", "song_id"])
data class PlaylistSong(
    @ColumnInfo("playlist_id")
    var playlistId: Int = 0,
    @ColumnInfo("song_id")
    var songId: String = ""
)