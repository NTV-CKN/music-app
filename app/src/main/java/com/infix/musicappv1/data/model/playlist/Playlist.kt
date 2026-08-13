package com.infix.musicappv1.data.model.playlist

import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
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
    var playlistId: Long = autoId++.toLong(),
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
    var songsId: List<String> = emptyList(),
    @Ignore
    //fix error logic click song recent but not update playlist
    var playAt: Long = System.currentTimeMillis()
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
        songs.forEach { song ->
            val mediaItem = MediaItem.Builder()
                .setUri(song.source)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setArtworkUri(song.image.toUri())
                        .build()
                ).build()
            this.mediaItems.add(mediaItem)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as Playlist

        if (playlistId != other.playlistId) return false
        if (playAt != other.playAt) return false
        if (_songsObject != other._songsObject) return false

        return true
    }

    override fun hashCode(): Int {
        var result = playlistId
        result = 31 * result + playAt.hashCode()
        result = 31 * result + _songsObject.hashCode()
        return result.toInt()
    }


    companion object {
        //Playlist user create start at 50000 (we save next id into datastore)
        //Playlist system start at 10000
        //Playlist for multiple screen of app start at 1 (ref to PlaylistEnum)
        private var autoId = 1
    }
}