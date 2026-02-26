package com.infix.musicappv1.ui.home.album.more_album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.source.local.db.MusicDatabase
import com.infix.musicappv1.databinding.FragmentMoreAlbumBinding
import com.infix.musicappv1.ui.adapter.album.MoreAlbumPagingDataAdapter
import com.infix.musicappv1.ui.detail.PlaylistDetailViewModel
import com.infix.musicappv1.utils.InjectUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MoreAlbumFragment : Fragment() {
    private lateinit var binding: FragmentMoreAlbumBinding
    private lateinit var adapter: MoreAlbumPagingDataAdapter

    private val moreAlbumViewModel: MoreAlbumViewModel by viewModels {
        MoreAlbumViewModel.Factory(
            InjectUtils.getAlbumRepository(requireContext().applicationContext),
            MusicDatabase.getInstance(requireContext().applicationContext)
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
        setupPagingAlbum()
    }

    private fun setupRecyclerView() {
        adapter =
            MoreAlbumPagingDataAdapter(object : MoreAlbumPagingDataAdapter.AlbumClickListener {
                override fun onClick(album: Album) {
                    //  playlist.updateSongs(extractSongsByPlaylist(playlist))
                    playlistDetailViewModel.setPlaylist(
                        Playlist(
                            playlistId = album.id.toInt(),
                            namePlaylist = album.name,
                            artwork = album.artwork
                        )
                    )
                    findNavController().navigate(R.id.action_navigation_more_album_to_navigation_detail_playlist)
                }
            })

        binding.rvMoreAlbum.adapter = adapter
    }

    private fun setupPagingAlbum() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                moreAlbumViewModel.albums.collectLatest { adapter.submitData(it) }
            }
        }
    }
}