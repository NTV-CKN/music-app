package com.infix.musicappv1.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.infix.musicappv1.data.source.local.db.MusicDatabase
import com.infix.musicappv1.databinding.FragmentHomeBinding
import com.infix.musicappv1.ui.home.rcm_song.RecommendSongViewModel
import com.infix.musicappv1.ui.home.system_playlist.AlbumViewModel
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
    private val albumViewModel: AlbumViewModel by activityViewModels {
        AlbumViewModel.Factory(
            InjectUtils.getAlbumRepository(requireContext().applicationContext),
            MusicDatabase.getInstance(requireContext().applicationContext)
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
            setupInitDataTmp()
            isObserve = true
        }
        if (savedInstanceState != null) {
            val scrollY = savedInstanceState.getInt(SCROLL_POS_Y, 0)
            binding.homeScrollView.post {
                binding.homeScrollView.scrollTo(0, scrollY)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val scrollY = binding.root.scrollY
        outState.putInt(SCROLL_POS_Y, scrollY)
    }

    private fun setupInitDataTmp() {
        homeViewModel.songLocal.observe(viewLifecycleOwner) { songs ->
            isSongsReady = true
            playingSongSharedViewModel.setIsDataReady(isAlbumReady && isSongsReady)
        }
        //album data
//        homeViewModel.playlists.observe(viewLifecycleOwner) { playlists ->
//            systemPlaylistViewModel.setPlaylists(playlists)
//            isAlbumReady = true
//            playingSongSharedViewModel.setIsDataReady(isAlbumReady && isSongsReady)
//        }
    }

    companion object {
        const val SCROLL_POS_Y = "com.infix.musicappv1.ui.home.HomeFragment.SCROLL_POS_Y"
    }
}