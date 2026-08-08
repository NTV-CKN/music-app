package com.infix.musicappv1.ui.admin.song.update_add

import android.R
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.infix.musicappv1.databinding.FragmentAddOrUpdateSongBinding
import com.infix.musicappv1.enums.Genre
import com.infix.musicappv1.utils.FormatSongPathUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class AddOrUpdateSongFragment : Fragment() {
    private lateinit var binding: FragmentAddOrUpdateSongBinding
    private val addOrUpdateSongViewModel by activityViewModels<AddOrUpdateSongViewModel>()

    //File picker
    private var selectedAudioUri: Uri? = null
    private val pickAudioLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedAudioUri = it
            binding.edtSource.setText(it.toString())
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
        observeAddOrUpdateVM()
        setupEvents()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        addOrUpdateSongViewModel.setIsUpdateSongState(null)
    }

    private fun observeAddOrUpdateVM() {
        addOrUpdateSongViewModel.params
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                if (it != null)
                    handleAddOrUpdateParams(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setupEvents() {
        initializeForm()

        //edt name song
        binding.edtTitle.doOnTextChanged { text, _, _, _ ->
            addOrUpdateSongViewModel.params.value?.song?.title = text.toString()
        }

        //edt path song

        //select artist
        //select album
        //edt path/url song
        setupAudioPicker()

        //energy
        setupEnergyDrag()

        //vip
        setupSwitchVipClick()
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
        if (addOrUpdateSongParams.isUpdate) {
            //inflate data
        }
    }

    private fun setupAudioPicker() {
        //show picker
        binding.tilSource.setEndIconOnClickListener {
            pickAudioLauncher.launch("audio/*")
        }

        //on text change
        binding.edtSource.doOnTextChanged { text, _, _, _ ->
            val str = text.toString()
            if(!str.isEmpty() && !FormatSongPathUtils.isValidUriOrUrl(str)) {
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
}