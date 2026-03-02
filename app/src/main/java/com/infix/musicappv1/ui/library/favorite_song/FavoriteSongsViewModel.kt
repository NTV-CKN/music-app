package com.infix.musicappv1.ui.library.favorite_song

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.song.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteSongsViewModel @Inject constructor(
    private val songRepository: SongRepository
) : ViewModel() {
    val songsFavorite: LiveData<List<Song>?> = songRepository.getSongsFavoriteWithLimit().asLiveData()

//    class Factory(private val songRepository: SongRepository) :
//        ViewModelProvider.Factory {
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            if (modelClass.isAssignableFrom(FavoriteSongsViewModel::class.java))
//                return FavoriteSongsViewModel(songRepository) as T
//            throw IllegalArgumentException("Model class is not suit")
//        }
//    }
}