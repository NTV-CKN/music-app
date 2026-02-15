package com.infix.musicappv1.ui.library.your_playlist.more_your_playlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.infix.musicappv1.data.model.playlist.PlaylistWithSongs
import com.infix.musicappv1.databinding.FragmentMoreYourPlaylistBinding
import com.infix.musicappv1.ui.library.your_playlist.PlaylistCustomAdapter
import com.infix.musicappv1.utils.InjectUtils

class MoreYourPlaylistFragment : Fragment() {
    private lateinit var binding: FragmentMoreYourPlaylistBinding
    private lateinit var adapter: PlaylistCustomAdapter
    private val moreYourPlaylistViewModel: MoreYourPlaylistViewModel by viewModels {
        MoreYourPlaylistViewModel.Factory(InjectUtils.getPlaylistRepository(requireContext().applicationContext))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoreYourPlaylistBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        setupObserve()
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

        binding.rvPlaylistsCustom.adapter = adapter
    }

    private fun setupObserve() {
        moreYourPlaylistViewModel.playlists.observe(viewLifecycleOwner) {
            adapter.updatePlaylistCustoms(it ?: emptyList())
        }
    }
}