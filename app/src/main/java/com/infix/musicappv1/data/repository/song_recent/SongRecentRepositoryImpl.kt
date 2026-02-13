package com.infix.musicappv1.data.repository.song_recent

import com.infix.musicappv1.data.model.recent.SongRecent
import com.infix.musicappv1.data.source.local.recent.SongRecentLocalSource
import kotlinx.coroutines.flow.Flow

class SongRecentRepositoryImpl(
    private val localRecent: SongRecentLocalSource
) : SongRecentRepository {
    override fun getSongRecentsDb(limit: Int): Flow<List<SongRecent>?> {
        return localRecent.loadSongRecentsLocal(limit)
    }

    override suspend fun insertSongRecentDb(vararg songRecent: SongRecent) {
        localRecent.insertToDb(*songRecent)
    }

    override suspend fun trimSongRecentDb(keepLimit: Int) {
        localRecent.trimSongRecent(keepLimit)
    }
}