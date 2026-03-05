package com.infix.musicappv1.ui.discovery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.infix.musicappv1.databinding.FragmentDiscoveryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiscoveryFragment : Fragment() {
    private lateinit var binding: FragmentDiscoveryBinding
    private val discoveryViewModel: DiscoveryViewModel by activityViewModels()

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
        setupEvent()
    }

    private fun setupEvent() {
        binding.btnSearchDiscovery.setOnClickListener {
            findNavController().navigate(DiscoveryFragmentDirections.actionNavigationDiscoveryToNavigateSearchSong())
        }
    }
}