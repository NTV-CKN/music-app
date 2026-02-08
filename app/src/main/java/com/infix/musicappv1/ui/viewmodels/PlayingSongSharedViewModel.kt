package com.infix.musicappv1.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import com.infix.musicappv1.data.model.now_playing.PlayingSong
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.PlaybackRepository

class PlayingSongSharedViewModel(private val playbackRepository: PlaybackRepository) : ViewModel() {
    val currentPlaylist: LiveData<Playlist?> = playbackRepository.currentPlaylist.asLiveData()
    val indexToPlay: LiveData<Int?> = playbackRepository.indexToPlay.asLiveData().distinctUntilChanged()

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

    fun getMediaItemIndexCurrent() = playbackRepository.getMediaItemIndexCurrent()
    fun getPlaylistTrackCurrent() = playbackRepository.getPlaylistTrackCurrent()
}