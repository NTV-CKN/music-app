package com.infix.musicappv1.data.repository.search.song

import com.infix.musicappv1.data.model.search.RecentSearchSong
import kotlinx.coroutines.flow.Flow

interface RecentSearchSongRepository {
    fun getRecentSearchSong(limit: Int = 30): Flow<List<RecentSearchSong>>
    suspend fun clearAll()
    suspend fun trimRecentSearchSong(keepLimit: Int = 30)
    suspend fun insert(vararg recentSearchSong: RecentSearchSong)
}