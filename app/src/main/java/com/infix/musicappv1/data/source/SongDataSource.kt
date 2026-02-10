package com.infix.musicappv1.data.source

import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.model.song.SongList
import kotlinx.coroutines.flow.Flow

interface SongDataSource {
    interface Remote {
        suspend fun loadSongs(): Result<SongList>
    }

    interface Local {
        suspend fun update(vararg song: Song)
        suspend fun insert(vararg song: Song)
        suspend fun delete(vararg song: Song)
        suspend fun getAllSongs(): List<Song>
        fun getAllSongsFlow(): Flow<List<Song>>
        fun getSongsFavorite(): Flow<List<Song>>
        suspend fun updateSongFavorite(id: String, isFavorite: Boolean)
    }
}