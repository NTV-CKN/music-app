package com.infix.musicappv1.data.model.artist

data class Artist(
    val id: Int,
    val name: String,
    val avatar: String,
    val interested: Int
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Artist

        return id == other.id
    }

    override fun hashCode(): Int {
        return id
    }
}
