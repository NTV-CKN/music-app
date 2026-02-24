package com.infix.musicappv1.ui.home.system_playlist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.source.local.db.MusicDatabase
import com.infix.musicappv1.databinding.FragmentSystemPlaylistBinding
import com.infix.musicappv1.ui.detail.PlaylistDetailViewModel
import com.infix.musicappv1.ui.home.HomeViewModel
import com.infix.musicappv1.ui.home.system_playlist.more_system_playlist.MoreSystemPlaylistViewModel
import com.infix.musicappv1.utils.InjectUtils

class SystemPlaylistFragment : Fragment() {
    private val systemPlaylistViewModel: SystemPlaylistViewModel by activityViewModels {
        SystemPlaylistViewModel.Factory(
            InjectUtils.getPlaylistRepository(requireContext().applicationContext)
        )
    }
    private val playlistDetailViewModel: PlaylistDetailViewModel by activityViewModels {
        PlaylistDetailViewModel.Factory(
            InjectUtils.getPlaylistRepository(requireContext().applicationContext)
        )
    }
    private val moreSystemPlaylistViewModel: MoreSystemPlaylistViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by activityViewModels {
        HomeViewModel.Factory(
            InjectUtils.getSongRepository(requireContext().applicationContext),
            InjectUtils.getPlaylistRepository(requireContext().applicationContext),
            MusicDatabase.getInstance(requireContext().applicationContext)
        )
    }
    private lateinit var binding: FragmentSystemPlaylistBinding
    private lateinit var adapter: SystemPlaylistAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSystemPlaylistBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        setupEvent()
        observeViewModel()
    }

    private fun setupEvent() {
        //text view most track album
        binding.tvLabelSystemPlaylist.setOnClickListener { navigateToMoreAlbum() }
        //btn image most track album
        binding.btnMoreSystemPlaylist.setOnClickListener { navigateToMoreAlbum() }
    }

    private fun observeViewModel() {
        binding.progressSystemPlaylist.visibility = View.VISIBLE
        systemPlaylistViewModel.playlists.observe(viewLifecycleOwner, { playlists ->
            val sortAlbum = playlists.sortedBy { it.songsId.size }.reversed().subList(0, 8)
            adapter.updatePlaylists(sortAlbum)
            binding.progressSystemPlaylist.visibility = View.GONE
        })
    }

    private fun initRecyclerView() {
        adapter = SystemPlaylistAdapter(object : SystemPlaylistAdapter.SystemPlaylistClick {
            override fun onClick(playlist: Playlist) {
               // playlist.updateSongs(extractSongsByPlaylist(playlist))
                playlistDetailViewModel.setPlaylist(playlist)
                findNavController().navigate(R.id.action_navigation_home_to_detail_playlist)
            }
        })
        binding.rvSystemPlaylist.adapter = adapter
    }

    private fun extractSongsByPlaylist(playlist: Playlist): List<Song> {
//        val allSongs = homeViewModel.songsLocal.value ?: return emptyList()
//        val songsMap = allSongs.associateBy { it.id }
//
//        return playlist.songsId.mapNotNull { songId ->
//            songsMap[songId]
//        }
        return emptyList()
    }

    private fun navigateToMoreAlbum() {
//        moreSystemPlaylistViewModel.setPlaylists(homeViewModel.playlists.value ?: emptyList())
//        findNavController().navigate(R.id.action_navigation_home_to_navigation_more_album)
    }
}