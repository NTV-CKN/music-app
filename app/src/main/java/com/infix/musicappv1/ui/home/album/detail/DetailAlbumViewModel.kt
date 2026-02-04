package com.infix.musicappv1.ui.home.album.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.model.song.Song

class DetailAlbumViewModel : ViewModel() {
    private val _album = MutableLiveData<Album>()
    val album: LiveData<Album> = _album

    private val _songs = MutableLiveData<List<Song>>()
    val songs: LiveData<List<Song>> = _songs

    fun setAlbumAndSongs(album: Album, songs: List<Song>) {
        _album.postValue(album)
        _songs.postValue(songs)
    }
}