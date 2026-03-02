package com.infix.musicappv1.ui.discovery.most_heard.more_top_40

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.song.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoreSongMostHeardViewModel @Inject constructor(
    private val songRepository: SongRepository
) : ViewModel() {
    val top40SongMostHeard: LiveData<List<Song>> =
        songRepository.getTop40SongMostHeard().asLiveData()

//    class Factory(
//        private val songRepository: SongRepository
//    ) : ViewModelProvider.Factory {
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            if (modelClass.isAssignableFrom(MoreSongMostHeardViewModel::class.java))
//                return MoreSongMostHeardViewModel(songRepository) as T
//            throw IllegalArgumentException("Model class is not suit")
//        }
//    }
}