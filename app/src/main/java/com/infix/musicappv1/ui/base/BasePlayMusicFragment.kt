package com.infix.musicappv1.ui.base

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.ui.auth.AuthViewModel
import com.infix.musicappv1.ui.dialog.song_option_menu.SongOptionMenuDialog
import com.infix.musicappv1.ui.dialog.song_option_menu.SongOptionMenuViewModel
import com.infix.musicappv1.ui.viewmodels.PlayingSongSharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BasePlayMusicFragment : BaseFragment() {
    private val songOptionMenuViewModel: SongOptionMenuViewModel by activityViewModels()

    private val playingSongSharedViewModel: PlayingSongSharedViewModel by activityViewModels()

    private val authViewModel by activityViewModels<AuthViewModel>()

    protected fun playSong(index: Int, playlist: Playlist, songs: List<Song>) {
        if (!checkNetwork()) return

        val userSession = authViewModel.userSession.value
        if (songs[index].isVip) {
            Log.d("BasePlayMusicFragment", userSession.user?.toString() ?: "")
            if (userSession.user == null || !userSession.user.isVip) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.msg_vip_required_for_song),
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
        }

        //update songsLocal for current playlist
        playingSongSharedViewModel.updatePlaylistCurrent(songs, playlist)
        //update index of song will be play
        playingSongSharedViewModel.updateIndexToPlay(index)
    }

    protected fun showDialogSongOptionMenu(song: Song) {
        if (!checkNetwork()) return

        songOptionMenuViewModel.setSong(song)
        SongOptionMenuDialog().show(
            requireActivity().supportFragmentManager,
            SongOptionMenuDialog.TAG
        )
    }
}