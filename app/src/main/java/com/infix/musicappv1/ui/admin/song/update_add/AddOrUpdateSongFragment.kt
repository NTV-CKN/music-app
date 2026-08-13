package com.infix.musicappv1.ui.admin.song.update_add

import android.R
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.databinding.FragmentAddOrUpdateSongBinding
import com.infix.musicappv1.enums.Genre
import com.infix.musicappv1.ui.admin.bottom_sheet.SAAPickerBottomSheet
import com.infix.musicappv1.ui.admin.bottom_sheet.SAAPickerViewModel
import com.infix.musicappv1.ui.dialog.LoadingDialogFragment
import com.infix.musicappv1.utils.FormatSongPathUtils
import com.infix.musicappv1.utils.MusicAppUtils.getAudioDurationInSeconds
import com.infix.musicappv1.utils.SnackbarUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class AddOrUpdateSongFragment : Fragment() {
    private lateinit var binding: FragmentAddOrUpdateSongBinding
    private val addOrUpdateSongViewModel by activityViewModels<AddOrUpdateSongViewModel>()

    private lateinit var loadingDialogFragment: LoadingDialogFragment

    //File picker
    private var selectedAudioUri: Uri? = null
    private val pickAudioLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedAudioUri = it
            binding.edtSource.setText(it.toString())
            val durationInSeconds = getAudioDurationInSeconds(requireContext(), it)

            addOrUpdateSongViewModel.params.value?.song?.apply {
                source = selectedAudioUri.toString()
                duration = durationInSeconds.toInt()
            }
        }
    }

    //Photo picker
    private var selectedImageUri: Uri? = null
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            binding.ivCover.apply {
                setPadding(0, 0, 0, 0)
                setImageURI(it)
            }
            addOrUpdateSongViewModel.params.value?.song?.image = selectedImageUri.toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddOrUpdateSongBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialogFragment = LoadingDialogFragment()

        observeAddOrUpdateVM()
        setupEvents()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        addOrUpdateSongViewModel.setIsUpdateSongState(null)
    }

    private fun observeAddOrUpdateVM() {
        //Song and Option update
        addOrUpdateSongViewModel.params
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                if (it != null)
                    handleAddOrUpdateParams(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        //Is loading
        addOrUpdateSongViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading == null) return@observe

            try {
                if (isLoading)
                    loadingDialogFragment.show(requireActivity().supportFragmentManager, null)
                else
                    loadingDialogFragment.dismissNow()
            } catch (_: Exception) {
            }
        }
    }

    private fun setupEvents() {
        initializeForm()

        //edt name song
        binding.edtTitle.doOnTextChanged { text, _, _, _ ->
            addOrUpdateSongViewModel.params.value?.song?.title = text.toString()
        }

        //image song
        setupPhotoPicker()

        //select artist
        binding.tvArtist.setOnClickListener {
            openSaaPickerSheet<Artist>(SAAPickerViewModel.TypeSAAPicker.ARTIST) { artist ->
                binding.tvArtist.text = artist.name
                addOrUpdateSongViewModel.params.value?.song?.artist = artist.name
                addOrUpdateSongViewModel.params.value?.song?.artistId = artist.id
            }
        }

        //select album
        binding.tvAlbum.setOnClickListener {
            openSaaPickerSheet<Album>(SAAPickerViewModel.TypeSAAPicker.ALBUM) { album ->
                binding.tvAlbum.text = album.name
                addOrUpdateSongViewModel.params.value?.song?.album = album.name
            }
        }

        //edt path/url song
        setupAudioPicker()

        //energy
        setupEnergyDrag()

        //vip
        setupSwitchVipClick()

        //save song
        handleSaveSong()
    }

    private fun initializeForm() {
        //spinner genre
        initializeAndHandleSpinnerGenre()
    }

    private fun initializeAndHandleSpinnerGenre() {
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_spinner_item,
            Genre.entries
        ).apply {
            setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        }
        binding.spGenre.adapter = adapter

        //event
        binding.spGenre.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedGenre = Genre.entries[position]
                addOrUpdateSongViewModel.params.value?.song?.genre = selectedGenre.name
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun handleAddOrUpdateParams(
        addOrUpdateSongParams: AddOrUpdateSongViewModel.AddOrUpdateSongParams
    ) {
        bindSongData(addOrUpdateSongParams.song)
    }

    private fun setupPhotoPicker() {
        binding.ivCover.setOnClickListener {
            pickImageLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    }

    private fun <T> openSaaPickerSheet(
        type: SAAPickerViewModel.TypeSAAPicker,
        onItemClick: (data: T) -> Unit
    ) {
        SAAPickerBottomSheet.openSaaPickerSheet(
            type,
            requireActivity().supportFragmentManager,
            onItemClick
        )
    }

    private fun setupAudioPicker() {
        //show picker
        binding.tilSource.setEndIconOnClickListener {
            pickAudioLauncher.launch("audio/*")
        }

        //on text change
        binding.edtSource.doOnTextChanged { text, _, _, _ ->
            val str = text.toString()
            if (!str.isEmpty() && !FormatSongPathUtils.isValidUriOrUrl(str)) {
                binding.tilSource.error = getString(com.infix.musicappv1.R.string.txt_error_format)
            } else {
                binding.tilSource.error = null
            }
        }
    }

    private fun setupEnergyDrag() {
        binding.sliderEnergy.addOnChangeListener { _, value, fromUser ->
            if (fromUser) {
                addOrUpdateSongViewModel.params.value?.song?.energy = value
            }
        }
    }

    private fun setupSwitchVipClick() {
        binding.switchIsVip.setOnCheckedChangeListener { _, isChecked ->
            addOrUpdateSongViewModel.params.value?.song?.isVip = isChecked
        }
    }

    private fun handleSaveSong() {
        binding.btnSave.setOnClickListener {
            val error = addOrUpdateSongViewModel.validateSong()
            if (error != null) {
                handleValidationError(error)
                return@setOnClickListener
            }

            addOrUpdateSongViewModel.saveSong { resultResponse ->
                SnackbarUtils.showBaseSnackbar(
                    binding.root,
                    resultResponse.message,
                    Snackbar.LENGTH_SHORT
                )

                if (resultResponse.success) {
                    val params = addOrUpdateSongViewModel.params.value
                    if (params != null && !params.isUpdate)
                        addOrUpdateSongViewModel.setIsUpdateSongState(
                            AddOrUpdateSongViewModel.AddOrUpdateSongParams(
                                false,
                                Song()
                            )
                        )
                }
            }
        }
    }

    private fun handleValidationError(error: AddOrUpdateSongViewModel.ValidationError) {
        when (error) {
            is AddOrUpdateSongViewModel.ValidationError.EmptyImage -> {
                SnackbarUtils.showBaseSnackbar(
                    binding.root,
                    getString(com.infix.musicappv1.R.string.error_image_song_empty),
                    Snackbar.LENGTH_SHORT
                )
            }

            is AddOrUpdateSongViewModel.ValidationError.EmptyTitle -> {
                val message = getString(com.infix.musicappv1.R.string.error_title_song_empty)
                binding.edtTitle.error = message
                binding.edtTitle.requestFocus()
                SnackbarUtils.showBaseSnackbar(
                    binding.root,
                    message,
                    Snackbar.LENGTH_SHORT
                )
            }

            is AddOrUpdateSongViewModel.ValidationError.InvalidArtist -> {
                SnackbarUtils.showBaseSnackbar(
                    binding.root,
                    getString(com.infix.musicappv1.R.string.error_artist_song_empty),
                    Snackbar.LENGTH_SHORT
                )
            }

            is AddOrUpdateSongViewModel.ValidationError.InvalidGenre -> {
                SnackbarUtils.showBaseSnackbar(
                    binding.root,
                    getString(com.infix.musicappv1.R.string.error_genre_song_empty),
                    Snackbar.LENGTH_SHORT
                )
            }

            is AddOrUpdateSongViewModel.ValidationError.EmptySource -> {
                val message = getString(com.infix.musicappv1.R.string.error_source_song_empty)
                binding.tilSource.error = message
                binding.edtSource.requestFocus()
                SnackbarUtils.showBaseSnackbar(
                    binding.root,
                    message,
                    Snackbar.LENGTH_SHORT
                )
            }

            AddOrUpdateSongViewModel.ValidationError.EmptyAlbum -> {
                SnackbarUtils.showBaseSnackbar(
                    binding.root,
                    getString(com.infix.musicappv1.R.string.error_album_song_empty),
                    Snackbar.LENGTH_SHORT
                )
            }
        }
    }

    //initialize song data
    private fun bindSongData(song: Song) {
        binding.edtTitle.setText(song.title)
        binding.tvArtist.text = song.artist.ifBlank {
            getString(com.infix.musicappv1.R.string.txt_pick_artist)
        }

        binding.tvAlbum.text = song.album.ifBlank {
            getString(com.infix.musicappv1.R.string.txt_pick_album)
        }

        val genreIndex = Genre.entries.indexOfFirst {
            it.name.equals(song.genre, ignoreCase = true)
        }

        if (genreIndex >= 0) {
            binding.spGenre.setSelection(genreIndex)
        }

        if (song.source.isBlank()) {
            selectedAudioUri = null
            binding.edtSource.setText("")
        } else {
            selectedAudioUri = song.source.toUri()
            binding.edtSource.setText(song.source)
        }

        if (song.image.isBlank()) {
            selectedImageUri = null
        } else {
            selectedImageUri = song.image.toUri()
            Glide.with(binding.root)
                .load(song.image)
                .error(com.infix.musicappv1.R.drawable.ic_song_24)
                .into(binding.ivCover)
        }

        binding.sliderEnergy.value = song.energy.coerceIn(0f, 1f)
        binding.switchIsVip.isChecked = song.isVip
    }
}