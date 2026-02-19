package com.infix.musicappv1.ui.dialog.your_playlist_add_create

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.playlist.PlaylistSongCrossRef
import com.infix.musicappv1.data.model.playlist.PlaylistWithSongs
import com.infix.musicappv1.databinding.DialogFragmentYourPlaylistAddOrCreateBinding
import com.infix.musicappv1.ui.dialog.song_option_menu.SongOptionMenuDialog
import com.infix.musicappv1.ui.library.your_playlist.PlaylistCustomAdapter
import com.infix.musicappv1.ui.library.your_playlist.YourPlaylistViewModel
import com.infix.musicappv1.ui.library.your_playlist.more_your_playlist.MoreYourPlaylistViewModel
import com.infix.musicappv1.utils.InjectUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//this dialog creates a new playlist custom. If a user select an playlist custom existing, it
// extract song id from fragment result (SongOptionMenuDialog) and save into PlaylistSong
class YourPlaylistAddOrCreateDialog : DialogFragment() {
    private var songId: String? = null
    private lateinit var adapter: PlaylistCustomAdapter
    private lateinit var binding: DialogFragmentYourPlaylistAddOrCreateBinding
    private var createPlaylistJob: Job? = null
    private var addSongToPlaylistJob: Job? = null

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

        setupListenerFragmentResult()
        //setup data playlist
        initRvPlaylistCustoms()
        setupObserve()
        //setup event button
        setupEventButtons()
        return dialog
    }

    override fun onDestroy() {
        super.onDestroy()
        createPlaylistJob?.cancel()
        addSongToPlaylistJob?.cancel()
    }

    private fun setupListenerFragmentResult() {
        requireActivity().supportFragmentManager.setFragmentResultListener(
            SongOptionMenuDialog.REQUEST_KEY,
            this
        ) { requestKey, result ->
            songId = result.getString(SongOptionMenuDialog.KEY_SONG_ID, null)
        }
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
                    addSongToPlaylistJob?.cancel()
                    if (songId == null) {
                        Toast.makeText(
                            requireContext(), getString(R.string.txt_song_id_is_null),
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }
                    val playlist = playlistWithSongs.playlist
                    addSongToPlaylistJob = lifecycleScope.launch(Dispatchers.IO) {
                        val result = yourPlaylistViewModel.insertPlaylistSong(
                            PlaylistSongCrossRef(
                                playlist.playlistId,
                                songId!!
                            )
                        )
                        lifecycleScope.launch(Dispatchers.Main) {
                            Toast.makeText(
                                requireContext(), result,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
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
        //cancel
        binding.btnCancelCreatePlaylistCustomDialog.setOnClickListener { dismiss() }
        //submit create playlist
        binding.btnSubmitCreatePlaylistCustomDialog.setOnClickListener {
            createPlaylistJob?.cancel()
            createPlaylistJob = lifecycleScope.launch(Dispatchers.IO) {
                val namePlaylist = binding.inputPlaylistNameDialog.text?.toString()
                val result = if (namePlaylist == null || namePlaylist.isEmpty())
                    getString(R.string.txt_name_playlist_is_empty)
                else yourPlaylistViewModel.createPlaylist(namePlaylist)

                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), result, Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    companion object {
        const val TAG = "YourPlaylistAddOrCreateDialog"
    }
}