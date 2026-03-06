package com.infix.musicappv1.data.repository.search.song

import com.infix.musicappv1.data.model.search.RecentSearchSong
import kotlinx.coroutines.flow.Flow

interface RecentSearchSongRepository {
    fun getRecentSearchSong(limit: Int = 30): Flow<List<RecentSearchSong>>
    fun clearAll()
    fun trimRecentSearchSong(keepLimit: Int = 30)
    fun insert(vararg recentSearchSong: RecentSearchSong)
}