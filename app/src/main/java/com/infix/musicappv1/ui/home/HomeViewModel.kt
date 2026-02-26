package com.infix.musicappv1.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.album.AlbumRepository
import com.infix.musicappv1.data.repository.song.SongRepository
import com.infix.musicappv1.data.source.local.db.MusicDatabase

@Suppress("UNCHECKED_CAST")
class HomeViewModel(
    private val songRepository: SongRepository,
    private val albumRepository: AlbumRepository,
    private val musicDb: MusicDatabase
) : ViewModel() {
    //when any fragment has paging song, room update more song and notify for songLocal
    val songLocal: LiveData<List<Song>?> = songRepository.getAllSongsFlow().asLiveData()
    val albumLocal: LiveData<List<Album>?> = albumRepository.loadAllAlbumsFlow().asLiveData()

    class Factory(
        private val songRepository: SongRepository,
        private val albumRepository: AlbumRepository,
        private val musicDb: MusicDatabase
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java))
                return HomeViewModel(songRepository, albumRepository, musicDb) as T
            throw IllegalArgumentException("Model class is not suit")
        }
    }
}