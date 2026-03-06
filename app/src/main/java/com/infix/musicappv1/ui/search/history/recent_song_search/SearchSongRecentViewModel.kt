package com.infix.musicappv1.ui.search.history.recent_song_search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.infix.musicappv1.data.repository.search.song.RecentSearchSongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchSongRecentViewModel @Inject constructor(
    private val recentSearchSongRepository: RecentSearchSongRepository
) : ViewModel() {
    val songs = recentSearchSongRepository.getRecentSearchSong().asLiveData()

    fun clearAll() {
        viewModelScope.launch(Dispatchers.IO) {
            recentSearchSongRepository.clearAll()
        }
    }
}