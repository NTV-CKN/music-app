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
import com.infix.musicappv1.data.source.local.db.MusicDatabase
import com.infix.musicappv1.data.source.remote.album.AlbumRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

class HomeViewModel(
    private val songRepository: SongRepository,
    private val playlistRepository: PlaylistRepository,
    private val musicDb: MusicDatabase
) : ViewModel() {
//    private val _playlists = MutableLiveData<List<Playlist>>()
//    val playlists: LiveData<List<Playlist>> = _playlists


//    private fun extractSongRemoteNotContainLocal(
//        local: List<Song>,
//        remote: List<Song>
//    ): List<Song> {
//        val result = mutableListOf<Song>()
//        val localSet = local.toSet()
//        for (tmp in remote)
//            if (!localSet.contains(tmp)) result.add(tmp)
//
//        return result
//    }

    class Factory(
        private val songRepository: SongRepository,
        private val playlistRepository: PlaylistRepository,
        private val musicDb: MusicDatabase
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java))
                return HomeViewModel(songRepository, playlistRepository, musicDb) as T
            throw IllegalArgumentException("Model class is not suit")
        }
    }
}