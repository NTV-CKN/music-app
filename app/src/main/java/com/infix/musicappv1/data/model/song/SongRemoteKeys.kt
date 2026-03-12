package com.infix.musicappv1.data.model.song

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "song_remote_keys")
data class SongRemoteKeys(
    @PrimaryKey
    @ColumnInfo("song_id")
    val songId: String,
    @ColumnInfo("create_at")
    val createAt: Long = System.currentTimeMillis()
)
