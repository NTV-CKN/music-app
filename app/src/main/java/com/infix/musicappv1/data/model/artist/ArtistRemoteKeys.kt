package com.infix.musicappv1.data.model.artist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "artist_remote_keys")
data class ArtistRemoteKeys(
    @PrimaryKey
    @ColumnInfo("artist_id")
    val artistId: Long,
    @ColumnInfo("create_at")
    val  createAt: Long = System.currentTimeMillis()
)
