package com.infix.musicappv1.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.album.AlbumRepository
import com.infix.musicappv1.data.repository.song.SongRepository
import com.infix.musicappv1.data.source.local.db.MusicDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
@Suppress("UNCHECKED_CAST")
class HomeViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val albumRepository: AlbumRepository,
    private val musicDb: MusicDatabase
) : ViewModel() {
    //when any fragment has paging song, room update more song and notify for songLocal
    val songLocal: LiveData<List<Song>?> = songRepository.getAllSongsFlow().asLiveData()
    val albumLocal: LiveData<List<Album>?> = albumRepository.loadAllAlbumsFlow().asLiveData()
}