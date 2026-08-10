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

    private val _params = MutableStateFlow<AddOrUpdateSongParams?>(null)
    val params = _params.asStateFlow()

    fun setIsUpdateSongState(params: AddOrUpdateSongParams?) {
        _params.value = params
    }
}