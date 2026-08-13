package com.infix.musicappv1.data.model.artist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.PropertyName

@Entity(tableName = "artists")
data class Artist(
    @PrimaryKey
    @ColumnInfo("artist_id")
    val id: Long = 0,
    @ColumnInfo("artist_name")
    val name: String = "",
    @ColumnInfo("artist_avatar")
    val avatar: String = "",
    @ColumnInfo("artist_interested")
    val amountInterested: Int = 0,
    @ColumnInfo("artist_is_interested")
    @get:PropertyName("artist_is_interested")
    @set:PropertyName("artist_is_interested")
    var isInterested: Boolean = false
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Artist

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.toInt()
    }
}
