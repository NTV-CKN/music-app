package com.infix.musicappv1.ui.library.your_playlist.more_your_playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.playlist.PlaylistWithSongs
import com.infix.musicappv1.databinding.FragmentMoreYourPlaylistBinding
import com.infix.musicappv1.ui.detail.PlaylistDetailViewModel
import com.infix.musicappv1.ui.library.your_playlist.PlaylistCustomAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoreYourPlaylistFragment : Fragment() {
    private lateinit var binding: FragmentMoreYourPlaylistBinding
    private lateinit var adapter: PlaylistCustomAdapter
    private val moreYourPlaylistViewModel: MoreYourPlaylistViewModel by viewModels()
    private val playlistDetailViewModel: PlaylistDetailViewModel by activityViewModels()

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
                    playlistWithSongs.playlist.updateSongs(playlistWithSongs.songs)
                    playlistDetailViewModel.setPlaylist(playlistWithSongs.playlist)
                    findNavController().navigate(R.id.action_navigate_more_your_playlist_to_navigation_detail_playlist)
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