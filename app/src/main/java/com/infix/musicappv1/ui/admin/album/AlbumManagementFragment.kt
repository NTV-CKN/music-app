package com.infix.musicappv1.ui.admin.album

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
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.databinding.FragmentAlbumManagementBinding
import com.infix.musicappv1.ui.adapter.admin.AlbumAdminPagingDataAdapter
import com.infix.musicappv1.ui.admin.album.update_add.AddOrUpdateAlbumViewModel
import com.infix.musicappv1.ui.dialog.CRUDOptionDialog
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
    private lateinit var crudOptionDialog: CRUDOptionDialog<Album>

    private val albumManagementVM by viewModels<AlbumManagementViewModel>()
    private val addOrUpdateAlbumVM by activityViewModels<AddOrUpdateAlbumViewModel>()

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

        initializeCrudOptionDialog()
        initializeRv()
        observeAlbumManagementVM()
        setEvents()
    }

    private fun initializeCrudOptionDialog() {
        crudOptionDialog = CRUDOptionDialog(
            onView = { album -> },
            onUpdate = { album ->
                addOrUpdateAlbumVM.setAlbumParamsState(
                    AddOrUpdateAlbumViewModel.AddOrUpdateAlbumParams(
                        isUpdate = true,
                        album
                    )
                )

                findNavController().navigate(
                    AlbumManagementFragmentDirections.actionNavigateManageAlbumsToNavigateAddOrUpdateAlbum(
                        R.string.txt_update_album
                    )
                )
            },
            onDelete = { album -> }
        )
    }

    private fun initializeRv() {
        adapter = AlbumAdminPagingDataAdapter(
            { album ->
                crudOptionDialog.setData(album)
                try {
                    crudOptionDialog.show(requireActivity().supportFragmentManager, null)
                }catch (_: Exception){}
            }
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