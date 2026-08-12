package com.infix.musicappv1.ui.admin.album.update_add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.infix.musicappv1.databinding.FragmentAddOrUpdateAlbumBinding

class AddOrUpdateAlbumFragment : Fragment() {
    private lateinit var binding: FragmentAddOrUpdateAlbumBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddOrUpdateAlbumBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }
}