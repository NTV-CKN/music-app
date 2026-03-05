package com.infix.musicappv1.ui.search.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.infix.musicappv1.databinding.FragmentSearchSongRecentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchSongRecentFragment : Fragment() {
    private lateinit var binding: FragmentSearchSongRecentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchSongRecentBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }
}