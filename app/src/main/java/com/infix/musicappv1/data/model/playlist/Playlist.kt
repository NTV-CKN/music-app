package com.infix.musicappv1.data.model.playlist

import androidx.media3.common.MediaItem
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.infix.musicappv1.data.model.song.Song
import java.util.Date

@Entity
data class Playlist(
    @PrimaryKey(true)
    @ColumnInfo("id")
    var idPlaylist: Int = 10001,
    @ColumnInfo("name")
    var namePlaylist: String = "",
    @ColumnInfo("artwork")
    var artwork: String = "",
    @ColumnInfo("create_at")
    var createdAt: Date? = null
) {
    //    var idPlaylist: Int
//        get() = _idPlaylist
//        set(value) {
//            _idPlaylist = if (value < 0) autoId++
//            else value
//        }
    @Ignore
    private val _songs: MutableList<Song> = mutableListOf()

    @Ignore
    val songs: List<Song> = _songs

    @Ignore
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

        if (idPlaylist != other.idPlaylist) return false
        if (_songs != other._songs) return false

        return true
    }

    override fun hashCode(): Int {
        var result = idPlaylist
        result = 31 * result + _songs.hashCode()
        return result
    }

    companion object {
        private var autoId = 10001
    }
}