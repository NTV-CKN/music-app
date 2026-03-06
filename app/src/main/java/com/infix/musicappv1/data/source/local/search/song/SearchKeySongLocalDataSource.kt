package com.infix.musicappv1.data.source.local.search.song

import com.infix.musicappv1.data.model.search.SearchKeySong
import com.infix.musicappv1.data.source.SearchKeySongDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchKeySongLocalDataSource @Inject constructor(
    private val searchKeySongDao: SearchKeySongDao
) : SearchKeySongDataSource.Local {
    override fun getSearchKeySong(limit: Int): Flow<List<SearchKeySong>> {
        return searchKeySongDao.getSearchKeySong(limit)
    }

    override suspend fun clearAll() {
        searchKeySongDao.clear()
    }

    override suspend fun trimSearchKeySong(keepLimit: Int) {
        searchKeySongDao.trimSearchKeySong(keepLimit)
    }

    override suspend fun insert(vararg searchKeySong: SearchKeySong) {
        searchKeySongDao.insert(*searchKeySong)
    }
}