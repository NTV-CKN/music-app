package com.infix.musicappv1.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.ui.dialog.SongOptionMenuDialog
import com.infix.musicappv1.ui.dialog.SongOptionMenuViewModel
import kotlin.getValue

abstract class BasePlayMusicFragment : Fragment() {
    private val songOptionMenuViewModel: SongOptionMenuViewModel by activityViewModels()

    protected fun showDialogSongOptionMenu(song: Song) {
        songOptionMenuViewModel.setSong(song)
        SongOptionMenuDialog().show(
            requireActivity().supportFragmentManager,
            SongOptionMenuDialog.TAG
        )
    }
}