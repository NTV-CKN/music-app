package com.infix.musicappv1.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.infix.musicappv1.data.model.now_playing.PlayingSong
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.PlaybackRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayingSongSharedViewModel(private val playbackRepository: PlaybackRepository) : ViewModel() {
    val currentPlaylist: LiveData<Playlist?> = playbackRepository.currentPlaylist.asLiveData()
    val indexToPlay: LiveData<PlaybackRepository.IndexToPlayDate?> =
        playbackRepository.indexToPlay.asLiveData()

    val playingSongLivedata: LiveData<PlayingSong?> =
        playbackRepository.mediaItemTransition.asLiveData().map { mediaWrap ->
            val playlist = currentPlaylist.value
            val index = mediaWrap?.index ?: -1

            if (index >= 0 && playlist != null && index < playlist.songsObject.size) {
                val songTmp = (playlist.songsObject[index])
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

    fun updatePlaylistCurrent(songs: List<Song>, playlistT: Playlist) {
        var playlist = playbackRepository.getPlaylists()[playlistT.namePlaylist]
        if (playlist == null) {
            playbackRepository.getPlaylists()[playlistT.namePlaylist] = playlistT
            playlist = playlistT
        }

        playlist.updateSongs(songs)
        playbackRepository.getPlaylists()[playlistT.namePlaylist] = playlist
        playbackRepository.updatePlaylist(playlist)
    }

    fun updateIndexToPlay(index: Int) {
        playbackRepository.updateIndexToPlay(index)
    }

    fun updateSongFavorite(id: String, isFavorite: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            playbackRepository.updateSongFavorite(id, isFavorite)
        }
    }

    fun setIsDataReady(boolean: Boolean) {
        _isDataReady.value = boolean
    }

    //We only restore when value of currentPlaylist and indexToPlay is NULL
    //Notice: Now I'm still not store playlist, so this func not stable yet
    fun restorePrevSession(songId: String?, playlistId: Int?) {
        //check value of stateflow is null
        if (currentPlaylist.value == null && indexToPlay.value == null && playlistId != null && songId != null) {
            viewModelScope.launch(Dispatchers.IO) {
                //retrieve data of name playlist under room db, after select get songsObject
                val playlistWithSongs = playbackRepository.getPlaylistWithSongs(playlistId)
                //set current playlist and index to play
                if (playlistWithSongs != null) {
                    val playlistCurrent = playlistWithSongs.playlist
                    val songs = playlistWithSongs.songs
                    playlistCurrent.updateSongs(songs)
                    val indexToPlay = songs.indexOfFirst { song -> song.id == songId }

                    withContext(Dispatchers.Main) {
//                        MiniPlayerFragment.trackOldPlaylist = playlistCurrent
                        playbackRepository.updatePlaylist(playlistCurrent)
                        playbackRepository.updateIndexToPlay(indexToPlay)
                    }
                }
            }

        }
    }

    fun getMediaItemIndexCurrent() = playbackRepository.getMediaItemIndexCurrent()
    fun getPlaylistTrackCurrent() = playbackRepository.getPlaylistTrackCurrent()
}