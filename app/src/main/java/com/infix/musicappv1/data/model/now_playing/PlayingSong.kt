package com.infix.musicappv1.data.model.now_playing

import androidx.media3.common.MediaItem
import com.infix.musicappv1.data.model.song.Song

data class PlayingSong(
    private var _song: Song? = null,
    private var playlist: Playlist? = null,
    private var indexCurrent: Int = -1
) {
    private var mediaItem: MediaItem? = null
    var song: Song?
        get() = _song
        set(value) {
            value?.let {
                _song = it
                mediaItem = MediaItem.fromUri(it.source)
            }
        }

    init {
        song?.let {
            mediaItem = MediaItem.fromUri(it.source)
        }
    }

    fun getMediaItem() = mediaItem

    fun setIndexCurrent(index: Int) {
        this.indexCurrent = index
    }

    fun setPlaylist(playlist: Playlist?) {
        this.playlist = playlist
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlayingSong

        if (song != other.song) return false
        if (playlist != other.playlist) return false

        return true
    }

    override fun hashCode(): Int {
        var result = song?.hashCode() ?: 0
        result = 31 * result + (playlist?.hashCode() ?: 0)
        return result
    }
}