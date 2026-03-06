package com.infix.musicappv1.data.source

import com.infix.musicappv1.data.model.search.SearchKeySong
import kotlinx.coroutines.flow.Flow

interface SearchKeySongDataSource {
    interface Local {
        fun getSearchKeySong(limit: Int = 15): Flow<List<SearchKeySong>>
        fun clearAll()
        fun trimSearchKeySong(keepLimit: Int = 15)
        fun insert(vararg searchKeySong: SearchKeySong)
    }
}