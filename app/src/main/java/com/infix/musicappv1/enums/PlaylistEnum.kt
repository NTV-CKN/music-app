package com.infix.musicappv1.enums

enum class PlaylistEnum(val value: String, val playlistId: Int) {
    MORE_RCM_SONG("More Recommend Song", 1),
    FAVORITES("Favorites", 2),
    RECOMMENDED("Recommended", 3),
    RECENT("Recent", 4),
    SEARCH("Search", 5),
    MOST_HEARD("Songs Most Heard", 6),
    CUSTOM("Song Custom", 8),
    DETAIL_ALBUM("Detail Album", 9),
    MORE_SONG_MOST_HEARD("More Songs Most Heard", 10),
    RESULT_SEARCH_SONG("Result Search Songs", 11),
    RECENT_SEARCH_SONG("Recent Search Songs", 12)
}