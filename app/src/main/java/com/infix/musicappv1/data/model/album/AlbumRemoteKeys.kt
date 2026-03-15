package com.infix.musicappv1.data.model.album

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "album_remote_keys")
data class AlbumRemoteKeys(
    @PrimaryKey
    @ColumnInfo("album_id")
    val albumId: String,
    @ColumnInfo("create_at")
   val  createAt: Long = System.currentTimeMillis()
)
