package com.infix.musicappv1.data.model.album

import com.google.gson.annotations.SerializedName

data class AlbumList(
    @SerializedName("playlists")
    val albums: List<Album> = emptyList()
)