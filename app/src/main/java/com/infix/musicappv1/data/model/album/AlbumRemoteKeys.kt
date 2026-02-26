package com.infix.musicappv1.data.model.album

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "album_remote_keys")
data class AlbumRemoteKeys(
    @ColumnInfo("album_id")
    val albumId: String,
    @ColumnInfo("prev_key")
    val prevKey: Int?,
    @ColumnInfo("next_key")
    val nextKey: Int?
)
