package com.infix.musicappv1.data.repository

import android.util.Log
import com.infix.musicappv1.data.model.now_playing.MediaItemTransitionWrap
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.model.recent.SongRecent
import com.infix.musicappv1.data.source.local.recent.SongRecentDao
import com.infix.musicappv1.data.source.local.song.SongDao
import com.infix.musicappv1.enums.PlaylistEnum
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlin.collections.set

class PlaybackRepository private constructor(
    private val songRecentDao: SongRecentDao,
    private val songDao: SongDao
) {
    //observe song transition
    private val _mediaItemTransition: MutableStateFlow<MediaItemTransitionWrap?> =
        MutableStateFlow(null)
    val mediaItemTransition: StateFlow<MediaItemTransitionWrap?> = _mediaItemTransition

    //observe current playlist when song clicked at any playlist
    private val _currentPlaylist: MutableStateFlow<Playlist?> = MutableStateFlow<Playlist?>(null)
    val currentPlaylist: StateFlow<Playlist?> = _currentPlaylist

    //observe index to play when PlayerMedia recall mediaItemTransition
    private val _indexToPlay = MutableStateFlow<Int?>(null)
    val indexToPlay: StateFlow<Int?> = _indexToPlay

    //observe status Player isplaying
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    //observe song favorite
    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

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
            //when user not in app and comeback, this code help miniplayer update correct song on notification media
            updateIndexToPlay(it)
            updateIsFavorite()
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

    suspend fun updateSongFavorite(id: String, isFavorite: Boolean) {
        songDao.updateFavorite(id, isFavorite)
        //if current song playing is favorite, we need update UI for that
        //else we only write favorite for song id and non update UI
        _indexToPlay.value?.let { indexToPlay ->
            val currentSong = playlistTrackCurrent?.songs?.getOrNull(indexToPlay)
            if (currentSong != null && currentSong.id == id)
                _isFavorite.update { isFavorite }
//            Log.d("SVU", isFavorite.toString())
        }
    }

    private fun updateIsFavorite() {
        val song = playlistTrackCurrent?.songs?.getOrNull(_indexToPlay.value ?: -1)
        song?.let {
            _isFavorite.value = song.favorite
        }
    }

    companion object {
        @Volatile
        private var instance: PlaybackRepository? = null
        fun getInstance(songRecentDao: SongRecentDao, songDao: SongDao): PlaybackRepository {
            if (instance == null) {
                synchronized(this) {
                }
                if (instance == null)
                    instance = PlaybackRepository(songRecentDao, songDao)
            }
            return instance!!
        }
    }
}