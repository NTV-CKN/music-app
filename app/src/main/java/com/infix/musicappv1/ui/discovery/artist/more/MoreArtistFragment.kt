package com.infix.musicappv1.ui.discovery.artist.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.databinding.FragmentMoreArtistBinding
import com.infix.musicappv1.ui.discovery.artist.ArtistAdapter
import com.infix.musicappv1.utils.InjectUtils

class MoreArtistFragment : Fragment() {
    private lateinit var adapter: ArtistAdapter
    private lateinit var binding: FragmentMoreArtistBinding
    private val moreArtistViewModel: MoreArtistViewModel by activityViewModels {
        MoreArtistViewModel.Factory(InjectUtils.getArtistRepository(requireContext().applicationContext))
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
        moreArtistViewModel.artists.observe(viewLifecycleOwner) {
            adapter.updateArtists(it ?: emptyList())
        }
    }
}