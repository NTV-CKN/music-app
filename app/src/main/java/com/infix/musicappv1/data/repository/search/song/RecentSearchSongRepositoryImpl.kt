package com.infix.musicappv1.data.repository.search.song

import com.infix.musicappv1.data.model.search.RecentSearchSong
import com.infix.musicappv1.data.source.RecentSearchSongDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecentSearchSongRepositoryImpl @Inject constructor(
    private val local: RecentSearchSongDataSource.Local
) : RecentSearchSongRepository {
    override fun getRecentSearchSong(limit: Int): Flow<List<RecentSearchSong>> {
        return local.getRecentSearchSong(limit)
    }

    override fun clearAll() {
        local.clearAll()
    }

    override fun trimRecentSearchSong(keepLimit: Int) {
        local.trimRecentSearchSong(keepLimit)
    }
}