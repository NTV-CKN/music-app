package com.infix.musicappv1.data.repository.search.song

import com.infix.musicappv1.data.model.search.SearchKeySong
import com.infix.musicappv1.data.source.SearchKeySongDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchKeySongRepositoryImpl @Inject constructor(
    private val local: SearchKeySongDataSource.Local
): SearchKeySongRepository {
    override fun getSearchKeySong(limit: Int): Flow<List<SearchKeySong>> {
        return local.getSearchKeySong(limit)
    }

    override fun clearAll() {
        local.clearAll()
    }

    override fun trimSearchKeySong(keepLimit: Int) {
        local.trimSearchKeySong(keepLimit)
    }

    override fun insert(vararg searchKeySong: SearchKeySong) {
        local.insert(*searchKeySong)
    }
}