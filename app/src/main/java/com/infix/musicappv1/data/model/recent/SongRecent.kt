package com.infix.musicappv1.data.model.recent

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.infix.musicappv1.data.model.song.Song
import java.util.Date

@Entity("song_recents")
data class SongRecent(
    @ColumnInfo("play_at")
    var playAt: Date? = null
) : Song() {
    class Builder(private val song: Song) {
        fun build() = SongRecent().apply {
            this.id = song.id
            this.title = song.title
            this.album = song.album
            this.artist = song.artist
            this.source = song.source
            this.image = song.image
            this.duration = song.duration
            this.favorite = song.favorite
            this.counter = song.counter
            this.replay = song.replay
            this.playAt = Date()
        }
    }
}