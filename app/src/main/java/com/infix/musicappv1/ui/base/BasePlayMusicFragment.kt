package com.infix.musicappv1.ui.base

import androidx.fragment.app.activityViewModels
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.ui.dialog.song_option_menu.SongOptionMenuDialog
import com.infix.musicappv1.ui.dialog.song_option_menu.SongOptionMenuViewModel
import com.infix.musicappv1.ui.viewmodels.PlayingSongSharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BasePlayMusicFragment : BaseFragment() {
    private val songOptionMenuViewModel: SongOptionMenuViewModel by activityViewModels()

    private val playingSongSharedViewModel: PlayingSongSharedViewModel by activityViewModels()

    protected fun playSong(index: Int, playlist: Playlist, songs: List<Song>) {
        if(!checkNetwork()) return

        //update songsLocal for current playlist
        playingSongSharedViewModel.updatePlaylistCurrent(songs, playlist)
        //update index of song will be play
        playingSongSharedViewModel.updateIndexToPlay(index)
    }

    protected fun showDialogSongOptionMenu(song: Song) {
        if(!checkNetwork()) return

        songOptionMenuViewModel.setSong(song)
        SongOptionMenuDialog().show(
            requireActivity().supportFragmentManager,
            SongOptionMenuDialog.TAG
        )
    }
}