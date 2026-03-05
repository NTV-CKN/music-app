package com.infix.musicappv1.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.infix.musicappv1.databinding.FragmentSearchSongBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchSongFragment : Fragment() {
    private lateinit var binding: FragmentSearchSongBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchSongBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }
}