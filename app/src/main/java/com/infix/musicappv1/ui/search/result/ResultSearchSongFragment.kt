package com.infix.musicappv1.ui.search.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.infix.musicappv1.databinding.FragmentResultSearchSongBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultSearchSongFragment : Fragment() {
    private lateinit var binding: FragmentResultSearchSongBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultSearchSongBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }
}