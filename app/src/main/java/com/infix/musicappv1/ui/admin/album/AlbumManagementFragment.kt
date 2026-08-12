package com.infix.musicappv1.ui.admin.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.infix.musicappv1.databinding.FragmentAlbumManagementBinding
import com.infix.musicappv1.ui.adapter.admin.AlbumAdminPagingDataAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumManagementFragment : Fragment() {
    private lateinit var binding: FragmentAlbumManagementBinding
    private lateinit var adapter: AlbumAdminPagingDataAdapter

    private val albumManagementVM by viewModels<AlbumManagementViewModel>()

    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumManagementBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeRv()
        observeAlbumManagementVM()
        setEvents()
    }

    private fun initializeRv() {
        adapter = AlbumAdminPagingDataAdapter(
            { album -> }
        )

        binding.rvAlbums.adapter = adapter
    }

    private fun observeAlbumManagementVM() {
        //albums
        albumManagementVM.albums
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { adapter.submitData(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setEvents() {
        binding.edtSearch.doOnTextChanged { text, _, _, _ ->
            searchJob?.cancel()
            searchJob = viewLifecycleOwner.lifecycleScope.launch {
                delay(400)
                albumManagementVM.setQuerySearchState(text.toString())
            }
        }
    }
}