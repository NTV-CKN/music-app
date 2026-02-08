package com.infix.musicappv1.data.model.album

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "albums")
data class Album(
    @PrimaryKey
   var id: Int = 0,
    var name: String = "",
    @Ignore
    var songs: List<String> = emptyList(),
    var size: Int = 0,
    var artwork: String = ""
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Album

        return id == other.id
    }

    override fun hashCode(): Int {
        return id
    }
}
