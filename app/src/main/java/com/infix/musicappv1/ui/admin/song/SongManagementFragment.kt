package com.infix.musicappv1.ui.admin.song

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.infix.musicappv1.databinding.FragmentSongManagementBinding
import com.infix.musicappv1.ui.adapter.admin.SongAdminPagingDataAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SongManagementFragment : Fragment() {
    private lateinit var binding: FragmentSongManagementBinding
    private lateinit var adapter: SongAdminPagingDataAdapter

    private var searchJob: Job? = null

    private val songManagementViewModel by viewModels<SongManagementViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSongManagementBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeSongManagementVM()
        initRecyclerView()
        setupSearchSong()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchJob = null
    }

    private fun observeSongManagementVM() {
        //Song PagingData
        songManagementViewModel.songPagingData
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                adapter.submitData(it)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initRecyclerView() {
        adapter = SongAdminPagingDataAdapter(
            { song, pos -> },
            { song -> }
        )

        binding.rvSongs.adapter = adapter
        songManagementViewModel.setSongsPagingState("")
    }

    private fun setupSearchSong() {
        binding.edtSearch.doOnTextChanged { text, _, _, _ ->
            searchJob?.cancel()
            searchJob = viewLifecycleOwner.lifecycleScope.launch {
                delay(400)
                val query = text?.toString() ?: ""
                songManagementViewModel.setSongsPagingState(query)
            }
        }
    }
}