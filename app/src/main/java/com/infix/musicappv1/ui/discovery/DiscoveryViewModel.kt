package com.infix.musicappv1.ui.discovery

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.data.model.artist.ArtistSongCrossRef
import com.infix.musicappv1.data.repository.artist.ArtistRepository
import com.infix.musicappv1.data.repository.song.SongRepository
import com.infix.musicappv1.data.source.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DiscoveryViewModel(
    private val artistRepository: ArtistRepository,
    private val songRepository: SongRepository
) : ViewModel() {
   // private val songs: LiveData<List<Song>?> = songRepository.getAllSongsFlow().asLiveData()
    //val artists: LiveData<List<Artist>?> = artistRepository.getAllArtists().asLiveData()


//    fun loadArtistsRemote() {
//        viewModelScope.launch(Dispatchers.IO) {
//            val result = artistRepository.loadArtistsRemote()
//            if (result is Result.Success) {
//                extractArtistNotInDb(result.data)
//                insertArtistSongCrossRef()
//            }
//        }
//    }
//
//    private suspend fun insertArtistSongCrossRef() {
//        val songs = songRepository.getAllSongsFlow().first()
//        val artists = artists.value ?: return
//        val listArtistSongCrossRef = mutableListOf<ArtistSongCrossRef>()
//        //convert map key is name and value is artist
//        val artistMap = artists.associateBy { it.name.lowercase().trim() }
//
//        for (song in songs) {
//            //split name arist in song
//            val artistNamesInSong = song.artist.lowercase().split(",", ";", "ft.", "feat.", "ft")
//
//            for (rawName in artistNamesInSong) {
//                val cleanName = rawName.trim()
//                //if raw name exist in artistMap, we add into ArtistSongCrossRef
//                artistMap[cleanName]?.let { artist ->
//                    listArtistSongCrossRef.add(ArtistSongCrossRef(artist.id, song.id))
//                }
//            }
//        }
//
//        artistRepository.insertArtistSongCrossRef(*listArtistSongCrossRef.toTypedArray())
//    }
//
//    private suspend fun extractArtistNotInDb(data: List<Artist>) {
//        if (artists.value == null || artists.value!!.isEmpty()) {
//            artistRepository.insert(*data.toTypedArray())
//        } else {
//            val artistNotInDb = mutableListOf<Artist>()
//            val setLocal = artists.value!!.toSet()
//            for (artist in data)
//                if (!setLocal.contains(artist))
//                    artistNotInDb.add(artist)
//
//            if (artistNotInDb.isNotEmpty())
//                artistRepository.insert(*artistNotInDb.toTypedArray())
//        }
//    }

    class Factory(
        private val artistRepository: ArtistRepository,
        private val songRepository: SongRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DiscoveryViewModel::class.java))
                return DiscoveryViewModel(artistRepository, songRepository) as T
            throw IllegalArgumentException("Model class is not suit")
        }
    }
}