package com.infix.musicappv1.data.repository.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.model.album.AlbumRemoteKeys
import com.infix.musicappv1.data.model.tracking.TrackingUpdate
import com.infix.musicappv1.data.repository.album.AlbumRepository
import com.infix.musicappv1.data.source.local.db.MusicDatabase
import com.infix.musicappv1.data.source.remote.param.PagingParam
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class AlbumRemoteMediator(
    private val albumRepository: AlbumRepository,
    private val musicDb: MusicDatabase
) : RemoteMediator<Int, Album>() {
    override suspend fun initialize(): InitializeAction {
        val lastAlbumUpdate = musicDb.trackingUpdateDao().getLastUpdateAlbum() ?: 0
        val timeMustUpdate = TimeUnit.HOURS.toMillis(12)
        return if ((System.currentTimeMillis() - lastAlbumUpdate) >= timeMustUpdate)
            InitializeAction.LAUNCH_INITIAL_REFRESH
        else
            InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Album>
    ): MediatorResult {
        val numPage = when (loadType) {
            LoadType.REFRESH -> {
                getRemoteKeysCurrent(state)?.nextKey?.minus(1) ?: 0
            }

            LoadType.PREPEND -> return MediatorResult.Success(true)
            LoadType.APPEND -> {
                getRemoteKeysForLast(state)?.nextKey ?: return MediatorResult.Success(true)
            }
        }

        return try {
            val albums = albumRepository.loadAlbumsPaging(
                PagingParam(
                    offset = numPage * state.config.pageSize,
                    limit = state.config.pageSize
                )
            ) ?: emptyList()

            val endOfReach = albums.isEmpty() || albums.size < state.config.pageSize
            val prevKey = if (numPage == 0) null else numPage - 1
            val nextKey = if (endOfReach) null else numPage + 1
            musicDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    musicDb.albumRemoteKeysDao().clear()
                    musicDb.albumDao().clear()
                }

                val remoteKeys = albums.map {
                    AlbumRemoteKeys(
                        it.id,
                        prevKey,
                        nextKey
                    )
                }

                musicDb.albumRemoteKeysDao().insert(*remoteKeys.toTypedArray())
                musicDb.albumDao().insert(*albums.toTypedArray())
                musicDb.trackingUpdateDao().insert(
                    TrackingUpdate(albumUpdateAt = System.currentTimeMillis())
                )
            }

            MediatorResult.Success(endOfReach)
        } catch (ex: Exception) {
            MediatorResult.Error(ex)
        }

    }

    private suspend fun getRemoteKeysCurrent(state: PagingState<Int, Album>): AlbumRemoteKeys? {
        return state.anchorPosition?.let {
            val album = state.closestItemToPosition(it)
            album?.let { alb ->
                musicDb.albumRemoteKeysDao().getAlbumRemoteKeysByAlbumId(alb.id)
            }
        }
    }

    private suspend fun getRemoteKeysForLast(state: PagingState<Int, Album>): AlbumRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { album ->
            musicDb.albumRemoteKeysDao().getAlbumRemoteKeysByAlbumId(album.id)
        }
    }
}