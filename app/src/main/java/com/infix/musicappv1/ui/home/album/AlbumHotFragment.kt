package com.infix.musicappv1.ui.home.album

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.repository.album.AlbumRepositoryImpl
import com.infix.musicappv1.data.source.local.AlbumLocalDataSource
import com.infix.musicappv1.data.source.remote.AlbumRemoteDataSource
import com.infix.musicappv1.databinding.FragmentAlbumnHotBinding

class AlbumHotFragment : Fragment() {
    private val viewModel: AlbumHotViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(AlbumHotViewModel::class.java))
                    return AlbumHotViewModel(
                        AlbumRepositoryImpl(
                            AlbumRemoteDataSource(),
                            AlbumLocalDataSource()
                        )
                    ) as T
                throw IllegalAccessException("model class illegal")
            }
        }
    }
    private lateinit var binding: FragmentAlbumnHotBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumnHotBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //fill data for recycler album
        val adapter = AlbumAdapter(object : AlbumAdapter.AlbumClickListener {
            override fun onAlbumClick(album: Album) {
                TODO("Not yet implemented")
            }
        })
        binding.rvAlbumHot.adapter = adapter
        viewModel.albums.observe(viewLifecycleOwner, { albums ->
            val sortAlbum = albums.sortedBy { it.size }.reversed().subList(0, 8)
            adapter.updateAlbums(sortAlbum)
        })
    }
}