package com.infix.musicappv1.ui.playing

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.infix.musicappv1.data.model.now_playing.PlayingSong
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.repository.PlaybackRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NowPlayingViewModel(
    private val playbackRepository: PlaybackRepository
) : ViewModel() {
    val playlistCurrent: LiveData<Playlist?> = playbackRepository.currentPlaylist.asLiveData()

    val isFavorite: LiveData<Boolean> = playbackRepository.isFavorite.asLiveData()

    val isPlaying: LiveData<Boolean> = playbackRepository.isPlaying.asLiveData()

    val playingSongLivedata: LiveData<PlayingSong?> =
        playbackRepository.mediaItemTransition.asLiveData().map { mediaWrap ->
            val playlist = playbackRepository.currentPlaylist.value
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

    fun updateFavorite(songId: String, isFavorite: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            playbackRepository.updateSongFavorite(songId, isFavorite)
        }
    }

    fun getNamePlaylist() = playlistCurrent.value?.namePlaylist ?: "Unknown"
}