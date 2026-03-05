package com.infix.musicappv1.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.infix.musicappv1.R
import com.infix.musicappv1.databinding.FragmentSearchSongBinding
import com.infix.musicappv1.ui.search.history.SearchSongHistoryFragmentDirections
import com.infix.musicappv1.ui.search.result.ResultSearchSongFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchSongFragment : Fragment() {
    private lateinit var binding: FragmentSearchSongBinding
    private lateinit var searchView: SearchView
    private var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchSongBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbarSearchSong.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        setupNavController()
        setupOnQueryTextListener()
    }

    private fun setupOnQueryTextListener() {
        searchView = binding.toolbarSearchSong.findViewById(R.id.search_view_search_song)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (navController == null) return false
                if (newText == null || newText.isEmpty())
                    navController!!.navigate(
                        ResultSearchSongFragmentDirections.actionNavigateResultSearchSongToNavigateSearchSongHistory()
                    )
                else
                    if (navController!!.currentDestination?.id != R.id.navigate_result_search_song) {
                        navController!!.navigate(
                            SearchSongHistoryFragmentDirections.actionNavigateSearchSongHistoryToNavigateResultSearchSong()
                        )
                    }

                return true
            }
        })
    }

    private fun setupNavController() {
        val navHost = childFragmentManager.findFragmentById(R.id.nav_host_fragment_search_song)
        navController = navHost?.findNavController()
    }
}