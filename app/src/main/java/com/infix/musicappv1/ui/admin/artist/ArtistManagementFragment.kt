package com.infix.musicappv1.ui.admin.artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.databinding.FragmentArtistManagementBinding
import com.infix.musicappv1.ui.adapter.admin.ArtistAdminPagingDataAdapter
import com.infix.musicappv1.ui.admin.artist.add_update.AddOrUpdateArtistViewModel
import com.infix.musicappv1.ui.dialog.CRUDOptionDialog
import com.infix.musicappv1.ui.dialog.LoadingDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArtistManagementFragment : Fragment() {
    private lateinit var binding: FragmentArtistManagementBinding
    private lateinit var loadingDialogFragment: LoadingDialogFragment
    private lateinit var adapter: ArtistAdminPagingDataAdapter
    private lateinit var crudOptionDialog: CRUDOptionDialog<Artist>

    private var searchJob: Job? = null

    private val artistManagementVM by viewModels<ArtistManagementViewModel>()
    private val addOrUpdateArtistVM by activityViewModels<AddOrUpdateArtistViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArtistManagementBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialogFragment = LoadingDialogFragment()

        initializeRv()
        setupCrudOptionDialog()
        observeArtistManagementVM()
        setupEvents()
    }

    private fun initializeRv() {
        adapter = ArtistAdminPagingDataAdapter(
            { artist ->
                crudOptionDialog.setData(artist)
                crudOptionDialog.show(requireActivity().supportFragmentManager, null)
            }
        )

        binding.rvArtists.adapter = adapter
    }

    private fun setupCrudOptionDialog() {
        crudOptionDialog = CRUDOptionDialog(
            onUpdate = { artist ->
                addOrUpdateArtistVM.setArtistParamsState(
                    AddOrUpdateArtistViewModel.AddOrUpdateArtistParams(
                        isUpdate = true,
                        artist = artist.clone()
                    )
                )

                findNavController().navigate(
                    ArtistManagementFragmentDirections.actionNavigateManageArtistsToNavigateAddOrUpdateArtist(
                        R.string.txt_update_artist
                    )
                )
            },
            onDelete = { artist -> },
            onView = { artist -> }
        )
    }

    private fun observeArtistManagementVM() {
        //is loading
        artistManagementVM.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading == null) return@observe
            try {
                if (isLoading)
                    loadingDialogFragment.show(requireActivity().supportFragmentManager, null)
                else
                    loadingDialogFragment.dismissNow()
            } catch (_: Exception) {
            }
        }

        //artists
        artistManagementVM.artists
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { adapter.submitData(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setupEvents() {
        //search
        binding.edtSearch.doOnTextChanged { text, _, _, _ ->
            searchJob?.cancel()
            searchJob = viewLifecycleOwner.lifecycleScope.launch {
                delay(400)
                artistManagementVM.setQuerySearchState(text.toString())
            }
        }

        //swipe refresh
        binding.swipeRefreshLayout.setOnRefreshListener {
            artistManagementVM.setQuerySearchState(
                artistManagementVM.currentQuery.value.query
            )
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }
}