package com.infix.musicappv1.data.model.song

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
open class Song(
    @PrimaryKey
    @ColumnInfo("song_id")
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



    override fun toString(): String {
        return "Song(id='$id', title='$title', album='$album', artist='$artist', source='$source', image='$image', duration=$duration, favorite=$favorite, counter=$counter, replay=$replay)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Song) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
