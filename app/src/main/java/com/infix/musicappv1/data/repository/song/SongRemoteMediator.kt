package com.infix.musicappv1.data.repository.song

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.model.song.SongRemoteKeys
import com.infix.musicappv1.data.source.local.db.MusicDatabase
import com.infix.musicappv1.data.source.remote.PagingParam
import java.lang.Exception

@OptIn(ExperimentalPagingApi::class)
class SongRemoteMediator(
    private val songRepository: SongRepository,
    private val musicDb: MusicDatabase
) : RemoteMediator<Int, Song>() {

    override suspend fun initialize(): InitializeAction {
        return super.initialize()
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Song>
    ): MediatorResult {
        val numPage = when (loadType) {
            LoadType.REFRESH -> {
                //refresh page current
                val songRemoteKey = getSongRemoteKeyCurrentPosition(state)
                //if next page not null so we get current page, else default is 0
                songRemoteKey?.nextKey?.minus(1) ?: 0
            }

            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                //get last song remote key
                val songRemoteKey = getLastSongRemoteKey(state)
                songRemoteKey?.nextKey ?: return MediatorResult.Success(
                    endOfPaginationReached = true
                )
            }
        }

        return try {
            val songsRemote = songRepository.loadSongsPaging(
                PagingParam(offset = numPage, limit = state.config.pageSize)
            )?.songs ?: emptyList()
            val endOfReach = songsRemote.isEmpty() || songsRemote.size < state.config.pageSize
            musicDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    musicDb.songRemoteKeysDao().clear()
                }
                val nextKey = if (endOfReach) null else numPage + 1
                val prevKey = if (numPage == 0) null else numPage - 1
                val remoteKeys = songsRemote.map { song ->
                    SongRemoteKeys(song.id, prevKey, nextKey)
                }

                musicDb.songRemoteKeysDao().insert(*remoteKeys.toTypedArray())
                songRepository.insert(*songsRemote.toTypedArray())
                MediatorResult.Success(endOfPaginationReached = endOfReach)
            }
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getLastSongRemoteKey(state: PagingState<Int, Song>): SongRemoteKeys? {
        return state.lastItemOrNull()?.let { song ->
            musicDb.songRemoteKeysDao().getSongRemoteKeysById(song.id)
        }
    }

    private suspend fun getSongRemoteKeyCurrentPosition(state: PagingState<Int, Song>): SongRemoteKeys? {
        return state.anchorPosition?.let { pos ->
            state.closestItemToPosition(pos)?.let { song ->
                musicDb.songRemoteKeysDao().getSongRemoteKeysById(song.id)
            }
        }
    }
}