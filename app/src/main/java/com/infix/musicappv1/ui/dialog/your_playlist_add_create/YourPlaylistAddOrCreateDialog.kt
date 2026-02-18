package com.infix.musicappv1.ui.dialog.your_playlist_add_create

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.playlist.PlaylistWithSongs
import com.infix.musicappv1.databinding.DialogFragmentYourPlaylistAddOrCreateBinding
import com.infix.musicappv1.ui.library.your_playlist.PlaylistCustomAdapter
import com.infix.musicappv1.ui.library.your_playlist.YourPlaylistViewModel
import com.infix.musicappv1.ui.library.your_playlist.more_your_playlist.MoreYourPlaylistViewModel
import com.infix.musicappv1.utils.InjectUtils

//this dialog creates a new playlist custom. If a user select an playlist custom existing, it
// returns playlist id forSongOptionMenuDialo, via to fragment result
class YourPlaylistAddOrCreateDialog : DialogFragment() {
    private lateinit var adapter: PlaylistCustomAdapter
    private lateinit var binding: DialogFragmentYourPlaylistAddOrCreateBinding

    //create playlist custom
    private val yourPlaylistViewModel: YourPlaylistViewModel by activityViewModels {
        YourPlaylistViewModel.Factory(
            InjectUtils.getPlaylistRepository(requireContext().applicationContext),
            requireContext().applicationContext
        )
    }

    //get playlists custom (limit+offset)
    private val moreYourPlaylistViewModel: MoreYourPlaylistViewModel by activityViewModels {
        MoreYourPlaylistViewModel.Factory(
            InjectUtils.getPlaylistRepository(requireContext().applicationContext)
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogFragmentYourPlaylistAddOrCreateBinding.inflate(layoutInflater)

        val builder = AlertDialog.Builder(requireContext())
        val dialog = builder.setView(binding.root).create()

        //setup data playlist
        initRvPlaylistCustoms()
        setupObserve()
        //setup event button
        setupEventButtons()
        return dialog
    }

    private fun setupObserve() {
        moreYourPlaylistViewModel.playlists.observe(this) {
            adapter.updatePlaylistCustoms(it ?: emptyList())
        }
    }

    private fun initRvPlaylistCustoms() {
        adapter = PlaylistCustomAdapter(
            object : PlaylistCustomAdapter.OnPlaylistCustomClick {
                override fun onClick(playlistWithSongs: PlaylistWithSongs) {

                }
            },
            object : PlaylistCustomAdapter.OnMenuOptionClick {
                override fun onClick(playlistWithSong: PlaylistWithSongs) {

                }
            },
            showOptionMenu = false
        )
        binding.includeListPlaylistCustom.listYourLayout.adapter = adapter
    }

    private fun setupEventButtons() {

    }

    companion object {
        const val TAG = "YourPlaylistAddOrCreateDialog"
    }
}