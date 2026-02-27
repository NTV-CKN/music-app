package com.infix.musicappv1.ui.home.album.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.album.AlbumRepository
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.source.remote.param.SearchParam
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class AlbumDetailViewModel(
    private val albumRepository: AlbumRepository
) : ViewModel() {
    private var songsTrack: List<Song>? = null

    private val _album = MutableLiveData<Album?>()
    val album: LiveData<Album?> = _album

    private val _songs = MutableSharedFlow<List<Song>>()
    val songs: SharedFlow<List<Song>> = _songs

    fun setAlbum(album: Album) {
        _album.value = album
    }

    fun loadSongs(albumId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val searchParam = SearchParam(
                queryType = SearchParam.QUERY_TYPE_ALBUM_WITH_SONG,
                query = albumId
            )
            val result = albumRepository.loadSongsByAlbumId(searchParam)
            if (result is Result.Success) {
                _songs.emit(result.data)
                songsTrack = result.data
            } else {
                val empty = emptyList<Song>()
                _songs.emit(empty)
                songsTrack = empty
            }
        }
    }

    fun getSongsTrack() = songsTrack

    class Factory(
        private val albumRepository: AlbumRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AlbumDetailViewModel::class.java))
                return AlbumDetailViewModel(albumRepository) as T
            throw IllegalArgumentException("Model class is not suit")
        }
    }
}