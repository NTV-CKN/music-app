package com.infix.musicappv1.ui.discovery.artist.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.artist.ArtistRepository
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.source.remote.param.SearchParam
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistDetailViewModel @Inject constructor(
    private val artistRepository: ArtistRepository
) : ViewModel() {
    private var artist: Artist? = null
    //use shared flow cause ArtistDetailViewModel is share with fragments, so when
    //user access ArtistDetailFragment, adapter will update double songs (songs of old value and songs of new value)
    private val _songs = MutableSharedFlow<List<Song>?>()
    val songs: SharedFlow<List<Song>?> = _songs

    //cause we paging song and full song if only user scroll reached at More recommend song
    //So we get songs of name artist with API to avoid song not full but artist detail click
    fun setArtistWithSongsByArtistName(artistId: Int, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            this@ArtistDetailViewModel.artist = artistRepository.getArtistById(artistId)
            val result = artistRepository.loadSongsByNameArtist(
                SearchParam(
                    queryType = SearchParam.QUERY_TYPE_SEARCH,
                    query = name
                )
            )

            if (result is Result.Success)
                _songs.emit(result.data)
            else
                _songs.emit(emptyList())
        }
    }

    fun getArtist() = artist

//    class Factory(
//        private val artistRepository: ArtistRepository
//    ) : ViewModelProvider.Factory {
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            if (modelClass.isAssignableFrom(ArtistDetailViewModel::class.java))
//                return ArtistDetailViewModel(artistRepository) as T
//            throw IllegalArgumentException("Model class is not suit")
//        }
//    }
}