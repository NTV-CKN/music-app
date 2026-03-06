package com.infix.musicappv1.data.source.local.search.song

import com.infix.musicappv1.data.model.search.RecentSearchSong
import com.infix.musicappv1.data.source.RecentSearchSongDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecentSearchSongLocalDataSource @Inject constructor(
    private val recentSearchSongDao: RecentSearchSongDao
): RecentSearchSongDataSource.Local {
    override fun getRecentSearchSong(limit: Int): Flow<List<RecentSearchSong>> {
        return recentSearchSongDao.getRecentSearchSong(limit)
    }

    override fun clearAll() {
        recentSearchSongDao.clear()
    }

    override fun trimRecentSearchSong(keepLimit: Int) {
        recentSearchSongDao.trimRecentSearchSong(keepLimit)
    }

    override fun insert(vararg recentSearchSong: RecentSearchSong) {
        recentSearchSongDao.insert(*recentSearchSong)
    }
}