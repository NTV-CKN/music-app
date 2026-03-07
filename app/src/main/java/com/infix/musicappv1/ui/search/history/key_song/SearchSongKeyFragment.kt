package com.infix.musicappv1.ui.search.history.key_song

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.infix.musicappv1.R
import com.infix.musicappv1.databinding.FragmentSearchSongKeyBinding
import com.infix.musicappv1.ui.search.history.SearchSongHistoryFragmentDirections
import com.infix.musicappv1.ui.search.result.ResultSearchSongViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchSongKeyFragment : Fragment() {
    private lateinit var binding: FragmentSearchSongKeyBinding
    private lateinit var adapter: SearchSongKeyAdapter
    private var navController: NavController? = null
    private val searchSongKeyViewModel: SearchSongKeyViewModel by activityViewModels()
    private val resultSearchSongViewModel: ResultSearchSongViewModel by activityViewModels()

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
        navController = findNavController()
        initRecyclerView()
        observeData()
        binding.tvClearAll.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle(requireContext().getString(R.string.txt_confirm_clear_all_data))
                .setNegativeButton(requireContext().getString(R.string.txt_cancel)) { _, _ -> }
                .setPositiveButton(requireContext().getString(R.string.txt_agree)) { _, _ ->
                    searchSongKeyViewModel.clearAll()
                    adapter.updateSearchKeySongs(emptyList())
                }
                .create().show()
        }
    }

    private fun initRecyclerView() {
        adapter = SearchSongKeyAdapter { keySong ->
            navController ?: return@SearchSongKeyAdapter
            resultSearchSongViewModel.setKeySearch(keySong.key)
            if (navController!!.currentDestination!!.id == R.id.navigate_result_search_song) return@SearchSongKeyAdapter
            navController!!.navigate(SearchSongHistoryFragmentDirections.actionNavigateSearchSongHistoryToNavigateResultSearchSong())
        }

        binding.rvKeySearchSong.adapter = adapter
    }

    private fun observeData() {
        searchSongKeyViewModel.searchKeySongs.observe(viewLifecycleOwner) {
            adapter.updateSearchKeySongs(it)
        }
    }
}