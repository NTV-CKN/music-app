package com.infix.musicappv1.ui.home.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.source.local.db.MusicDatabase
import com.infix.musicappv1.databinding.FragmentAlbumBinding
import com.infix.musicappv1.ui.adapter.album.AlbumPagingDataAdapter
import com.infix.musicappv1.ui.detail.PlaylistDetailViewModel
import com.infix.musicappv1.ui.home.album.detail.AlbumDetailViewModel
import com.infix.musicappv1.utils.InjectUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AlbumFragment : Fragment() {
    private val albumViewModel: AlbumViewModel by activityViewModels {
        AlbumViewModel.Factory(
            InjectUtils.getAlbumRepository(requireContext().applicationContext),
            MusicDatabase.getInstance(requireContext().applicationContext)
        )
    }

    private val albumDetailViewModel: AlbumDetailViewModel by activityViewModels {
        AlbumDetailViewModel.Factory(
            InjectUtils.getAlbumRepository(requireContext().applicationContext)
        )
    }

    private lateinit var binding: FragmentAlbumBinding
    private lateinit var adapter: AlbumPagingDataAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumBinding.inflate(
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
        setupAlbumPaging()
    }

    private fun setupEvent() {
        //text view most track album
        binding.tvLabelAlbum.setOnClickListener { navigateToMoreAlbum() }
        //btn image most track album
        binding.btnMoreAlbum.setOnClickListener { navigateToMoreAlbum() }
    }

    private fun setupAlbumPaging() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                albumViewModel.albums.collectLatest { adapter.submitData(it) }
            }
        }
    }

    private fun initRecyclerView() {
        adapter = AlbumPagingDataAdapter(object : AlbumPagingDataAdapter.AlbumClickListener {
            override fun onClick(album: Album) {
                albumDetailViewModel.setAlbum(album)
                findNavController().navigate(R.id.action_navigation_home_to_navigate_album_detail)
            }
        })
        binding.rvAlbum.adapter = adapter
    }

    private fun navigateToMoreAlbum() {
        findNavController().navigate(R.id.action_navigation_home_to_navigation_more_album)
    }
}