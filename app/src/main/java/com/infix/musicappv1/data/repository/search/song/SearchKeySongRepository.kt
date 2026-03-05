package com.infix.musicappv1.data.repository.search.song

import com.infix.musicappv1.data.model.search.SearchKeySong
import kotlinx.coroutines.flow.Flow

interface SearchKeySongRepository {
    fun getSearchKeySong(limit: Int = 15): Flow<List<SearchKeySong>>
    fun clearAll()
    fun trimSearchKeySong(keepLimit: Int = 15)
}