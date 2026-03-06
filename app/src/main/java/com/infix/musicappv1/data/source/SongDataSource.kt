package com.infix.musicappv1.data.source

import androidx.paging.PagingSource
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.model.song.SongList
import com.infix.musicappv1.data.source.remote.param.PagingParam
import kotlinx.coroutines.flow.Flow

interface SongDataSource {
    interface Remote {
        suspend fun loadSongs(pagingParam: PagingParam): SongList?
    }

    interface Local {
        suspend fun update(vararg song: Song)
        suspend fun insert(vararg song: Song)
        suspend fun delete(vararg song: Song)
        fun getAllSongsFlow(): Flow<List<Song>>
        fun getTop15SongMostHeard(): Flow<List<Song>>
        fun getTop40SongMostHeard(): Flow<List<Song>>
        fun getAllSongsPaging(): PagingSource<Int, Song>
        fun getNSongsPaging(limit: Int = 10): PagingSource<Int, Song>
        fun getSongsFavorite(): Flow<List<Song>>
        fun getSongsFavoriteWithLimit(limit: Int = 10): Flow<List<Song>>
        suspend fun updateSongFavorite(id: String, isFavorite: Boolean)
        suspend fun getSongsByNameSongOrNameArtist(key: String): List<Song>
    }
}