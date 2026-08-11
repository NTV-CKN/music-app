package com.infix.musicappv1.ui.admin.song

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
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.databinding.FragmentSongManagementBinding
import com.infix.musicappv1.ui.adapter.admin.SongAdminPagingDataAdapter
import com.infix.musicappv1.ui.admin.song.update_add.AddOrUpdateSongViewModel
import com.infix.musicappv1.ui.dialog.CRUDOptionDialog
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
    private lateinit var crudOptionDialog: CRUDOptionDialog<Song>
    private lateinit var navController: NavController

    private var searchJob: Job? = null

    private val songManagementViewModel by viewModels<SongManagementViewModel>()
    private val addOrUpdateSongViewModel by activityViewModels<AddOrUpdateSongViewModel>()

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
        navController = findNavController()

        initCrudOptDialog()
        observeSongManagementVM()
        initRecyclerView()
        setupSearchSong()
        setupEvents()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchJob = null
    }

    private fun initCrudOptDialog() {
        //add logic items selected for crudOptionDialog
        crudOptionDialog = CRUDOptionDialog(
            onUpdate = { song ->
                addOrUpdateSongViewModel.setIsUpdateSongState(
                    AddOrUpdateSongViewModel.AddOrUpdateSongParams(
                        true,
                        song.clone()
                    )
                )

                findNavController().navigate(
                    SongManagementFragmentDirections.actionNavigateSongManagementToAddOrUpdateSong(
                        R.string.txt_update_song
                    )
                )
            },
            onView = { song -> },
            onDelete = { song -> }
        )
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
            { song -> showCRUDDialog(song) }
        )

        binding.rvSongs.adapter = adapter
        songManagementViewModel.setSongsPagingState("")
    }

    private fun showCRUDDialog(song: Song) {
        crudOptionDialog.setData(song)
        crudOptionDialog.show(requireActivity().supportFragmentManager, null)
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

    private fun setupEvents() {
        //FAB Add song
        binding.fabAddSong.setOnClickListener {
            addOrUpdateSongViewModel.setIsUpdateSongState(
                AddOrUpdateSongViewModel.AddOrUpdateSongParams(
                    isUpdate = false
                )
            )

            val action = SongManagementFragmentDirections
                .actionNavigateSongManagementToAddOrUpdateSong(
                    addOrUpdate = R.string.txt_add_song
                )
            navController.navigate(action)
        }
    }
}