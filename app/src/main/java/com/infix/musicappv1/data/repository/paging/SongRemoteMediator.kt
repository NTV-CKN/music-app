package com.infix.musicappv1.data.repository.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.model.song.SongRemoteKeys
import com.infix.musicappv1.data.model.tracking.TrackingUpdate
import com.infix.musicappv1.data.repository.NetworkRepository
import com.infix.musicappv1.data.repository.song.SongRepository
import com.infix.musicappv1.data.source.local.db.MusicDatabase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class SongRemoteMediator @AssistedInject constructor(
    //when app haven't data, we load N song but use query limit with PagingSource
    //can lead to infinite loop. So we use isLimit to block load next
    @Assisted
    private var isLimit: Boolean,
    private val firestore: FirebaseFirestore,
    private val songRepository: SongRepository,
    private val musicDb: MusicDatabase,
    private val networkRepository: NetworkRepository
) : RemoteMediator<Int, Song>() {

    @AssistedFactory
    interface FactoryAssisted {
        fun create(isLimit: Boolean): SongRemoteMediator
    }

    private var trackLimit = false

    override suspend fun initialize(): InitializeAction {
        val lastSongUpdate = musicDb.trackingUpdateDao().getLastUpdateSongs() ?: 0
        val timeMustUpdate = TimeUnit.HOURS.toMillis(12)
        return if ((System.currentTimeMillis() - lastSongUpdate) >= timeMustUpdate)
            InitializeAction.LAUNCH_INITIAL_REFRESH
        else
            InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Song>
    ): MediatorResult {
        Log.d(
            "SongRemoteMediator",
            "is limit $isLimit, trackLimit == isLimit && isLimit ${trackLimit == isLimit && isLimit}"
        )
        if (trackLimit == isLimit && isLimit)
            return MediatorResult.Success(true)

        trackLimit = true
        if (loadType == LoadType.PREPEND) return MediatorResult.Success(true)
        val isNetwork = networkRepository.hasNetwork.value ?: false
        if (!isNetwork) return MediatorResult.Error(Exception("Not Internet"))
        Log.d(
            "SongRemoteMediator",
            "Load type: " + loadType.name + ", coroutine: ${Thread.currentThread().name}"
        )
        var isEnd = false
        return try {
            Log.d(
                "SongRemoteMediator",
                "" + ", coroutine: ${Thread.currentThread().name}"
            )

            val songRemoteKeyLatest = if (loadType != LoadType.REFRESH)
                musicDb.songRemoteKeysDao().getSongRemoteKeyLastest()
            else null
            val lastSnapshot = songRemoteKeyLatest?.let {
                firestore
                    .collection("songs")
                    .document(it.songId)
                    .get().await()
            }
            val query =
                if (lastSnapshot != null) {//if not load refresh or song remote key not null
                    firestore
                        .collection("songs")
                        .orderBy("id")
                        .startAfter(lastSnapshot)
                        .limit(state.config.pageSize.toLong())
                } else {//else load init
                    firestore
                        .collection("songs")
                        .orderBy("id")
                        .limit(state.config.pageSize.toLong())
                }

            musicDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    musicDb.songRemoteKeysDao().clear()
                    musicDb.songDao().clear()
                }
                val result = query.get().await().toObjects<Song>()
                isEnd = result.isEmpty() || result.size < state.config.pageSize
                val lastOrNullSong = result.lastOrNull()
                if (lastOrNullSong != null) {
                    musicDb.songRemoteKeysDao().insert(
                        SongRemoteKeys(lastOrNullSong.id)
                    )
                    musicDb.trackingUpdateDao().insert(
                        TrackingUpdate(songUpdateAt = System.currentTimeMillis())
                    )
                    songRepository.insert(*result.toTypedArray())
                }
            }
            MediatorResult.Success(endOfPaginationReached = isEnd)
        } catch (e: Exception) {
            Log.d("SongRemoteMediator", e.message ?: "Unknow")
            MediatorResult.Error(e)
        }
    }

//    private suspend fun getLastSongRemoteKey(state: PagingState<Int, Song>): SongRemoteKeys? {
//        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { song ->
////            Log.d("SongRemoteMediator", "song: $song")
//
//            musicDb.songRemoteKeysDao().getSongRemoteKeysById(song.id)
//        }
//    }
//
//    private suspend fun getSongRemoteKeyCurrentPosition(state: PagingState<Int, Song>): SongRemoteKeys? {
//        return state.anchorPosition?.let { pos ->
//            state.closestItemToPosition(pos)?.let { song ->
//                musicDb.songRemoteKeysDao().getSongRemoteKeysById(song.id)
//            }
//        }
//    }
}