package com.infix.musicappv1.ui.discovery.artist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.data.source.local.db.MusicDatabase
import com.infix.musicappv1.databinding.FragmentArtistBinding
import com.infix.musicappv1.ui.adapter.artist.ArtistAdapter
import com.infix.musicappv1.ui.adapter.artist.ArtistPagingDataAdapter
import com.infix.musicappv1.ui.discovery.DiscoveryViewModel
import com.infix.musicappv1.ui.discovery.artist.detail.ArtistDetailViewModel
import com.infix.musicappv1.utils.InjectUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.getValue

class ArtistFragment : Fragment() {
    private lateinit var binding: FragmentArtistBinding
    private val adapter: ArtistPagingDataAdapter = ArtistPagingDataAdapter(
        object : ArtistPagingDataAdapter.OnArtistClick {
            override fun onClick(artist: Artist) {
                artistDetailViewModel.setArtistWithSongsByArtistName(artist.id, artist.name)
                findNavController().navigate(R.id.action_navigation_discovery_to_navigate_detail_artist)
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
    private val artistViewModel: ArtistViewModel by activityViewModels {
        ArtistViewModel.Factory(
            InjectUtils.getArtistRepository(requireContext().applicationContext),
            MusicDatabase.getInstance(requireContext().applicationContext)
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
        setupPagingArtist()
        setupEvent()
    }

    private fun setupEvent() {
        //more artist
        binding.tvLabelArtist.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_discovery_to_navigate_more_artist)
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