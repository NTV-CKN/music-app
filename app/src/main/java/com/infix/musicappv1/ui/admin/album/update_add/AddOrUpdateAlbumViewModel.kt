package com.infix.musicappv1.ui.admin.album.update_add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.album.AlbumRepository
import com.infix.musicappv1.data.source.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddOrUpdateAlbumViewModel @Inject constructor(
    private val albumRepository: AlbumRepository
) : ViewModel() {
    data class AddOrUpdateAlbumParams(
        val isUpdate: Boolean,
        val album: Album = Album(),
        val songs: MutableList<Song> = mutableListOf(),
        val current: Long = System.currentTimeMillis()
    ) {
        fun cloneBySongs(songs: MutableList<Song>): AddOrUpdateAlbumParams {
            return AddOrUpdateAlbumParams(
                isUpdate,
                album,
                songs = songs,
                current = System.currentTimeMillis()
            )
        }
    }

    sealed class ValidationError {
        object EmptyImage : ValidationError()
        object EmptyTitle : ValidationError()
        object EmptySongList : ValidationError()
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _params = MutableStateFlow<AddOrUpdateAlbumParams?>(null)
    val params = _params.asStateFlow()

    fun validateAlbum(selectedSongs: List<Song>): ValidationError? {
        val currentAlbum = _params.value?.album ?: return ValidationError.EmptyTitle

        if (currentAlbum.name.isBlank()) {
            return ValidationError.EmptyTitle
        }

        if (currentAlbum.artwork.isBlank()) {
            return ValidationError.EmptyImage
        }

        if (selectedSongs.isEmpty()) {
            return ValidationError.EmptySongList
        }

        return null
    }

    fun setAlbumParamsState(params: AddOrUpdateAlbumParams) {
        _params.value = params

        //We must load all songs from Firestore for the album if isUpdateState is true.
        if (params.isUpdate && params.album.songs.isNotEmpty()) {
            _isLoading.value = true
            viewModelScope.launch(Dispatchers.IO) {
                val resultSongs = albumRepository.loadSongsByAlbumId(params.album.id)
                withContext(Dispatchers.Main) {
                    if (resultSongs is Result.Success) {
                        val newParams = params.cloneBySongs(resultSongs.data.toMutableList())
                        _params.value = newParams
                    } else if (resultSongs is Result.Error) {
                        _params.value = params.cloneBySongs(
                            params.album.songs.map { idSong -> return@map Song(id = idSong) }
                                .toMutableList()
                        )
                    }

                    _isLoading.value = false
                }
            }
        }
    }
}