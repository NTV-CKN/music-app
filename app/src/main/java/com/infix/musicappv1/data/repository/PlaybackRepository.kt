package com.infix.musicappv1.data.repository

import com.infix.musicappv1.data.model.now_playing.MediaItemTransitionWrap
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.model.recent.SongRecent
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.source.local.recent.SongRecentDao
import com.infix.musicappv1.enums.PlaylistEnum
import com.infix.musicappv1.media.PlaybackService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.collections.set

class PlaybackRepository private constructor(private val songRecentDao: SongRecentDao) {
    private val _mediaItemTransition: MutableStateFlow<MediaItemTransitionWrap?> =
        MutableStateFlow(null)
    val mediaItemTransition: StateFlow<MediaItemTransitionWrap?> = _mediaItemTransition
    private val _currentPlaylist: MutableStateFlow<Playlist?> = MutableStateFlow<Playlist?>(null)
    val currentPlaylist: StateFlow<Playlist?> = _currentPlaylist
    private val _indexToPlay = MutableStateFlow<Int?>(null)
    val indexToPlay: StateFlow<Int?> = _indexToPlay
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private var mediaItemCurrentIndex: Int? = null

    //guarantee when user comeback app after swipe out
    private var playlistTrackCurrent: Playlist? = null
    private val playlists: MutableMap<String, Playlist> = hashMapOf()

    init {
        PlaylistEnum.entries.forEach { enum ->
            playlists[enum.value] = (Playlist(namePlaylist = enum.value))
        }
    }

    fun updateMediaTransition(
        mediaItemTransitionWrap: MediaItemTransitionWrap?
    ) {

        _mediaItemTransition.value = mediaItemTransitionWrap
        mediaItemTransitionWrap?.index?.let {
            mediaItemCurrentIndex = it
            updateIndexToPlay(it)
        }
    }

    fun getIndexToPlay() = indexToPlay.value

    fun updateIndexToPlay(index: Int) {
        _indexToPlay.value = index
    }

    fun updatePlaylist(playlist: Playlist) {
        _currentPlaylist.value = playlist
        playlistTrackCurrent = playlist
    }

    fun getMediaItemIndexCurrent() = mediaItemCurrentIndex

    fun getPlaylists() = playlists

    fun getPlaylistTrackCurrent() =
        playlistTrackCurrent

    fun setIsPlaying(boolean: Boolean) {
        _isPlaying.value = boolean
    }

    suspend fun insertSongRecent(songRecent: SongRecent) {
        songRecentDao.insert(songRecent)
    }

    companion object {
        @Volatile
        private var instance: PlaybackRepository? = null
        fun getInstance(songRecentDao: SongRecentDao): PlaybackRepository {
            if (instance == null) {
                synchronized(this) {
                }
                if (instance == null)
                    instance = PlaybackRepository(songRecentDao)
            }
            return instance!!
        }
    }
}