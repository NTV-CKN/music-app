package com.infix.musicappv1.ui.admin.song.update_add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infix.musicappv1.data.dto.BaseResultResponse
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.song.SongRepository
import com.infix.musicappv1.data.source.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddOrUpdateSongViewModel @Inject constructor(
    private val songRepository: SongRepository
) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    data class AddOrUpdateSongParams(
        val isUpdate: Boolean,
        val song: Song = Song(),
        val current: Long = System.currentTimeMillis()
    )

    sealed class ValidationError {
        object EmptyImage : ValidationError()
        object EmptyTitle : ValidationError()
        object InvalidArtist : ValidationError()
        object InvalidGenre : ValidationError()
        object EmptySource : ValidationError()
        object EmptyAlbum : ValidationError()
    }

    data class MediaUploadResult(
        val imageUrl: String? = null,
        val sourceUrl: String? = null
    )

    private val _params = MutableStateFlow<AddOrUpdateSongParams?>(null)
    val params = _params.asStateFlow()

    fun setIsUpdateSongState(params: AddOrUpdateSongParams?) {
        _params.value = params
    }

    fun validateSong(): ValidationError? {
        val song = _params.value?.song ?: return ValidationError.EmptyTitle

        return when {
            song.image.isBlank() -> ValidationError.EmptyImage
            song.title.isBlank() -> ValidationError.EmptyTitle
            song.artistId == 0L || song.artist.isBlank() -> ValidationError.InvalidArtist
            song.album.isEmpty() -> ValidationError.EmptyAlbum
            song.genre.isBlank() -> ValidationError.InvalidGenre
            song.source.isBlank() -> ValidationError.EmptySource
            else -> null
        }
    }

    fun saveSong(callback: (baseResult: BaseResultResponse) -> Unit) {
        if (validateSong() != null) return

        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            val song = params.value!!.song
            val response = songRepository.saveSong(song, params.value!!.isUpdate)

            if (response is Result.Success) {
                callback.invoke(response.data)
            } else if (response is Result.Error) {
                callback.invoke(
                    BaseResultResponse(
                        success = false,
                        message = response.err.message ?: "Unknown"
                    )
                )
            }

            _isLoading.postValue(false)
        }
    }
}