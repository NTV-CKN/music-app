package com.infix.musicappv1.ui.discovery.artist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.databinding.FragmentArtistBinding
import com.infix.musicappv1.ui.discovery.DiscoveryViewModel
import com.infix.musicappv1.ui.discovery.artist.detail.ArtistDetailViewModel
import com.infix.musicappv1.utils.InjectUtils
import java.lang.Exception
import kotlin.getValue

class ArtistFragment : Fragment() {
    private lateinit var binding: FragmentArtistBinding
    private lateinit var adapter: ArtistAdapter
    private val discoveryViewModel: DiscoveryViewModel by activityViewModels {
        DiscoveryViewModel.Factory(
            InjectUtils.getArtistRepository(requireContext().applicationContext),
            InjectUtils.getSongRepository(requireContext().applicationContext)
        )
    }
    private val artistDetailViewModel: ArtistDetailViewModel by activityViewModels {
        ArtistDetailViewModel.Factory(InjectUtils.getArtistRepository(requireContext().applicationContext))
    }

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
        setupObserve()
        setupEvent()
    }

    private fun setupEvent() {
        //more artist
        binding.tvLabelArtist.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_discovery_to_navigate_more_artist)
        }
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

                }
            }
        )

        binding.includeListArtists.rvArtist.adapter = adapter
    }

    private fun setupObserve() {
        discoveryViewModel.artists.observe(viewLifecycleOwner) {
            val sublist = try {
                it?.subList(0, 10) ?: emptyList()
            } catch (_: Exception) {
                emptyList()
            }
            adapter.updateArtists(sublist)
            binding.progressArtist.visibility = View.GONE
        }
    }
}