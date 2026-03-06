package com.infix.musicappv1.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.search.SearchKeySong
import com.infix.musicappv1.databinding.FragmentSearchSongBinding
import com.infix.musicappv1.ui.search.history.SearchSongHistoryFragmentDirections
import com.infix.musicappv1.ui.search.history.key_song.SearchSongKeyViewModel
import com.infix.musicappv1.ui.search.result.ResultSearchSongFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchSongFragment : Fragment() {
    private lateinit var binding: FragmentSearchSongBinding
    private lateinit var searchView: SearchView
    private var navController: NavController? = null
    private var navControllerOfParentFragment: NavController? = null
    //use this viewmodel to save key when user click submit in keyboard
    private val searchSongKeyViewModel: SearchSongKeyViewModel by activityViewModels()

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
            if (navControllerOfParentFragment != null)
                navControllerOfParentFragment!!.popBackStack()
        }
        setupNavController()
        setupOnQueryTextListener()
    }

    private fun setupOnQueryTextListener() {
        searchView = binding.toolbarSearchSong.findViewById(R.id.search_view_search_song)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query != null && query.isNotEmpty()) {
                    searchSongKeyViewModel.insert(SearchKeySong(key = query))
                }
                return true
            }

            //avoid when click toolbar but text still exist => crash if user click close icon
            override fun onQueryTextChange(newText: String?): Boolean {
                if (navController == null) return false
                if (newText == null || newText.isEmpty()) {
                    if (navController!!.currentDestination?.id == R.id.navigate_search_song_history) return true
                    navController!!.navigate(
                        ResultSearchSongFragmentDirections.actionNavigateResultSearchSongToNavigateSearchSongHistory()
                    )
                } else {
                    if (navController!!.currentDestination?.id == R.id.navigate_result_search_song) return true
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

        //get nav host in activity. Cause this fragment  is contained in navhost of activity, we
        //get parentFragment and cast to NavHostFragment to get navController
        navControllerOfParentFragment = (parentFragment as? NavHostFragment)?.navController
    }
}