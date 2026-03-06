package com.infix.musicappv1.data.source

import com.infix.musicappv1.data.model.search.SearchKeySong
import kotlinx.coroutines.flow.Flow

interface SearchKeySongDataSource {
    interface Local {
        fun getSearchKeySong(limit: Int = 15): Flow<List<SearchKeySong>>
        suspend fun clearAll()
        suspend fun trimSearchKeySong(keepLimit: Int = 15)
        suspend fun insert(vararg searchKeySong: SearchKeySong)
    }
}