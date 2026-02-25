package com.infix.musicappv1.data.repository.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.data.model.artist.ArtistRemoteKeys
import com.infix.musicappv1.data.model.tracking.TrackingUpdate
import com.infix.musicappv1.data.repository.artist.ArtistRepository
import com.infix.musicappv1.data.source.local.db.MusicDatabase
import com.infix.musicappv1.data.source.remote.PagingParam
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class ArtistRemoteMediator(
    private val artistRepository: ArtistRepository,
    private val musicDb: MusicDatabase
) : RemoteMediator<Int, Artist>() {
    override suspend fun initialize(): InitializeAction {
        val lastArtistUpdate = musicDb.trackingUpdateDao().getLastUpdateArtist() ?: 0
        val timeMustUpdate = TimeUnit.HOURS.toMillis(12)
        return if ((System.currentTimeMillis() - lastArtistUpdate) >= timeMustUpdate)
            InitializeAction.LAUNCH_INITIAL_REFRESH
        else
            InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Artist>
    ): MediatorResult {
        val numPage: Int = when (loadType) {
            LoadType.REFRESH -> {
                getRemoteKeysCurrent(state)?.nextKey?.minus(1) ?: 0
            }

            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeysForLast(state)
                remoteKeys?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
        }

        return try {
            val artists = artistRepository.loadArtistsPaging(
                PagingParam(offset = numPage * state.config.pageSize, limit = state.config.pageSize)
            ) ?: emptyList()

            val endOfReach = artists.isEmpty() || artists.size < state.config.pageSize
            val prevKey = if (numPage == 0) null else numPage - 1
            val nextKey = if (endOfReach) null else numPage + 1
            musicDb.withTransaction {
                if(loadType == LoadType.REFRESH) {
                    musicDb.artistDao().clear()
                    musicDb.artistRemoteKeysDao().clear()
                }

                val remoteKeys = artists.map {
                    ArtistRemoteKeys(
                        it.id,
                        prevKey,
                        nextKey
                    )
                }

                musicDb.artistDao().insert(*artists.toTypedArray())
                musicDb.artistRemoteKeysDao().insert(*remoteKeys.toTypedArray())
                musicDb.trackingUpdateDao().insert(
                    TrackingUpdate(
                        songUpdateAt = 0,
                        artistUpdateAt = System.currentTimeMillis()
                    )
                )
            }
            MediatorResult.Success(endOfReach)
        } catch (ex: Exception) {
            MediatorResult.Error(ex)
        }
    }

    suspend fun getRemoteKeysForLast(state: PagingState<Int, Artist>): ArtistRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { artist ->
            musicDb.artistRemoteKeysDao().getArtistRemoteKeysByArtistId(artist.id)
        }
    }

    suspend fun getRemoteKeysCurrent(state: PagingState<Int, Artist>): ArtistRemoteKeys? {
        return state.anchorPosition?.let { pos ->
            val artist = state.closestItemToPosition(pos)
            artist?.let { musicDb.artistRemoteKeysDao().getArtistRemoteKeysByArtistId(it.id) }
        }
    }
}