package com.infix.musicappv1.ui.home.album.more_album

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.infix.musicappv1.R

class MoreAlbumFragment : Fragment() {

    companion object {
        fun newInstance() = MoreAlbumFragment()
    }

    private val viewModel: MoreAlbumViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_more_album, container, false)
    }
}