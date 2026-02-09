package com.infix.musicappv1.data.model.song

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
open class Song(
    @PrimaryKey
    var id: String = "",
    var title: String = "",
    var album: String = "",
    var artist: String = "",
    var source: String = "",
    var image: String = "",
    var duration: Int = 0,
    var favorite: Boolean = false,
    var counter: Int = 0,
    var replay: Int = 0
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Song

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
