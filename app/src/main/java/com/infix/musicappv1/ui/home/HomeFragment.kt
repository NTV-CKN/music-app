package com.infix.musicappv1.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.infix.musicappv1.data.repository.song.SongRepositoryImpl
import com.infix.musicappv1.data.source.remote.song.SongRemoteDataSource
import com.infix.musicappv1.databinding.FragmentHomeBinding
import com.infix.musicappv1.ui.home.system_playlist.SystemPlaylistViewModel
import com.infix.musicappv1.ui.home.rcm_song.RecommendSongViewModel
import com.infix.musicappv1.ui.viewmodels.Factory
import com.infix.musicappv1.ui.viewmodels.PlayingSongSharedViewModel
import com.infix.musicappv1.utils.InjectUtils

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private var isSongsReady = false
    private var isAlbumReady = false
    private val homeViewModel: HomeViewModel by activityViewModels {
        HomeViewModel.Factory(
            InjectUtils.getSongRepository(requireContext().applicationContext),
            InjectUtils.getPlaylistRepository(requireContext().applicationContext)
        )
    }
    private val playingSongSharedViewModel: PlayingSongSharedViewModel by activityViewModels {
        Factory(InjectUtils.getPlaybackRepository(requireContext()))
    }

    private val systemPlaylistViewModel: SystemPlaylistViewModel by activityViewModels {
        SystemPlaylistViewModel.Factory(
            InjectUtils.getPlaylistRepository(requireContext().applicationContext)
        )
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
    ): View {
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
        //album data
        homeViewModel.playlists.observe(viewLifecycleOwner) { playlists ->
            systemPlaylistViewModel.setPlaylists(playlists)
            isAlbumReady = true
            playingSongSharedViewModel.setIsDataReady(isAlbumReady && isSongsReady)
        }
        //song local data
        homeViewModel.songsLocal.observe(viewLifecycleOwner) { songs ->
            songs?.let {
                songViewModel.setSongs(it)
                isSongsReady = true
                playingSongSharedViewModel.setIsDataReady(isAlbumReady && isSongsReady)
            }
        }
    }
}