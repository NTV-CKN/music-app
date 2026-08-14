package com.infix.musicappv1.ui.admin.artist.add_update

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.infix.musicappv1.R
import com.infix.musicappv1.databinding.FragmentAddOrUpdateArtistBinding
import com.infix.musicappv1.ui.dialog.LoadingDialogFragment
import com.infix.musicappv1.utils.GenerateIdHelper
import com.infix.musicappv1.utils.SnackbarUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class AddOrUpdateArtistFragment : Fragment() {
    private lateinit var binding: FragmentAddOrUpdateArtistBinding
    private lateinit var loadingDialogFragment: LoadingDialogFragment

    // Avatar Photo Picker
    private var selectedImageUri: Uri? = null
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            Glide.with(binding.root)
                .load(it)
                .into(binding.ivAvatar)
            addOrUpdateArtistVM.params.value?.artist?.avatar = selectedImageUri.toString()
        }
    }

    private val addOrUpdateArtistVM by activityViewModels<AddOrUpdateArtistViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddOrUpdateArtistBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialogFragment = LoadingDialogFragment()

        observeAddOrUpdateArtistVM()
        setEvents()
    }

    private fun observeAddOrUpdateArtistVM() {
        // Observer Loading State
        addOrUpdateArtistVM.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading == null) return@observe

            try {
                if (isLoading)
                    loadingDialogFragment.show(requireActivity().supportFragmentManager, null)
                else
                    loadingDialogFragment.dismissNow()

            } catch (_: Exception) {
            }
        }

        // Observer Params State
        addOrUpdateArtistVM.params
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { params ->
                if (params != null) {
                    initArtistForm(params)
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    @SuppressLint("StringFormatMatches")
    private fun initArtistForm(params: AddOrUpdateArtistViewModel.AddOrUpdateArtistParams) {
        val artist = params.artist

        binding.apply {
            edtArtistName.setText(artist.name)
            tvAmountInterested.text = root.context.getString(
                R.string.txt_amount_of_interested,
                artist.amountInterested
            )

            if (artist.avatar.isNotEmpty()) {
                Glide.with(root.context)
                    .load(artist.avatar)
                    .error(R.drawable.ic_song_24)
                    .into(ivAvatar)
            }
        }
    }

    private fun setEvents() {
        //avatar artist
        binding.ivAvatar.setOnClickListener {
            pickImageLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        //artist name
        binding.edtArtistName.doOnTextChanged { text, _, _, _ ->
            addOrUpdateArtistVM.params.value?.artist?.name = text.toString().trim()
        }

        //save artist
        handleSaveArtist()
    }

    private fun handleSaveArtist() {
        binding.btnSave.setOnClickListener {
            val error = addOrUpdateArtistVM.validateArtist()
            if (error != null) {
                handleValidationError(error)
                return@setOnClickListener
            }
            if (addOrUpdateArtistVM.params.value == null) {
                return@setOnClickListener
            }

            val artistTmp = addOrUpdateArtistVM.params.value!!.artist

            if (!addOrUpdateArtistVM.params.value!!.isUpdate) {
                artistTmp.id = GenerateIdHelper.generateIdLong()
            }

            addOrUpdateArtistVM.saveArtist(
                artistTmp,
                addOrUpdateArtistVM.params.value!!.isUpdate
            ) { success, msg ->
                Toast.makeText(
                    requireContext(),
                    msg,
                    Toast.LENGTH_SHORT
                ).show()

                if (success) {
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun handleValidationError(error: AddOrUpdateArtistViewModel.ValidationError) {
        when (error) {
            is AddOrUpdateArtistViewModel.ValidationError.EmptyName -> {
                binding.edtArtistName.error = getString(R.string.error_name_artist_empty)
                binding.edtArtistName.requestFocus()
                SnackbarUtils.showBaseSnackbar(
                    binding.root,
                    getString(R.string.error_name_artist_empty),
                    Snackbar.LENGTH_SHORT
                )
            }

            is AddOrUpdateArtistViewModel.ValidationError.EmptyImage -> {
                SnackbarUtils.showBaseSnackbar(
                    binding.root,
                    getString(R.string.error_avatar_artist_empty),
                    Snackbar.LENGTH_SHORT
                )
            }
        }
    }
}