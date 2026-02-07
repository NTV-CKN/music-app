package com.infix.musicappv1.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.ui.dialog.song_option_menu.SongOptionMenuDialog
import com.infix.musicappv1.ui.dialog.song_option_menu.SongOptionMenuViewModel
import com.infix.musicappv1.ui.viewmodels.PlayingSongSharedViewModel
import kotlin.getValue

abstract class BasePlayMusicFragment : Fragment() {
    private val songOptionMenuViewModel: SongOptionMenuViewModel by activityViewModels()
    private val playingSongSharedViewModel: PlayingSongSharedViewModel by activityViewModels()

    protected fun playSong(song: Song, index: Int, namePlaylist: String, songs: List<Song>) {
        //update songs for current playlist
        playingSongSharedViewModel.updatePlaylistCurrent(songs, namePlaylist)
        //update index of song will be play
        playingSongSharedViewModel.updateIndexToPlay(index)
    }

    protected fun showDialogSongOptionMenu(song: Song) {
        songOptionMenuViewModel.setSong(song)
        SongOptionMenuDialog().show(
            requireActivity().supportFragmentManager,
            SongOptionMenuDialog.TAG
        )
    }
}