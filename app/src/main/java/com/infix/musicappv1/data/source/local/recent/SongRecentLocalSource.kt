package com.infix.musicappv1.data.source.local.recent

import com.infix.musicappv1.data.model.recent.SongRecent
import com.infix.musicappv1.data.source.SongRecentSource
import kotlinx.coroutines.flow.Flow

class SongRecentLocalSource(
    private val songRecentDao: SongRecentDao
) : SongRecentSource.Local {
    override fun loadSongRecentsLocal(limit: Int): Flow<List<SongRecent>?> {
        return songRecentDao.getSongRecents(limit)
    }

    override suspend fun insertToDb(vararg songRecent: SongRecent) {
        songRecentDao.insert(*songRecent)
    }

    override suspend fun trimSongRecent(keepLimit: Int) {
        songRecentDao.trimSongRecents(keepLimit)
    }
}