package com.infix.musicappv1.data.repository.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.data.model.artist.ArtistRemoteKeys
import com.infix.musicappv1.data.model.tracking.TrackingUpdate
import com.infix.musicappv1.data.repository.NetworkRepository
import com.infix.musicappv1.data.repository.artist.ArtistRepository
import com.infix.musicappv1.data.source.local.db.MusicDatabase
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class ArtistRemoteMediator(
    private val isLimit: Boolean,
    private val artistRepository: ArtistRepository,
    private val musicDb: MusicDatabase,
    private val networkRepository: NetworkRepository
) : RemoteMediator<Int, Artist>() {
    private var trackLimit = false

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
        val isNetwork = networkRepository.hasNetwork.value ?: false
        if (!isNetwork) return MediatorResult.Error(Exception("Not Internet"))

        if (LoadType.PREPEND == loadType)
            return MediatorResult.Success(true)

        if (isLimit && trackLimit)
            return MediatorResult.Success(true)

        return try {
            if (loadType == LoadType.REFRESH) {
                musicDb.artistRemoteKeysDao().clear()
                musicDb.artistDao().clear()
            }
            val artistsCollection = Firebase.firestore.collection("artists")
            val lastDocument =
                musicDb.artistRemoteKeysDao().getArtistRemoteKeyLastest()?.let { key ->
                    artistsCollection.document(key.artistId.toString()).get().await()
                }

            val query = if (lastDocument != null) {
                artistsCollection
                    .startAfter(lastDocument)
                    .limit(state.config.pageSize.toLong())
            } else {
                artistsCollection
                    .limit(state.config.pageSize.toLong())
            }

            val result = query.get().await().toObjects<Artist>()
            val endReached = result.isEmpty() || result.size < state.config.pageSize

            musicDb.withTransaction {
                result.lastOrNull()?.let { artist ->
                    musicDb.artistRemoteKeysDao().insert(
                        ArtistRemoteKeys(artist.id)
                    )
                    musicDb.trackingUpdateDao().insert(
                        TrackingUpdate(
                            artistUpdateAt = System.currentTimeMillis()
                        )
                    )
                }

                musicDb.artistDao().insert(*result.toTypedArray())
            }

            trackLimit = true
            MediatorResult.Success(endReached)
        } catch (ex: Exception) {
            MediatorResult.Error(ex)
        }
    }
}