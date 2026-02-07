package com.infix.musicappv1.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.infix.musicappv1.data.model.now_playing.PlayingSong
import com.infix.musicappv1.data.model.now_playing.Playlist
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.PlaybackRepository
import com.infix.musicappv1.enums.PlaylistEnum

class PlayingSongSharedViewModel : ViewModel() {
    private val playlists: MutableMap<String, Playlist> = hashMapOf()

    private val _currentPlaylist = MutableLiveData<Playlist?>()
    val currentPlaylist: LiveData<Playlist?> = _currentPlaylist//update media items
    private val _indexToPlay = MutableLiveData<Int?>()
    val indexToPlay: LiveData<Int?> = _indexToPlay

    val playingSongLivedata: LiveData<PlayingSong?> =
        PlaybackRepository.instance.mediaItemTransition.asLiveData().map { mediaWrap ->
            val playlist = _currentPlaylist.value
            val index = mediaWrap?.index ?: -1

            if (index >= 0 && playlist != null && index < playlist.songs.size) {
                PlayingSong().apply {
                    setIndexCurrent(index)
                    song = (playlist.songs[index])
                    setPlaylist(playlist)
                }
            } else {
                null
            }
        }

    init {
        PlaylistEnum.entries.forEach { enum ->
            playlists[enum.value] = (Playlist(namePlaylist = enum.value))
        }
    }

    fun updatePlaylistCurrent(songs: List<Song>, namePlaylist: String) {
        val playlist = playlists[namePlaylist]
        playlist?.let {
            it.updateSongs(songs)
            playlists[namePlaylist] = it
            _currentPlaylist.value = it
        }
    }

    fun updateIndexToPlay(index: Int) {
        _indexToPlay.value = index
    }
}