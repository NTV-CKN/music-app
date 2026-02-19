package com.infix.musicappv1.data.source

import com.infix.musicappv1.data.model.recent.SongRecent
import kotlinx.coroutines.flow.Flow

interface SongRecentDataSource {
    interface Local {
        fun loadSongRecentsLocal(limit: Int = 30): Flow<List<SongRecent>?>
        suspend fun insertToDb(vararg songRecent: SongRecent)
        suspend fun trimSongRecent(keepLimit: Int = 30)
    }

    interface Remote {

    }
}