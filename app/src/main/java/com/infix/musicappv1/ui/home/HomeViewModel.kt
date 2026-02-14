package com.infix.musicappv1.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.album.AlbumRepositoryImpl
import com.infix.musicappv1.data.repository.playlist.PlaylistRepository
import com.infix.musicappv1.data.repository.song.SongRepository
import com.infix.musicappv1.data.repository.song.SongRepositoryImpl
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.source.local.album.AlbumLocalDataSource
import com.infix.musicappv1.data.source.remote.album.AlbumRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

class HomeViewModel(
    private val songRepository: SongRepository,
    private val playlistRepository: PlaylistRepository
) : ViewModel() {

//    private val _songsRemote = MutableLiveData<List<Song>?>()

    //when _songRemote has data, songsMediator notify for observe to update local db
//    val songsMediator: LiveData<List<Song>?> = _songsRemote.map { it }
//
    private val _songsLocal = MutableLiveData<List<Song>?>()
    val songsLocal: LiveData<List<Song>?> = _songsLocal

    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>> = _playlists

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
//                _songsRemote.postValue(resultSong.data.songsObject)
                //compare songsObject between local and remote
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

            //album local
            val resultPlaylists = playlistRepository.loadSystemPlaylists()
            if (resultPlaylists is Result.Success) {
                _playlists.postValue(resultPlaylists.data)
            } else {
                _playlists.postValue(emptyList())
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

    class Factory(
        private val songRepository: SongRepository,
        private val playlistRepository: PlaylistRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java))
                return HomeViewModel(songRepository, playlistRepository) as T
            throw IllegalArgumentException("Model class is not suit")
        }
    }
}