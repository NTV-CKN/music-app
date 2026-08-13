package com.infix.musicappv1.ui.admin.album.update_add

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.databinding.FragmentAddOrUpdateAlbumBinding
import com.infix.musicappv1.ui.admin.bottom_sheet.SAAPickerBottomSheet
import com.infix.musicappv1.ui.admin.bottom_sheet.SAAPickerViewModel
import com.infix.musicappv1.ui.dialog.LoadingDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class AddOrUpdateAlbumFragment : Fragment() {
    private lateinit var binding: FragmentAddOrUpdateAlbumBinding
    private lateinit var loadingDialogFragment: LoadingDialogFragment
    private lateinit var saaPickerBottomSheet: SAAPickerBottomSheet<Song>

    private val addOrUpdateAlbumVM by activityViewModels<AddOrUpdateAlbumViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddOrUpdateAlbumBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialogFragment = LoadingDialogFragment()

        initializeSaaPicker()
        initRvSelectedSong()
        observeAddOrUpdateAlbumVM()
        setEvents()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clear()
        selectedSongAdapter = null
    }

    private fun initializeSaaPicker() {
        saaPickerBottomSheet = SAAPickerBottomSheet(
            SAAPickerViewModel.TypeSAAPicker.SONG,
            { _ -> }
        )
    }

    private fun initRvSelectedSong() {
        selectedSongAdapter = SelectedSongAdapter { song -> }

        binding.rvSelectedSongs.adapter = selectedSongAdapter
    }

    private fun observeAddOrUpdateAlbumVM() {
        //is loading
        addOrUpdateAlbumVM.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading == null) return@observe

            try {
                if (isLoading)
                    loadingDialogFragment.show(requireActivity().supportFragmentManager, null)
                else
                    loadingDialogFragment.dismissNow()

            } catch (_: Exception) {
            }
        }

        //params
        addOrUpdateAlbumVM.params
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { params ->
                if (params != null) {
                    initAlbumForm(params)
                    clear()
                    addAll(params.songs)
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    @SuppressLint("SuspiciousIndentation", "StringFormatMatches")
    fun initAlbumForm(params: AddOrUpdateAlbumViewModel.AddOrUpdateAlbumParams) {
        val album = params.album

        binding.apply {
            edtAlbumName.setText(album.name)
            tvAlbumSize.text = root.context.getString(
                com.infix.musicappv1.R.string.txt_size_album_args,
                album.size.toString()
            )

            if (album.artwork.isNotEmpty())
                Glide.with(root.context)
                    .load(album.artwork)
                    .error(com.infix.musicappv1.R.drawable.ic_song_24)
                    .into(ivArtwork)

            selectedSongAdapter?.updateSongs(params.songs)
        }
    }

    private fun setEvents() {
        binding.btnAddSong.setOnClickListener {
            try {
                saaPickerBottomSheet.show(requireActivity().supportFragmentManager, null)
            } catch (_: Exception) {
            }
        }
    }

    companion object {
        private val _selectedSongs = mutableListOf<Song>()

        val selectedSongs: List<Song> = _selectedSongs
        //track selected songs to update or add
        var selectedSongAdapter: SelectedSongAdapter? = null

        private fun addAll(songs: MutableList<Song>) {
            _selectedSongs.addAll(songs)
        }

        fun checkContainSong(song: Song) = selectedSongs.contains(song)

        fun removeSong(song: Song) {
            val isSuccess = _selectedSongs.remove(song)
            if (isSuccess) {
                selectedSongAdapter?.updateSongs(selectedSongs)
            }
        }

        //return true if add success
        fun addSong(song: Song): Boolean {
            if (checkContainSong(song)) {
                return false
            }

            _selectedSongs.add(song)
            selectedSongAdapter?.updateSongs(selectedSongs)
            return true
        }

        fun clear() {
            _selectedSongs.clear()
        }
    }
}