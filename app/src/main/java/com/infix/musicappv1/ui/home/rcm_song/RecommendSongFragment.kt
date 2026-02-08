package com.infix.musicappv1.ui.home.rcm_song

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.song.SongRepositoryImpl
import com.infix.musicappv1.data.source.local.song.SongLocalDataSource
import com.infix.musicappv1.data.source.remote.SongRemoteDataSource
import com.infix.musicappv1.databinding.FragmentRecommendSongBinding
import com.infix.musicappv1.enums.PlaylistEnum
import com.infix.musicappv1.ui.BasePlayMusicFragment
import com.infix.musicappv1.ui.home.HomeViewModel
import com.infix.musicappv1.ui.home.rcm_song.more_rcm.MoreRcmSongViewModel

class RecommendSongFragment : BasePlayMusicFragment() {
    private val rcmSongViewModel: RecommendSongViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(RecommendSongViewModel::class.java))
                    return RecommendSongViewModel(
                        SongRepositoryImpl(
                            SongRemoteDataSource(),
                            SongLocalDataSource()
                        )
                    ) as T
                throw IllegalArgumentException("Model class illegal")
            }
        }
    }
    private val moreRcmSongViewModel: MoreRcmSongViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()
//    private val miniPlayerViewModel: MiniPlayerViewModel by activityViewModels()

    private lateinit var binding: FragmentRecommendSongBinding
    private lateinit var adapter: SongAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecommendSongBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //init recycler view
        initRecyclerView()
        observeViewModel()
        setupEvent()
    }

    private fun setupEvent() {
        //text view rcm song
        binding.tvTitleRcmSong.setOnClickListener { navigateToMoreRcmSong() }
        //btn image more song
        binding.btnMoreRcmSong.setOnClickListener { navigateToMoreRcmSong() }
    }

    private fun observeViewModel() {
        binding.progressRcmSong.visibility = View.VISIBLE
        rcmSongViewModel.songs.observe(viewLifecycleOwner) { songs ->
            adapter.updateSongs(songs.subList(0, 10))
            binding.progressRcmSong.visibility = View.GONE
        }
    }

    private fun initRecyclerView() {
        adapter = SongAdapter(
            object : SongAdapter.SongClickListener {
                override fun onSongClick(song: Song, pos: Int) {
                    playSong(
                        song,
                        pos,
                        PlaylistEnum.RECOMMENDED.value,
                        rcmSongViewModel.songs.value?.subList(0, 10) ?: emptyList()
                    )
                }
            },
            object : SongAdapter.OptionSongClickListener {
                override fun onOptionClick(song: Song) {
                    showDialogSongOptionMenu(song)
                }
            }
        )

        binding.includeRcmSong.rvSongList.adapter = adapter
    }

    private fun navigateToMoreRcmSong() {
        moreRcmSongViewModel.setSongs(homeViewModel.songs.value ?: emptyList())
        findNavController().navigate(R.id.action_navigation_home_to_navigation_more_rcm_song)
    }
}