package com.infix.musicappv1.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.infix.musicappv1.data.model.now_playing.PlayingSong
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.PlaybackRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayingSongSharedViewModel(private val playbackRepository: PlaybackRepository) : ViewModel() {
    val currentPlaylist: LiveData<Playlist?> = playbackRepository.currentPlaylist.asLiveData()
    val indexToPlay: LiveData<Int?> =
        playbackRepository.indexToPlay.asLiveData().distinctUntilChanged()

    val playingSongLivedata: LiveData<PlayingSong?> =
        playbackRepository.mediaItemTransition.asLiveData().map { mediaWrap ->
            val playlist = currentPlaylist.value
            val index = mediaWrap?.index ?: -1

            if (index >= 0 && playlist != null && index < playlist.songs.size) {
                val songTmp = (playlist.songs[index])
                PlayingSong().apply {
                    setIndexCurrent(index)
                    song = songTmp
                    setPlaylist(playlist)
                }
            } else {
                null
            }
        }

    private val _isDataReady = MutableLiveData(false)
    val isDataReady: LiveData<Boolean> = _isDataReady

    init {
//        val songCurrent = currentPlaylist.value?.songs?.getOrNull(indexToPlay.value ?: -1)
//        _isRestoreSession.value = songCurrent == null
    }

    fun updatePlaylistCurrent(songs: List<Song>, namePlaylist: String) {
        val playlist = playbackRepository.getPlaylists()[namePlaylist]
        playlist?.let {
            it.updateSongs(songs)
            playbackRepository.getPlaylists()[namePlaylist] = it
            playbackRepository.updatePlaylist(it)
        }
    }

    fun updateIndexToPlay(index: Int) {
        playbackRepository.updateIndexToPlay(index)
    }

    fun updateSongFavorite(id: String, isFavorite: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            playbackRepository.updateSongFavorite(id, isFavorite)
        }
    }

    //We only restore when value of currentPlaylist and indexToPlay is NULL
    //Notice: Now I'm still not store playlist, so this func not stable yet
    fun restorePrevSession(songId: String?, namePlaylist: String?) {
        //check value of stateflow is null
        //retrieve data of name playlist under room db, after select get songs
        //set current playlist and index to play
    }

    fun getMediaItemIndexCurrent() = playbackRepository.getMediaItemIndexCurrent()
    fun getPlaylistTrackCurrent() = playbackRepository.getPlaylistTrackCurrent()
}