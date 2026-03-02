package com.infix.musicappv1.ui.library.your_playlist

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.model.playlist.PlaylistSongCrossRef
import com.infix.musicappv1.data.model.playlist.PlaylistWithSongs
import com.infix.musicappv1.data.repository.playlist.PlaylistRepository
import com.infix.musicappv1.ui.MainActivity
import com.infix.musicappv1.ui.datastorePrefSession
import com.infix.musicappv1.utils.MusicAppUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class YourPlaylistViewModel @Inject constructor(
    //require application context
    @param:ApplicationContext private val context: Context,
    private val playlistRepository: PlaylistRepository
) : ViewModel() {
    val playlistCustoms: LiveData<List<PlaylistWithSongs>?> =
        playlistRepository.getLimitPlaylistCustomWithSong().asLiveData()

    suspend fun insertPlaylistSong(playlistSongCrossRef: PlaylistSongCrossRef):String {
        return try {
            playlistRepository.insertPlaylistSongStrict(playlistSongCrossRef)
            context.getString(R.string.txt_add_song_into_playlist_success)
        } catch (_: SQLiteConstraintException) {
            context.getString(R.string.txt_song_already_added_to_playlist)
        }
    }

    suspend fun createPlaylist(namePlaylist: String): String {
        return try {
            val playlistId =
                context.datastorePrefSession.data.first()[MainActivity.KEY_ID_PLAYLIST_CUSTOM]
                    ?: MusicAppUtils.DEFAULT_PLAYLIST_ID_CUSTOM

            playlistRepository.insertPlaylistStrict(
                Playlist(namePlaylist = namePlaylist, playlistId = playlistId, createdAt = Date(), isCustom = true)
            )
            //if success, increase or create next custom playlist id
            context.datastorePrefSession.edit { pref ->
                pref[MainActivity.KEY_ID_PLAYLIST_CUSTOM] = playlistId + 1
            }
            context.getString(R.string.txt_create_playlist_success)
        } catch (_: SQLiteConstraintException) {
            context.getString(R.string.txt_name_playlist_exists)
        }
    }

//    class Factory(
//        private val playlistRepository: PlaylistRepository,
//        private val context: Context
//    ) : ViewModelProvider.Factory {
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            if (modelClass.isAssignableFrom(YourPlaylistViewModel::class.java))
//                return YourPlaylistViewModel(context, playlistRepository) as T
//            throw IllegalArgumentException("Model class is not suit")
//        }
//    }
}