package com.infix.musicappv1.ui.search.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.infix.musicappv1.databinding.FragmentSearchSongHistoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchSongHistoryFragment : Fragment() {
    private lateinit var binding: FragmentSearchSongHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchSongHistoryBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }
}