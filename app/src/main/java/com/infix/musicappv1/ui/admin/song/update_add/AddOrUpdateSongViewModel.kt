package com.infix.musicappv1.ui.admin.song.update_add

import androidx.lifecycle.ViewModel
import com.infix.musicappv1.data.model.song.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AddOrUpdateSongViewModel @Inject constructor(): ViewModel() {
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
            song.artistId == 0 || song.artist.isBlank() -> ValidationError.InvalidArtist
            song.album.isEmpty() -> ValidationError.EmptyAlbum
            song.genre.isBlank() -> ValidationError.InvalidGenre
            song.source.isBlank() -> ValidationError.EmptySource
            else -> null
        }
    }
}