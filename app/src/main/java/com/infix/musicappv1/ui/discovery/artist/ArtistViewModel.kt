package com.infix.musicappv1.ui.discovery.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.data.repository.NetworkRepository
import com.infix.musicappv1.data.repository.artist.ArtistRepository
import com.infix.musicappv1.data.repository.paging.ArtistRemoteMediator
import com.infix.musicappv1.data.source.local.db.MusicDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(
    private val artistRepository: ArtistRepository,
    private val musicDb: MusicDatabase,
    private val networkRepository: NetworkRepository
) : ViewModel() {
    @OptIn(ExperimentalPagingApi::class)
    val artists = Pager(
        PagingConfig(
            pageSize = 20,
            initialLoadSize = 20,
            prefetchDistance = 5,
            enablePlaceholders = false
        ),
        remoteMediator = ArtistRemoteMediator(artistRepository, musicDb, networkRepository)
    ) {
        artistRepository.getNArtistsPaging(ARTIST_SIZE)
    }.flow.cachedIn(viewModelScope)

    fun updateInterestedArtist(artist: Artist) {
        viewModelScope.launch(Dispatchers.IO) {
            artistRepository.update(artist)
        }
    }

//    class Factory(
//        private val artistRepository: ArtistRepository,
//        private val musicDb: MusicDatabase
//    ) : ViewModelProvider.Factory {
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            if (modelClass.isAssignableFrom(ArtistViewModel::class.java))
//                return ArtistViewModel(artistRepository, musicDb) as T
//            throw IllegalArgumentException("Model class is not suit")
//        }
//    }

    companion object {
        const val ARTIST_SIZE = 10
    }
}