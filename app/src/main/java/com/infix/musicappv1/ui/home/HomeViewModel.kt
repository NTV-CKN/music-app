package com.infix.musicappv1.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.album.AlbumRepositoryImpl
import com.infix.musicappv1.data.repository.song.SongRepositoryImpl
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.source.local.AlbumLocalDataSource
import com.infix.musicappv1.data.source.local.SongLocalDataSource
import com.infix.musicappv1.data.source.remote.AlbumRemoteDataSource
import com.infix.musicappv1.data.source.remote.SongRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
) : ViewModel() {
    //temporary
    private val albumRepository = AlbumRepositoryImpl(
        AlbumRemoteDataSource(),
        AlbumLocalDataSource()
    )
    private val songRepository = SongRepositoryImpl(
        SongRemoteDataSource(),
        SongLocalDataSource()
    )

    private val _songs = MutableLiveData<List<Song>>()
    val songs: LiveData<List<Song>> = _songs

    private val _albums = MutableLiveData<List<Album>>()
    val albums: LiveData<List<Album>> = _albums

    init {
        setupDataTmp()
    }

    private fun setupDataTmp() {
        viewModelScope.launch(Dispatchers.IO) {
            val resultSong = songRepository.loadSongs()
            if (resultSong is Result.Success)
                _songs.postValue(resultSong.data.songs)
            else if (resultSong is Result.Error) {
                _songs.postValue(emptyList())
            }

            val resultAlbum = albumRepository.loadAlbums()
            if (resultAlbum is Result.Success) {
                _albums.postValue(resultAlbum.data.albums)
            } else {
                _albums.postValue(emptyList())
            }
        }
    }
}