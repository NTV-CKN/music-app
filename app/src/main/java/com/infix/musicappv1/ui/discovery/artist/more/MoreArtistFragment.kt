package com.infix.musicappv1.ui.discovery.artist.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.databinding.FragmentMoreArtistBinding
import com.infix.musicappv1.ui.adapter.artist.ArtistPagingDataAdapter
import com.infix.musicappv1.ui.discovery.artist.ArtistViewModel
import com.infix.musicappv1.ui.discovery.artist.detail.ArtistDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoreArtistFragment : Fragment() {
    private lateinit var adapter: ArtistPagingDataAdapter
    private lateinit var binding: FragmentMoreArtistBinding
    private val moreArtistViewModel: MoreArtistViewModel by activityViewModels()
    private val artistViewModel: ArtistViewModel by activityViewModels()
    private val artistDetailViewModel: ArtistDetailViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoreArtistBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbarMoreArtist.setNavigationOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
        initRecyclerView()
        setupPagingArtist()
    }

    private fun initRecyclerView() {
        adapter = ArtistPagingDataAdapter(
            object : ArtistPagingDataAdapter.OnArtistClick {
                override fun onClick(artist: Artist) {
                    artistDetailViewModel.setArtistWithSongsByArtistName(artist.id, artist.name)
                    findNavController().navigate(R.id.action_navigate_more_artist_to_navigate_detail_artist)
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

        binding.includeListArtists.rvArtist.adapter = adapter
    }

    private fun setupPagingArtist() {
       lifecycleScope.launch { moreArtistViewModel.artists.collectLatest { adapter.submitData(it) } }
    }
}