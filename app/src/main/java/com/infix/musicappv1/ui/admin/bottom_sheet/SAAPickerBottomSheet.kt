package com.infix.musicappv1.ui.admin.bottom_sheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.databinding.DialogFragmentSaaPickerBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SAAPickerBottomSheet<T : Any>(
    private val type: SAAPickerViewModel.TypeSAAPicker,
    private val onItemClick: (data: T) -> Unit
) : BottomSheetDialogFragment() {
    private lateinit var binding: DialogFragmentSaaPickerBinding
    private lateinit var adapter: SAAPickerPagingDataAdapter<T>

    private var searchJob: Job? = null

    private val saaPickerViewModel by activityViewModels<SAAPickerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogFragmentSaaPickerBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initializeAndObserve()
        setSearchEvent()
    }

    private fun initRecyclerView() {
        adapter = SAAPickerPagingDataAdapter { item ->
            onItemClick.invoke(item)
            dismiss()
        }

        binding.rvSongsSaaPicker.adapter = adapter
    }

    @Suppress("UNCHECKED_CAST")
    private fun initializeAndObserve() {
        when (type) {
            SAAPickerViewModel.TypeSAAPicker.SONG -> {
                binding.edtSearchSaaPicker.setText(saaPickerViewModel.getCurrentQuery(type))
                saaPickerViewModel.songPagingData
                    .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                    .onEach {
                        Log.d("SSI", "SONG")
                        adapter.submitData(it as PagingData<T>)
                    }
                    .launchIn(viewLifecycleOwner.lifecycleScope)
            }

            SAAPickerViewModel.TypeSAAPicker.ALBUM -> {
                binding.edtSearchSaaPicker.setText(saaPickerViewModel.getCurrentQuery(type))
                saaPickerViewModel.albumPagingData
                    .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                    .onEach {
                        Log.d("SSI", "ALBUM")

                        adapter.submitData(it as PagingData<T>)
                    }
                    .launchIn(viewLifecycleOwner.lifecycleScope)
            }

            SAAPickerViewModel.TypeSAAPicker.ARTIST -> {
                binding.edtSearchSaaPicker.setText(saaPickerViewModel.getCurrentQuery(type))
                saaPickerViewModel.artistPagingData
                    .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                    .onEach {
                        Log.d("SSI", "ARTIST")

                        adapter.submitData(it as PagingData<T>)
                    }
                    .launchIn(viewLifecycleOwner.lifecycleScope)
            }
        }
    }

    private fun setSearchEvent() {
        binding.edtSearchSaaPicker.doOnTextChanged { text, _, _, _ ->
            searchJob?.cancel()
            searchJob = viewLifecycleOwner.lifecycleScope.launch {
                delay(400)
                saaPickerViewModel.setQuerySearchState(text.toString(), type)
            }
        }
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun <T> openSaaPickerSheet(
            type: SAAPickerViewModel.TypeSAAPicker,
            fragmentManager: FragmentManager,
            onItemClick: (data: T) -> Unit
        ) {
            when (type) {
                SAAPickerViewModel.TypeSAAPicker.SONG -> {
                    SAAPickerBottomSheet(
                        SAAPickerViewModel.TypeSAAPicker.SONG,
                        onItemClick as (data: Song) -> Unit,
                    )
                        .show(fragmentManager, null)
                }

                SAAPickerViewModel.TypeSAAPicker.ALBUM -> {
                    SAAPickerBottomSheet(
                        SAAPickerViewModel.TypeSAAPicker.ALBUM,
                        onItemClick as (data: Album) -> Unit,
                    )
                        .show(fragmentManager, null)
                }

                SAAPickerViewModel.TypeSAAPicker.ARTIST -> {
                    SAAPickerBottomSheet(
                        SAAPickerViewModel.TypeSAAPicker.ARTIST,
                        onItemClick as (data: Artist) -> Unit,
                    )
                        .show(fragmentManager, null)
                }
            }
        }
    }
}