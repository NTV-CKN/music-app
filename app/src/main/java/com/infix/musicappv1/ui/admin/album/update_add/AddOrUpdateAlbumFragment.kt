package com.infix.musicappv1.ui.admin.album.update_add

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.databinding.FragmentAddOrUpdateAlbumBinding
import com.infix.musicappv1.ui.admin.bottom_sheet.SAAPickerBottomSheet
import com.infix.musicappv1.ui.admin.bottom_sheet.SAAPickerViewModel
import com.infix.musicappv1.ui.dialog.LoadingDialogFragment
import com.infix.musicappv1.utils.SnackbarUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class AddOrUpdateAlbumFragment : Fragment() {
    private lateinit var binding: FragmentAddOrUpdateAlbumBinding
    private lateinit var loadingDialogFragment: LoadingDialogFragment
    private lateinit var saaPickerBottomSheet: SAAPickerBottomSheet<Song>

    //artwork
    private var selectedImageUri: Uri? = null
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            Glide.with(binding.root)
                .load(it)
                .into(binding.ivArtwork)
            addOrUpdateAlbumVM.params.value?.album?.artwork = selectedImageUri.toString()
        }
    }

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
        selectedSongAdapter = SelectedSongAdapter { song -> removeSong(song)}

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
                R.string.txt_size_album_args,
                album.size.toString()
            )

            if (album.artwork.isNotEmpty())
                Glide.with(root.context)
                    .load(album.artwork)
                    .error(R.drawable.ic_song_24)
                    .into(ivArtwork)

            selectedSongAdapter?.updateSongs(params.songs)
        }
    }

    private fun setEvents() {
        //photo picker
        binding.ivArtwork.setOnClickListener {
            pickImageLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        //title
        binding.edtAlbumName.doOnTextChanged { text, _, _, _ ->
            addOrUpdateAlbumVM.params.value?.album?.name = text.toString().trim()
        }

        //add song
        binding.btnAddSong.setOnClickListener {
            try {
                saaPickerBottomSheet.show(requireActivity().supportFragmentManager, null)
            } catch (_: Exception) {
            }
        }

        handleSaveAlbum()
    }

    private fun handleSaveAlbum() {
        binding.btnSave.setOnClickListener {
            val error = addOrUpdateAlbumVM.validateAlbum(selectedSongs)
            if (error != null) {
                handleValidationError(error)
                return@setOnClickListener
            }

//             addOrUpdateAlbumVM.saveAlbum(selectedSongs) { resultResponse ->
//                SnackbarUtils.showBaseSnackbar(
//                    binding.root,
//                    resultResponse.message,
//                    Snackbar.LENGTH_SHORT
//                )
//
//                if (resultResponse.success) {
//                    requireActivity().onBackPressedDispatcher.onBackPressed()
//                }
//            }
        }
    }

    private fun handleValidationError(error: AddOrUpdateAlbumViewModel.ValidationError) {
        when (error) {
            is AddOrUpdateAlbumViewModel.ValidationError.EmptyTitle -> {
                binding.edtAlbumName.error = getString(R.string.error_name_album_empty)
                binding.edtAlbumName.requestFocus()
                SnackbarUtils.showBaseSnackbar(
                    binding.root,
                    getString(R.string.error_name_album_empty),
                    Snackbar.LENGTH_SHORT
                )
            }

            is AddOrUpdateAlbumViewModel.ValidationError.EmptyImage -> {
                SnackbarUtils.showBaseSnackbar(
                    binding.root,
                    getString(R.string.error_artwork_album_empty),
                    Snackbar.LENGTH_SHORT
                )
            }

            is AddOrUpdateAlbumViewModel.ValidationError.EmptySongList -> {
                SnackbarUtils.showBaseSnackbar(
                    binding.root,
                    getString(R.string.error_songs_album_empty),
                    Snackbar.LENGTH_SHORT
                )
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