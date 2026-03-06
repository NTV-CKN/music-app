package com.infix.musicappv1.ui.search.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infix.musicappv1.data.model.search.RecentSearchSong
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.search.song.RecentSearchSongRepository
import com.infix.musicappv1.data.repository.song.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultSearchSongViewModel @Inject constructor(
    private val resultSearchSongRepository: RecentSearchSongRepository,
    private val songRepository: SongRepository
) : ViewModel() {
    private val _key = MutableLiveData<String?>(null)
    val key: LiveData<String?> = _key

    private val _songs = MutableLiveData<List<Song>?>(null)
    val songs: LiveData<List<Song>?> = _songs

    //this func will set value for _key, in ResultSearchSongFragment observe and load song with key
    fun setKeySearch(key: String) {
        _key.value = key
    }

    fun loadSongsByKey(key: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = songRepository.getSongsByNameSongOrNameArtist(key)
            _songs.postValue(result)
        }
    }

    fun saveSongWhenUserClick(recentSearchSong: RecentSearchSong) {
        viewModelScope.launch(Dispatchers.IO) {
            resultSearchSongRepository.insert(recentSearchSong)
            resultSearchSongRepository.trimRecentSearchSong()
        }
    }
}