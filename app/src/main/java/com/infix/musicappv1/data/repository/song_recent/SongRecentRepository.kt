package com.infix.musicappv1.data.repository.song_recent

import com.infix.musicappv1.data.model.recent.SongRecent
import kotlinx.coroutines.flow.Flow

interface SongRecentRepository {
    fun getSongRecentsDb(limit: Int = 30): Flow<List<SongRecent>?>
    suspend fun insertSongRecentDb(vararg songRecent: SongRecent)
    suspend fun trimSongRecentDb(keepLimit: Int = 30)
}