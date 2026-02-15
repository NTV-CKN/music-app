package com.infix.musicappv1.ui.library.your_playlist

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.playlist.PlaylistWithSongs
import com.infix.musicappv1.databinding.FragmentYourPlaylistBinding
import com.infix.musicappv1.ui.library.your_playlist.dialog.CreatePlaylistDialog
import com.infix.musicappv1.utils.InjectUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class YourPlaylistFragment : Fragment() {
    private lateinit var binding: FragmentYourPlaylistBinding
    private lateinit var adapter: PlaylistCustomAdapter
    private val yourPlaylistViewModel: YourPlaylistViewModel by activityViewModels {
        YourPlaylistViewModel.Factory(
            InjectUtils.getPlaylistRepository(requireContext().applicationContext),
            requireContext().applicationContext
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentYourPlaylistBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressYourPlaylist.visibility = View.VISIBLE
        initRecyclerView()
        setupObserve()
        setupEvent()
        setupListenerResultFragment()
    }

    private fun setupListenerResultFragment() {
        //set result fragment
        requireActivity().supportFragmentManager.setFragmentResultListener(
            CREATE_PLAYLIST_REQUEST_KEY,
            viewLifecycleOwner
        ) { requestKey, bundle ->
            Log.d("SVUC", bundle.getString(KEY_NAME_PLAYLIST) ?: "DEOHAVE")

            if (requestKey != CREATE_PLAYLIST_REQUEST_KEY) return@setFragmentResultListener
            lifecycleScope.launch(Dispatchers.IO) {
                val namePlaylist = bundle.getString(KEY_NAME_PLAYLIST) ?: return@launch
                val result = yourPlaylistViewModel.createPlaylist(namePlaylist)
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        result,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }//end lifecycleScope
        }
    }

    private fun setupEvent() {
        //create playlist
        binding.wrapCreatePlaylist.setOnClickListener {
            CreatePlaylistDialog().show(requireActivity().supportFragmentManager, null)
        }
    }

    private fun initRecyclerView() {
        adapter = PlaylistCustomAdapter(
            object : PlaylistCustomAdapter.OnPlaylistCustomClick {
                override fun onClick(playlistWithSongs: PlaylistWithSongs) {

                }

            },
            object : PlaylistCustomAdapter.OnMenuOptionClick {
                override fun onClick(playlistWithSong: PlaylistWithSongs) {

                }

            }
        )

        binding.includePlaylistCustom.listYourLayout.adapter = adapter
    }

    private fun setupObserve() {
        yourPlaylistViewModel.playlistCustoms.observe(viewLifecycleOwner) {
            adapter.updatePlaylistCustoms(it ?: emptyList())
            binding.progressYourPlaylist.visibility = View.GONE
        }
    }

    companion object {
        const val CREATE_PLAYLIST_REQUEST_KEY = "CREATE_PLAYLIST_REQUEST_KEY"
        const val KEY_NAME_PLAYLIST = "KEY_NAME_PLAYLIST"
    }
}