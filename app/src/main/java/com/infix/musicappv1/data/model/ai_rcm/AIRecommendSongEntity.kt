package com.infix.musicappv1.data.model.ai_rcm

import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.infix.musicappv1.data.model.song.Song

@Entity(tableName = "ai_rcm_songs")
data class AIRecommendSongEntity(
    @PrimaryKey
    val id: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "ai_message")
    val aiMessage: String = "",
    @ColumnInfo(name = "prompt_summary")
    val promptSummary: String = "",
    @ColumnInfo(name = "songs_json")
    var songsJson: String = ""
) {
    @Ignore
    val gson: Gson = Gson()

    fun songsListToJson(songs: List<Song>) {
        try {
            songsJson = gson.toJson(songs)
        } catch (e: Exception) {
            songsJson = ""
            Log.e("AIRecommendSongEntity", "songsListToJson: ${e.message ?: "Unknown"}")
        }
    }

    fun songsJsonToList(): List<Song> {
        if (songsJson.isEmpty()) return emptyList()
        try {
            val listType = object : TypeToken<List<Song>>() {}.type

            return gson.fromJson<List<Song>>(songsJson, listType) ?: emptyList()
        } catch (e: Exception) {
            Log.e("AIRecommendSongEntity", "songsJsonToList: ${e.message ?: "Unknown"}")
            return emptyList()
        }
    }
}