package com.infix.musicappv1.ui.search.history.key_song

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.infix.musicappv1.data.model.search.SearchKeySong
import com.infix.musicappv1.data.repository.search.song.SearchKeySongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchSongKeyViewModel @Inject constructor(
    private val searchKeySongRepository: SearchKeySongRepository
) : ViewModel() {
    val searchKeySongs = searchKeySongRepository.getSearchKeySong().asLiveData()

    fun insert(searchKeySong: SearchKeySong) {
        viewModelScope.launch(Dispatchers.IO) {
            searchKeySongRepository.insert(searchKeySong)
            searchKeySongRepository.trimSearchKeySong()
        }
    }

    fun clearAll() {
        viewModelScope.launch(Dispatchers.IO) {
            searchKeySongRepository.clearAll()
        }
    }
}