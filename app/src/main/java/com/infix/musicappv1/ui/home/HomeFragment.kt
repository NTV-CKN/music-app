package com.infix.musicappv1.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.infix.musicappv1.R
import com.infix.musicappv1.data.repository.album.AlbumRepositoryImpl
import com.infix.musicappv1.data.repository.song.SongRepositoryImpl
import com.infix.musicappv1.data.source.local.album.AlbumLocalDataSource
import com.infix.musicappv1.data.source.local.song.SongLocalDataSource
import com.infix.musicappv1.data.source.remote.AlbumRemoteDataSource
import com.infix.musicappv1.data.source.remote.SongRemoteDataSource
import com.infix.musicappv1.databinding.FragmentHomeBinding
import com.infix.musicappv1.ui.home.album.AlbumHotViewModel
import com.infix.musicappv1.ui.home.rcm_song.RecommendSongViewModel
import com.infix.musicappv1.utils.InjectUtils

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(HomeViewModel::class.java))
                    return HomeViewModel(
                        SongRepositoryImpl(
                            SongRemoteDataSource(),
                            InjectUtils.getSongLocalDataSource(requireContext().applicationContext)
                        )
                    ) as T
                throw IllegalArgumentException("Model class is not legal")
            }
        }
    }
    private val albumViewModel: AlbumHotViewModel by activityViewModels {
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
    private val songViewModel: RecommendSongViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(RecommendSongViewModel::class.java))
                    return RecommendSongViewModel(
                        SongRepositoryImpl(
                            SongRemoteDataSource(),
                            InjectUtils.getSongLocalDataSource(requireContext().applicationContext)
                        )
                    ) as T
                throw IllegalArgumentException("Model class illegal")
            }
        }
    }

    private var isObserve = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isObserve) {
            setupInitDataTmp()
            isObserve = true
        }
    }

    private fun setupInitDataTmp() {
        homeViewModel.albums.observe(viewLifecycleOwner) { albums -> albumViewModel.setAlbums(albums) }
        homeViewModel.songs.observe(viewLifecycleOwner) { songs -> songViewModel.setSongs(songs) }
    }
}