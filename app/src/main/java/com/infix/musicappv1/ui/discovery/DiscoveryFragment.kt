package com.infix.musicappv1.ui.discovery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.databinding.FragmentDiscoveryBinding
import com.infix.musicappv1.ui.discovery.artist.ArtistAdapter
import com.infix.musicappv1.utils.InjectUtils

class DiscoveryFragment : Fragment() {
    private lateinit var binding: FragmentDiscoveryBinding
    private val discoveryViewModel: DiscoveryViewModel by activityViewModels {
        DiscoveryViewModel.Factory(
            InjectUtils.getArtistRepository(requireContext().applicationContext),
            InjectUtils.getSongRepository(requireContext().applicationContext)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiscoveryBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        discoveryViewModel.loadArtistsRemote()
    }
}