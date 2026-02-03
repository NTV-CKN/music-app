package com.infix.musicappv1.data.model.song

open class Song(
    val id: Int = 0,
    val title: String = "",
    val album: String = "",
    val artist: String = "",
    val source: String = "",
    val image: String = "",
    val duration: Int = 0,
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
        return id
    }
}
