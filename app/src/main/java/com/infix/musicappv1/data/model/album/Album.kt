package com.infix.musicappv1.data.model.album

data class Album(
   val id: Int = 0,
    val name: String = "",
    val songs: List<String> = emptyList(),
    val size: Int = 0,
    val artwork: String = ""
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
