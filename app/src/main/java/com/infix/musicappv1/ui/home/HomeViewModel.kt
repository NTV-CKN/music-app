package com.infix.musicappv1.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.album.AlbumRepositoryImpl
import com.infix.musicappv1.data.repository.song.SongRepositoryImpl
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.source.local.album.AlbumLocalDataSource
import com.infix.musicappv1.data.source.remote.AlbumRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

class HomeViewModel(
    private val songRepository: SongRepositoryImpl
) : ViewModel() {
    //temporary
    private val albumRepository = AlbumRepositoryImpl(
        AlbumRemoteDataSource(),
        AlbumLocalDataSource()
    )

//    private val _songsRemote = MutableLiveData<List<Song>?>()

    //when _songRemote has data, songsMediator notify for observe to update local db
//    val songsMediator: LiveData<List<Song>?> = _songsRemote.map { it }
//
    private val _songsLocal = MutableLiveData<List<Song>?>()
    val songsLocal: LiveData<List<Song>?> = _songsLocal

    private val _albums = MutableLiveData<List<Album>>()
    val albums: LiveData<List<Album>> = _albums

    init {
        setupDataTmp()
    }

    private fun setupDataTmp() {
        viewModelScope.launch(Dispatchers.IO) {
            //load song db
            val songsLocal = songRepository.getAllSongs().toMutableList()
            //load song remote
            val resultSong = songRepository.loadSongsRemote()
            if (resultSong is Result.Success) {
//                _songsRemote.postValue(resultSong.data.songs)
                //compare songs between local and remote
                val songsExtract =
                    extractSongRemoteNotContainLocal(songsLocal, resultSong.data.songs)
                if (songsExtract.isNotEmpty()) {
                    songRepository.insert(*songsExtract.toTypedArray())
                    songsLocal.addAll(songsExtract)
                }
            } else if (resultSong is Result.Error) {
//                _songsRemote.postValue(emptyList())
                Log.e("HomeViewmodel", resultSong.err.message ?: "Unknown err")
            }
            _songsLocal.postValue(songsLocal)
            val resultAlbum = albumRepository.loadAlbums()
            if (resultAlbum is Result.Success) {
                _albums.postValue(resultAlbum.data.albums)
            } else {
                _albums.postValue(emptyList())
            }
        }
    }

    private fun extractSongRemoteNotContainLocal(
        local: List<Song>,
        remote: List<Song>
    ): List<Song> {
        val result = mutableListOf<Song>()
        val localSet = local.toSet()
        for (tmp in remote)
            if (!localSet.contains(tmp)) result.add(tmp)

        return result
    }
}