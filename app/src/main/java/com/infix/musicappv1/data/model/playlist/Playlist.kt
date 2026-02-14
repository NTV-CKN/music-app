package com.infix.musicappv1.data.model.playlist

import androidx.media3.common.MediaItem
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.infix.musicappv1.data.model.song.Song
import java.util.Date

@Entity(tableName = "playlists", indices = [Index(value = ["name"], unique = true)])
data class Playlist(
    @PrimaryKey
    @ColumnInfo("playlist_id")
    @SerializedName("id")
    var playlistId: Int = autoId++,
    @ColumnInfo("name")
    @SerializedName("name")
    var namePlaylist: String = "",
    @ColumnInfo("artwork")
    @SerializedName("artwork")
    var artwork: String = "",
    @ColumnInfo("create_at")
    var createdAt: Date? = null,
    @ColumnInfo("is_custom")
    var isCustom: Boolean = false,
    @Ignore
    @SerializedName("songs")
    var songsId: List<String> = emptyList()
) {
    @Ignore
    private val _songsObject: MutableList<Song> = mutableListOf()

    @Ignore
    val songsObject: List<Song> = _songsObject

    @Ignore
    private val mediaItems: MutableList<MediaItem> = mutableListOf()

    fun updateSongs(songs: List<Song>) {
        this._songsObject.clear()
        this._songsObject.addAll(songs)
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

        if (playlistId != other.playlistId) return false
        if (_songsObject != other._songsObject) return false

        return true
    }

    override fun hashCode(): Int {
        var result = playlistId
        result = 31 * result + _songsObject.hashCode()
        return result
    }

    companion object {
        //Playlist user create start at 50000 (we save next id into datastore)
        //Playlist system start at 10000
        //Playlist for multiple screen of app start at 1 (ref to PlaylistEnum)
        private var autoId = 1
    }
}