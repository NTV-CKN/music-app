package com.infix.musicappv1.ui.discovery.artist

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
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.databinding.FragmentArtistBinding
import com.infix.musicappv1.ui.adapter.artist.ArtistPagingDataAdapter
import com.infix.musicappv1.ui.discovery.DiscoveryFragmentDirections
import com.infix.musicappv1.ui.discovery.artist.detail.ArtistDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArtistFragment : Fragment() {
    private lateinit var binding: FragmentArtistBinding
    private val adapter: ArtistPagingDataAdapter = ArtistPagingDataAdapter(
        object : ArtistPagingDataAdapter.OnArtistClick {
            override fun onClick(artist: Artist) {
                artistDetailViewModel.setArtistWithSongsByArtistName(artist.id, artist.name)
                findNavController().navigate(DiscoveryFragmentDirections.actionNavigationDiscoveryToNavigateDetailArtist())
            }
        },
        object : ArtistPagingDataAdapter.OnInterestClick {
            override fun onClick(artist: Artist) {
                artistViewModel.updateInterestedArtist(
                    artist.apply { this.isInterested = !this.isInterested }
                )
            }
        }
    )
    private val artistViewModel: ArtistViewModel by activityViewModels()

    private val artistDetailViewModel: ArtistDetailViewModel by activityViewModels ()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArtistBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressArtist.visibility = View.VISIBLE
        initRecyclerView()
        setupPagingArtist()
        setupEvent()
    }

    private fun setupEvent() {
        //more artist
        binding.tvLabelArtist.setOnClickListener {
            findNavController().navigate(DiscoveryFragmentDirections.actionNavigationDiscoveryToNavigateMoreArtist())
        }
    }

    private fun initRecyclerView() {
        binding.includeListArtists.rvArtist.adapter = adapter
    }

    private fun setupPagingArtist() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                artistViewModel.artists.collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }
}