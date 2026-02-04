package com.infix.musicappv1.ui.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.infix.musicappv1.data.model.option_menu.SongOptionMenuItem
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.utils.SongOptionMenuUtils

class SongOptionMenuViewModel : ViewModel() {
    private val _song = MutableLiveData<Song>()
    val song: LiveData<Song> = _song

    private val _menuItems = MutableLiveData<List<SongOptionMenuItem>>()
    val menuItems: LiveData<List<SongOptionMenuItem>> = _menuItems

    init {
        _menuItems.postValue(SongOptionMenuUtils.optionMenuSongs)
    }

    fun setSong(song: Song) {
        _song.postValue(song)
    }
}