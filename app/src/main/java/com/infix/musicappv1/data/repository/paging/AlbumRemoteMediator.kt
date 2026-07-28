package com.infix.musicappv1.data.repository.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.model.album.AlbumRemoteKeys
import com.infix.musicappv1.data.model.tracking.TrackingUpdate
import com.infix.musicappv1.data.repository.NetworkRepository
import com.infix.musicappv1.data.repository.album.AlbumRepository
import com.infix.musicappv1.data.source.local.db.MusicDatabase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class AlbumRemoteMediator @AssistedInject constructor(
    @Assisted
    private val isLimit: Boolean,
    val albumRepository: AlbumRepository,
    private val musicDb: MusicDatabase,
    private val networkRepository: NetworkRepository
) : RemoteMediator<Int, Album>() {

    @AssistedFactory
    interface FactoryAssisted {
        fun create(isLimit: Boolean): AlbumRemoteMediator
    }

    private var trackLimit = false

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
        val isNetwork = networkRepository.hasNetwork.value ?: false
        if (!isNetwork) return MediatorResult.Error(Exception("Not Internet"))
        if(LoadType.PREPEND == loadType)
            return MediatorResult.Success(true)

        if(isLimit && trackLimit)
            return MediatorResult.Success(true)


        return try {
            if (loadType == LoadType.REFRESH) {
                musicDb.albumRemoteKeysDao().clear()
                musicDb.albumDao().clear()
            }
            val albumsCollection = Firebase.firestore.collection("albums")
            val lastDocument = musicDb.albumRemoteKeysDao().getAlbumRemoteKeyLatest()?.let { key ->
                albumsCollection.document(key.albumId).get().await()
            }

            val query = if (lastDocument != null) {
                albumsCollection
                    .startAfter(lastDocument)
                    .limit(state.config.pageSize.toLong())
            } else {
                albumsCollection
                    .limit(state.config.pageSize.toLong())
            }

            val result = query.get().await().toObjects<Album>()
            val endReached = result.isEmpty() || result.size < state.config.pageSize

            musicDb.withTransaction {
                result.lastOrNull()?.let { album ->
                    musicDb.albumRemoteKeysDao().insert(
                        AlbumRemoteKeys(album.id)
                    )
                    musicDb.trackingUpdateDao().insert(
                        TrackingUpdate(
                            albumUpdateAt = System.currentTimeMillis()
                        )
                    )
                }

                musicDb.albumDao().insert(*result.toTypedArray())
            }

            trackLimit = true
            MediatorResult.Success(endReached)
        } catch (ex: Exception) {
            MediatorResult.Error(ex)
        }
    }
}