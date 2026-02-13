package com.infix.musicappv1.ui.library.your_playlist

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.playlist.PlaylistWithSongs
import com.infix.musicappv1.databinding.FragmentYourPlaylistBinding
import com.infix.musicappv1.utils.InjectUtils

class YourPlaylistFragment : Fragment() {
    private lateinit var binding: FragmentYourPlaylistBinding
    private lateinit var adapter: PlaylistCustomAdapter
    private val yourPlaylistViewModel: YourPlaylistViewModel by activityViewModels {
        YourPlaylistViewModel.Factory(
            InjectUtils.getPlaylistRepository(requireContext().applicationContext)
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
}