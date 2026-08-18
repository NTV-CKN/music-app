package com.infix.musicappv1.media

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.HttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.google.firebase.auth.FirebaseAuth
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.now_playing.MediaItemTransitionWrap
import com.infix.musicappv1.data.model.recent.SongRecent
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.PlaybackRepository
import com.infix.musicappv1.ui.playing.now_playing.NowPlayingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class PlaybackService : MediaSessionService() {
    private lateinit var mediaSession: MediaSession
    private lateinit var listener: Player.Listener
    private var tryingRefresh = 0

    @Inject
    lateinit var playbackRepository: PlaybackRepository

    @Inject
    lateinit var firebaseAuthentication: FirebaseAuth
    private lateinit var serviceScope: CoroutineScope
    private lateinit var openNowPlayingPI: PendingIntent

    private lateinit var httpDataSourceFactory: DefaultHttpDataSource.Factory

    //Jobs
    private val supervisorJob = SupervisorJob()
    private var jobInsertRecentSong: Job? = null
    private var jobUpdateSong: Job? = null

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession {
        return mediaSession
    }

    override fun onCreate() {
        super.onCreate()
        initScope()
        initSessionAndPlayer()
        addListener()

        refreshTokenAndApplyToPlayer(false)
        playbackRepository.setRefreshHttpDataSource {
            refreshTokenAndApplyToPlayer(false)
        }
    }


    override fun onDestroy() {
        mediaSession.player.release()
        mediaSession.release()
        supervisorJob.cancel()

        super.onDestroy()
    }

    private fun initScope() {
        serviceScope = CoroutineScope(Dispatchers.Main + supervisorJob)
    }

    private fun initSessionAndPlayer() {
        val player = createExoPlayerWithAuth(baseContext)

        val intent = Intent(applicationContext, NowPlayingActivity::class.java)
        openNowPlayingPI = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val sessionMediaBuilder = MediaSession.Builder(applicationContext, player)
        sessionMediaBuilder.setSessionActivity(openNowPlayingPI)
        mediaSession = sessionMediaBuilder.build()
    }

    @OptIn(UnstableApi::class)
    fun createExoPlayerWithAuth(context: Context): ExoPlayer {
        httpDataSourceFactory = DefaultHttpDataSource.Factory()
            .setAllowCrossProtocolRedirects(true)

        val mediaSourceFactory = ProgressiveMediaSource.Factory(httpDataSourceFactory)

        return ExoPlayer.Builder(context)
            .setMediaSourceFactory(mediaSourceFactory)
            .build()
    }

    //Get token from Auth
    private suspend fun getFirebaseToken(forceRefresh: Boolean): String? {
        return withContext(Dispatchers.IO) {
            try {
                val user = firebaseAuthentication.currentUser ?: return@withContext null
                val result = user.getIdToken(forceRefresh).await()
                result.token
            } catch (e: Exception) {
                Log.e("PlaybackService", "Error fetching token: ${e.message}")
                null
            }
        }
    }

    //Initial httpDataSourceFactory with token
    @OptIn(UnstableApi::class)
    private fun refreshTokenAndApplyToPlayer(
        forceRefresh: Boolean,
        onComplete: ((Boolean) -> Unit)? = null
    ) {
        serviceScope.launch {
            if (tryingRefresh >= 4) {
                tryingRefresh = 0
                onComplete?.invoke(false)
                return@launch
            }

            if (forceRefresh) {
                tryingRefresh++
            }

            val token = getFirebaseToken(forceRefresh)
            if (!token.isNullOrEmpty()) {
                tryingRefresh = 0
                httpDataSourceFactory.setDefaultRequestProperties(
                    mapOf("Authorization" to "Bearer $token")
                )
                onComplete?.invoke(true)
            } else {
                httpDataSourceFactory.setDefaultRequestProperties(emptyMap())
                onComplete?.invoke(false)
            }
        }
    }

    private fun addListener() {
        listener = object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)
                Log.d(
                    "PlaybackService",
                    "onMediaItemTransition-MEDIA_ITEM_TRANSITION_REASON_AUTO: ${reason == Player.MEDIA_ITEM_TRANSITION_REASON_AUTO}"
                )
                Log.d(
                    "PlaybackService",
                    "onMediaItemTransition-MEDIA_ITEM_TRANSITION_REASON_SEEK: ${reason == Player.MEDIA_ITEM_TRANSITION_REASON_SEEK}"
                )
                Log.d(
                    "PlaybackService",
                    "onMediaItemTransition-MEDIA_ITEM_TRANSITION_REASON_REPEAT: ${reason == Player.MEDIA_ITEM_TRANSITION_REASON_REPEAT}"
                )
                Log.d(
                    "PlaybackService",
                    "onMediaItemTransition-MEDIA_ITEM_TRANSITION_REASON_PLAYLIST_CHANGED: ${reason == Player.MEDIA_ITEM_TRANSITION_REASON_PLAYLIST_CHANGED}"
                )
                Log.d(
                    "PlaybackService",
                    "onMediaItemTransition-current media item index: ${mediaSession.player.currentMediaItemIndex}"
                )
                //we will update playing song and write db when media item is not MEDIA_ITEM_TRANSITION_REASON_PLAYLIST_CHANGED
                val isPlaylistChanged =
                    reason == Player.MEDIA_ITEM_TRANSITION_REASON_PLAYLIST_CHANGED
                //when click song, callback will be call two time
                val isReadyToPlay =
                    playbackRepository.getIndexToPlay()?.indexToPlay == mediaSession.player.currentMediaItemIndex
                if (isReadyToPlay || !isPlaylistChanged) {
                    Log.d("PlaybackService", "Accept change")
                    serviceScope.launch {
                        playbackRepository.updateMediaTransition(
                            MediaItemTransitionWrap(
                                mediaItem,
                                mediaSession.player.currentMediaItemIndex
                            )
                        )
                    }

                    val playlistCurrent = playbackRepository.currentPlaylist.value
                    playlistCurrent?.let { playlist ->
                        val index = mediaSession.player.currentMediaItemIndex
                        if (index > -1 && playlist.songsObject.size > index) {
                            val song = playlist.songsObject[index]
                            writeRecentSongToDb(song)
                            increaseReplayAndCounter(song)
                        }
                    }
                }
            }//end onMediaItemTransition

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                playbackRepository.setIsPlaying(isPlaying)
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_IDLE -> Log.d(
                        "SVU",
                        "Player đang IDLE - Có thể do chưa gọi prepare() hoặc bị stop"
                    )

                    Player.STATE_BUFFERING -> Log.d(
                        "SVU",
                        "Player đang BUFFERING - Đang đợi nạp nhạc..."
                    )

                    Player.STATE_READY -> Log.d("SVU", "Player đã SẴN SÀNG")
                    Player.STATE_ENDED -> Log.d("SVU", "Player đã HÁT XONG")
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                val cause = error.cause
                if (cause is HttpDataSource.InvalidResponseCodeException) {
                    when (cause.responseCode) {
                        401 -> {
                            Log.w(
                                "PlaybackService",
                                "Token 401 Expired. Attempting auto-refresh..."
                            )
                            refreshTokenAndApplyToPlayer(forceRefresh = true) { success ->
                                if (success) {
                                    mediaSession.player.prepare()
                                    mediaSession.player.play()
                                } else {
                                    mediaSession.player.stop()
                                }
                            }
                        }

                        403 -> {
                            if (mediaSession.player.hasNextMediaItem()) {
                                mediaSession.player.seekToNext()
                                mediaSession.player.prepare()
                                mediaSession.player.play()
                            } else {
                                Toast.makeText(
                                    baseContext,
                                    getString(R.string.msg_vip_required_for_song),
                                    Toast.LENGTH_SHORT
                                ).show()
                                mediaSession.player.stop()
                            }
                        }

                        else -> {
                            if (mediaSession.player.hasNextMediaItem()) {
                                mediaSession.player.seekToNext()
                                mediaSession.player.prepare()
                                mediaSession.player.play()
                            } else {
                                mediaSession.player.stop()
                            }
                            Log.e("PlaybackService", "Http Error Code: ${cause.responseCode}")
                        }
                    }
                } else {
                    Log.e("PlaybackService", "Player Error: ${error.message}")
                }
            }
        }

        mediaSession.player.addListener(listener)
    }

    private fun writeRecentSongToDb(song: Song) {
        jobInsertRecentSong?.cancel()
        jobInsertRecentSong = serviceScope.launch {
            delay(4000)
            withContext(Dispatchers.IO) {
                playbackRepository.insertSongRecent(SongRecent.Builder(song).build())
            }
        }
    }

    private fun increaseReplayAndCounter(song: Song) {
        jobUpdateSong?.cancel()
        jobUpdateSong = serviceScope.launch {
            delay(4000)
//            song.apply {
//                replay++
//                counter++
//            }
            withContext(Dispatchers.IO) {
                playbackRepository.updateSong(song.id, song.replay + 1, song.counter + 1)
            }
        }
    }
}