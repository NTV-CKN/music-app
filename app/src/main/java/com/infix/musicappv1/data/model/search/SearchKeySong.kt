package com.infix.musicappv1.data.model.search

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "search_key_songs", indices = [Index(value = ["key"], unique = true)])
data class SearchKeySong(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Int = 0,
    @ColumnInfo("key")
    val key: String = "",
    @ColumnInfo("search_at")
    val searchAt: Long = System.currentTimeMillis()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SearchKeySong) return false

        if (id != other.id) return false
        if (searchAt != other.searchAt) return false
        if (key != other.key) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + searchAt.hashCode()
        result = 31 * result + key.hashCode()
        return result
    }
}