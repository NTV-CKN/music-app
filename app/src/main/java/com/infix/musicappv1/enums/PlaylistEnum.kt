package com.infix.musicappv1.enums

enum class PlaylistEnum(val value: String, val playlistId: Int) {
    MORE_RCM_SONG("More Rcm Song", 1),
    FAVORITES("Favorites", 2),
    RECOMMENDED("Recommended", 3),
    RECENT("Recent", 4),
    SEARCH("Search", 5),
    MOST_HEARD("Most_Heard", 6),
    FOR_YOU("For_You", 7),
    CUSTOM("Custom", 8),
    DETAIL_ALBUM("Detail Album", 9)
}