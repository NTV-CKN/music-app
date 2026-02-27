package com.infix.musicappv1.data.source.remote.param

data class SearchParam(
    val queryType: String,
    val query: String
) {

    companion object{
        const val QUERY_TYPE_SEARCH = "search"
        const val QUERY_TYPE_ALBUM_WITH_SONG = "albumWithSongs"
    }
}
