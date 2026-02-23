package com.infix.musicappv1.data.model.song

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "song_remote_keys")
data class SongRemoteKeys(
    @ColumnInfo("song_id")
    val songId: String,
    @ColumnInfo("prev_key")
    val prevKey: Int?,
    @ColumnInfo("next_key")
    val nextKey: Int?
)
