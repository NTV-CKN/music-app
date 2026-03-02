package com.infix.musicappv1.ui.library.recent_song

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.infix.musicappv1.data.model.recent.SongRecent
import com.infix.musicappv1.data.repository.song_recent.SongRecentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecentSongsViewModel @Inject constructor(
    private val songRecentRepository: SongRecentRepository
) : ViewModel() {
    val songRecents: LiveData<List<SongRecent>?> = songRecentRepository.getSongRecentsDb(12).asLiveData()

//    class Factory(private val songRecentRepository: SongRecentRepository) :
//        ViewModelProvider.Factory {
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            if (modelClass.isAssignableFrom(RecentSongsViewModel::class.java))
//                return RecentSongsViewModel(songRecentRepository) as T
//            throw IllegalArgumentException("Model class is not suit")
//        }
//    }
}