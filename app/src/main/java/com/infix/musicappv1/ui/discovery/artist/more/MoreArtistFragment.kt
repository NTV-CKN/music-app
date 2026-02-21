package com.infix.musicappv1.ui.discovery.artist.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.databinding.FragmentMoreArtistBinding
import com.infix.musicappv1.ui.discovery.artist.ArtistAdapter
import com.infix.musicappv1.ui.discovery.artist.ArtistViewModel
import com.infix.musicappv1.ui.discovery.artist.detail.ArtistDetailViewModel
import com.infix.musicappv1.utils.InjectUtils

class MoreArtistFragment : Fragment() {
    private lateinit var adapter: ArtistAdapter
    private lateinit var binding: FragmentMoreArtistBinding
    private val moreArtistViewModel: MoreArtistViewModel by activityViewModels {
        MoreArtistViewModel.Factory(InjectUtils.getArtistRepository(requireContext().applicationContext))
    }
    private val artistViewModel: ArtistViewModel by activityViewModels {
        ArtistViewModel.Factory(
            InjectUtils.getArtistRepository(requireContext().applicationContext)
        )
    }

    private val artistDetailViewModel: ArtistDetailViewModel by activityViewModels {
        ArtistDetailViewModel.Factory(InjectUtils.getArtistRepository(requireContext().applicationContext))
    }


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
        setupObserve()
    }

    private fun initRecyclerView() {
        adapter = ArtistAdapter(
            object : ArtistAdapter.OnArtistClick {
                override fun onClick(artist: Artist) {
                    artistDetailViewModel.setArtistWithSongsByArtistId(artist.id)
                    findNavController().navigate(R.id.action_navigation_discovery_to_navigate_detail_artist)
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
        moreArtistViewModel.artists.observe(viewLifecycleOwner) {
            adapter.updateArtists(it ?: emptyList())
        }
    }
}