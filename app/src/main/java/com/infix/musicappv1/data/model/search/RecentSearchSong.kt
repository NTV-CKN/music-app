package com.infix.musicappv1.data.model.search

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.infix.musicappv1.data.model.song.Song

@Entity(tableName = "recent_search_songs")
data class RecentSearchSong(
    @ColumnInfo("play_at")
    val playAt: Long = System.currentTimeMillis()
) : Song() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RecentSearchSong) return false
        if (!super.equals(other)) return false

        if (playAt != other.playAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + playAt.hashCode()
        return result
    }
}
