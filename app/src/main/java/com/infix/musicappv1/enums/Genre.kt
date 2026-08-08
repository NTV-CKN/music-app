package com.infix.musicappv1.enums

enum class Genre {
    BALLAD,
    LOFI,
    ACOUSTIC,
    POP,
    INDIE,
    HIPHOP_RAP,
    EDM_DANCE,
    REMIX,
    ROCK,
    BOLERO;

    fun stringToGenre(str: String): Genre? {
        for(genre in Genre.entries) {
            if(genre.name == str)
                return genre
        }

        return null
    }
}