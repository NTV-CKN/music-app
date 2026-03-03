package com.infix.musicappv1.ui.discovery.interest_artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.databinding.FragmentInterestArtistBinding
import com.infix.musicappv1.ui.adapter.artist.ArtistAdapter
import com.infix.musicappv1.ui.discovery.DiscoveryFragmentDirections
import com.infix.musicappv1.ui.discovery.artist.ArtistViewModel
import com.infix.musicappv1.ui.discovery.artist.detail.ArtistDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InterestArtistFragment : Fragment() {
    private lateinit var binding: FragmentInterestArtistBinding
    private lateinit var adapter: ArtistAdapter
    private val interestArtistViewModel: InterestArtistViewModel by activityViewModels()
    private val artistViewModel: ArtistViewModel by activityViewModels()

    private val artistDetailViewModel: ArtistDetailViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInterestArtistBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressInterestedArtist.visibility = View.VISIBLE
        initRecyclerView()
        setupObserve()
    }

    private fun initRecyclerView() {
        adapter = ArtistAdapter(
            object : ArtistAdapter.OnArtistClick {
                override fun onClick(artist: Artist) {
                    artistDetailViewModel.setArtistWithSongsByArtistName(artist.id, artist.name)
                    findNavController().navigate(DiscoveryFragmentDirections.actionNavigationDiscoveryToNavigateDetailArtist())
                }
            },
            object : ArtistAdapter.OnInterestClick {
                override fun onClick(artist: Artist) {
                    artistViewModel.updateInterestedArtist(
                        artist.apply { this.isInterested = !this.isInterested }
                    )
                }
            }
        )

        binding.includeListArtists.rvArtist.adapter = adapter
    }

    private fun setupObserve() {
        interestArtistViewModel.artistsInterested.observe(viewLifecycleOwner) {
            adapter.updateArtists(it ?: emptyList())
            binding.progressInterestedArtist.visibility = View.GONE
        }
    }
}