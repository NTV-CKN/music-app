package com.infix.musicappv1.ui.admin.artist.add_update

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.data.repository.artist.ArtistRepository
import com.infix.musicappv1.data.source.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddOrUpdateArtistViewModel @Inject constructor(
    private val artistRepository: ArtistRepository
) : ViewModel() {

    data class AddOrUpdateArtistParams(
        val isUpdate: Boolean,
        val artist: Artist = Artist(),
        val current: Long = System.currentTimeMillis()
    )

    sealed class ValidationError {
        object EmptyImage : ValidationError()
        object EmptyName : ValidationError()
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _params = MutableStateFlow<AddOrUpdateArtistParams?>(null)
    val params = _params.asStateFlow()

    fun validateArtist(): ValidationError? {
        val currentArtist = _params.value?.artist ?: return ValidationError.EmptyName

        if (currentArtist.name.isBlank()) {
            return ValidationError.EmptyName
        }

        if (currentArtist.avatar.isBlank()) {
            return ValidationError.EmptyImage
        }

        return null
    }

    fun saveArtist(
        artist: Artist,
        isUpdate: Boolean,
        callback: (success: Boolean, msg: String) -> Unit
    ) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = artistRepository.saveArtist(artist, isUpdate)

            withContext(Dispatchers.Main) {
                if (result is Result.Success) {
                    callback.invoke(result.data.success, result.data.message)
                } else if (result is Result.Error) {
                    callback.invoke(false, result.err.message ?: "Unknown error")
                }

                _isLoading.value = false
            }
        }
    }

    fun setArtistParamsState(params: AddOrUpdateArtistParams) {
        _params.value = params
    }
}