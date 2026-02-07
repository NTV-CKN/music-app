package com.infix.musicappv1.data.model.now_playing

import androidx.media3.common.MediaItem
import com.infix.musicappv1.data.model.song.Song
import java.util.Date

data class Playlist(
    var namePlaylist: String = "",
    private var _idPlaylist: Int = 10001,
    var artwork: String = "",
    var createdAt: Date? = null
) {
    //    var idPlaylist: Int
//        get() = _idPlaylist
//        set(value) {
//            _idPlaylist = if (value < 0) autoId++
//            else value
//        }
    private val _songs: MutableList<Song> = mutableListOf()
    val songs: List<Song> = _songs

    private val mediaItems: MutableList<MediaItem> = mutableListOf()

    fun updateSongs(songs: List<Song>) {
        this._songs.clear()
        this._songs.addAll(songs)
        updateMediaItems(songs)
    }

    fun getMediaItems() = mediaItems

    private fun updateMediaItems(songs: List<Song>) {
        this.mediaItems.clear()
        songs.forEach { song -> this.mediaItems.add(MediaItem.fromUri(song.source)) }
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Playlist

        return _idPlaylist == other._idPlaylist
    }

    override fun hashCode(): Int {
        return _idPlaylist
    }

    companion object {
        private var autoId = 10001
    }
}
