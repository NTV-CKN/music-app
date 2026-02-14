package com.infix.musicappv1.ui.home.system_playlist.more_system_playlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.databinding.FragmentMoreAlbumBinding
import com.infix.musicappv1.ui.detail.PlaylistDetailViewModel
import com.infix.musicappv1.ui.home.HomeViewModel
import com.infix.musicappv1.utils.InjectUtils

class MoreSystemPlaylistFragment : Fragment() {
    private lateinit var binding: FragmentMoreAlbumBinding
    private lateinit var adapter: MoreSystemPlaylistAdapter

    private val moreSystemPlaylistViewModel: MoreSystemPlaylistViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by activityViewModels {
        HomeViewModel.Factory(
            InjectUtils.getSongRepository(requireContext().applicationContext),
            InjectUtils.getPlaylistRepository(requireContext().applicationContext)
        )
    }
    private val playlistDetailViewModel: PlaylistDetailViewModel by activityViewModels {
        PlaylistDetailViewModel.Factory(
            InjectUtils.getPlaylistRepository(requireContext().applicationContext)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoreAlbumBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbarMoreAlbum.setupWithNavController(findNavController())
        setupRecyclerView()
        setupEvent()
    }

    private fun setupRecyclerView() {
        adapter =
            MoreSystemPlaylistAdapter(object : MoreSystemPlaylistAdapter.PlaylistClickListener {
                override fun onClick(playlist: Playlist) {
                    playlist.updateSongs(extractSongsByPlaylist(playlist))
                    playlistDetailViewModel.setPlaylist(playlist)
                    findNavController().navigate(R.id.action_navigation_more_system_playlist_to_navigation_detail_playlist)
                }
            })

        binding.rvMoreAlbum.adapter = adapter
    }

    private fun extractSongsByPlaylist(playlist: Playlist): List<Song> {
        val songs = homeViewModel.songsLocal.value
        val result = mutableListOf<Song>()
        songs?.let { songs ->
            for (songId in playlist.songsId) {
                val index = songs.indexOfFirst { song -> song.id == songId }
                if (index != -1)
                    result.add(songs[index])
            }
        }

        return result
    }

    private fun setupEvent() {
        moreSystemPlaylistViewModel.playlists.observe(viewLifecycleOwner) { albums ->
            adapter.updateAlbums(albums)
        }
    }
}