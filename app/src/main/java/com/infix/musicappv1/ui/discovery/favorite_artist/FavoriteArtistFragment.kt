package com.infix.musicappv1.ui.discovery.favorite_artist

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.infix.musicappv1.R

class FavoriteArtistFragment : Fragment() {

    companion object {
        fun newInstance() = FavoriteArtistFragment()
    }

    private val viewModel: FavoriteArtistViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_favorite_artist, container, false)
    }
}