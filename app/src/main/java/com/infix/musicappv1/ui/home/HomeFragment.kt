package com.infix.musicappv1.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.infix.musicappv1.data.source.local.db.MusicDatabase
import com.infix.musicappv1.databinding.FragmentHomeBinding
import com.infix.musicappv1.ui.home.rcm_song.RecommendSongViewModel
import com.infix.musicappv1.ui.home.system_playlist.SystemPlaylistViewModel
import com.infix.musicappv1.ui.viewmodels.Factory
import com.infix.musicappv1.ui.viewmodels.PlayingSongSharedViewModel
import com.infix.musicappv1.utils.InjectUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private var isSongsReady = false
    private var isAlbumReady = false
    private val homeViewModel: HomeViewModel by activityViewModels {
        HomeViewModel.Factory(
            InjectUtils.getSongRepository(requireContext().applicationContext),
            InjectUtils.getPlaylistRepository(requireContext().applicationContext),
            MusicDatabase.getInstance(requireContext().applicationContext)
        )
    }
    private val playingSongSharedViewModel: PlayingSongSharedViewModel by activityViewModels {
        Factory(InjectUtils.getPlaybackRepository(requireContext()))
    }
    private val rcmSongViewModel: RecommendSongViewModel by activityViewModels {
        RecommendSongViewModel.Factory(
            InjectUtils.getSongRepository(requireContext().applicationContext),
            MusicDatabase.getInstance(requireContext().applicationContext)
        )
    }
    private val systemPlaylistViewModel: SystemPlaylistViewModel by activityViewModels {
        SystemPlaylistViewModel.Factory(
            InjectUtils.getPlaylistRepository(requireContext().applicationContext)
        )
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
            //setupInitDataTmp()
            isObserve = true
        }
    }

    private fun setupInitDataTmp() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                //rcm song
                rcmSongViewModel.songs.collectLatest {
                    isSongsReady = true
                    playingSongSharedViewModel.setIsDataReady(isAlbumReady && isSongsReady)
                }
                //playlist system

            }
        }
        //album data
//        homeViewModel.playlists.observe(viewLifecycleOwner) { playlists ->
//            systemPlaylistViewModel.setPlaylists(playlists)
//            isAlbumReady = true
//            playingSongSharedViewModel.setIsDataReady(isAlbumReady && isSongsReady)
//        }
    }
}