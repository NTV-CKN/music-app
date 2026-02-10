package com.infix.musicappv1.media

import android.util.Log
import androidx.media3.common.AudioAttributes
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.infix.musicappv1.data.model.now_playing.MediaItemTransitionWrap
import com.infix.musicappv1.data.model.recent.SongRecent
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.PlaybackRepository
import com.infix.musicappv1.data.source.local.db.MusicDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaybackService : MediaSessionService() {
    private lateinit var mediaSession: MediaSession
    private lateinit var listener: Player.Listener
    private lateinit var playbackRepository: PlaybackRepository
    private lateinit var serviceScope: CoroutineScope

    //Jobs
    private val supervisorJob = SupervisorJob()
    private var jobInsertRecentSong: Job? = null
    private var jobUpdateSong: Job? = null

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession {
        return mediaSession
    }

    override fun onCreate() {
        super.onCreate()
        val db = MusicDatabase.getInstance(applicationContext)
        playbackRepository = PlaybackRepository.getInstance(
            db.songRecentDao(),
            db.songDao()
        )
        initScope()
        initSessionAndPlayer()
        addListener()
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
        val player = ExoPlayer.Builder(applicationContext)
            .setAudioAttributes(AudioAttributes.DEFAULT, true)
            .build()
        val sessionMediaBuilder = MediaSession.Builder(applicationContext, player)
        mediaSession = sessionMediaBuilder.build()
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
                    playbackRepository.getIndexToPlay() == mediaSession.player.currentMediaItemIndex
                if (isReadyToPlay || !isPlaylistChanged) {
                    playbackRepository.updateMediaTransition(
                        MediaItemTransitionWrap(
                            mediaItem,
                            mediaSession.player.currentMediaItemIndex
                        )
                    )

                    val playlistCurrent = playbackRepository.currentPlaylist.value
                    playlistCurrent?.let { playlist ->
                        val index = mediaSession.player.currentMediaItemIndex
                        if (index > -1 && playlist.songs.size > index) {
                            val song = playlist.songs[index]
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
            song.apply {
                replay++
                counter++
            }
            withContext(Dispatchers.IO) {
                playbackRepository.updateSong(song)
            }
        }
    }
}