package com.infix.musicappv1.ui.admin.song.update_add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.infix.musicappv1.databinding.FragmentAddOrUpdateSongBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class AddOrUpdateSongFragment : Fragment() {
    private lateinit var binding: FragmentAddOrUpdateSongBinding
    private val addOrUpdateSongViewModel by activityViewModels<AddOrUpdateSongViewModel>()

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

    private fun handleAddOrUpdateParams(
        addOrUpdateSongParams: AddOrUpdateSongViewModel.AddOrUpdateSongParams
    ) {
        if (addOrUpdateSongParams.isUpdate) {
            //inflate data
        }
    }
}