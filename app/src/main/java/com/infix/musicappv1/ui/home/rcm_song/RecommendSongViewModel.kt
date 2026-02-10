package com.infix.musicappv1.ui.home.rcm_song

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.song.SongRepository
import com.infix.musicappv1.data.source.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecommendSongViewModel(
    private val songRepository: SongRepository
) : ViewModel() {
    private val _songs = MutableLiveData<List<Song>>()
    val songs: LiveData<List<Song>> = _songs

//    init {
//        loadSongsRemote()
//    }

    fun setSongs(songs: List<Song>) {
        _songs.postValue(songs)
    }

    private fun loadSongs() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = songRepository.loadSongsRemote()
            if (result is Result.Success)
                _songs.postValue(result.data.songs)
            else if (result is Result.Error) {
                _songs.postValue(emptyList())
            }
        }
    }
}