package com.infix.musicappv1.ui.search.history.key_song

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.infix.musicappv1.databinding.FragmentSearchSongKeyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchSongKeyFragment : Fragment() {
    private lateinit var binding: FragmentSearchSongKeyBinding
    private lateinit var adapter: SearchSongKeyAdapter
    private val searchSongKeyViewModel: SearchSongKeyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchSongKeyBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observeData()
        binding.tvClearAll.setOnClickListener {
            searchSongKeyViewModel.clearAll()
            adapter.updateSearchKeySongs(emptyList())
        }
    }

    private fun initRecyclerView() {
        adapter = SearchSongKeyAdapter {}

        binding.rvKeySearchSong.adapter = adapter
    }

    private fun observeData() {
        searchSongKeyViewModel.searchKeySongs.observe(viewLifecycleOwner) {
            adapter.updateSearchKeySongs(it)
        }
    }
}