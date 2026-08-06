package com.infix.musicappv1.ui.admin.song

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.infix.musicappv1.databinding.FragmentSongManagementBinding

class SongManagementFragment : Fragment() {
    private lateinit var binding: FragmentSongManagementBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSongManagementBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }
}