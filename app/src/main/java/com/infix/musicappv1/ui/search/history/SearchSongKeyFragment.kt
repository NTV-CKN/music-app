package com.infix.musicappv1.ui.search.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.infix.musicappv1.databinding.FragmentSearchSongKeyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchSongKeyFragment : Fragment() {
    private lateinit var binding: FragmentSearchSongKeyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchSongKeyBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }
}