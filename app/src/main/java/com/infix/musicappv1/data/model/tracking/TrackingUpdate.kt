package com.infix.musicappv1.data.model.tracking

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracking_update")
data class TrackingUpdate(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo("song_update_at")
    val songUpdateAt: Long,
    @ColumnInfo("artist_update_at")
    val artistUpdateAt: Long
)