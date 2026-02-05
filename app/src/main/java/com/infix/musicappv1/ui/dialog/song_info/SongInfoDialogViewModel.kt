package com.infix.musicappv1.ui.dialog.song_info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.infix.musicappv1.data.model.song.Song

class SongInfoDialogViewModel : ViewModel() {
    private val _song = MutableLiveData<Song>()
    val song: LiveData<Song> = _song

    fun setSong(song: Song) {
        _song.postValue(song)
    }
}