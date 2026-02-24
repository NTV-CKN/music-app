package com.infix.musicappv1.utils

import com.infix.musicappv1.data.model.song.Song

object MusicAppUtils {
    const val DEFAULT_PLAYLIST_ID_CUSTOM = 50000
    const val KEY_FRACTION_EXTRA = "KEY_FRACTION_EXTRA"
    var density: Float = 0f

    fun getIndexOfSong(song: Song, songs: List<Song>): Int {
        val index = songs.indexOf(song)
        return if (index == -1) 0 else index
    }
}