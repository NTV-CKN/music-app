package com.infix.musicappv1.ui.home.album.more_album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.databinding.FragmentMoreAlbumBinding
import com.infix.musicappv1.ui.adapter.album.MoreAlbumPagingDataAdapter
import com.infix.musicappv1.ui.base.BaseFragment
import com.infix.musicappv1.ui.home.album.detail.AlbumDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoreAlbumFragment : BaseFragment() {
    private lateinit var binding: FragmentMoreAlbumBinding
    private lateinit var adapter: MoreAlbumPagingDataAdapter

    private val moreAlbumViewModel: MoreAlbumViewModel by viewModels()

    private val albumDetailViewModel: AlbumDetailViewModel by activityViewModels()
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
                    albumDetailViewModel.setAlbum(album)
                    findNavController().navigate(MoreAlbumFragmentDirections.actionNavigationMoreAlbumToNavigateAlbumDetail())
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