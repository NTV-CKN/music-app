package com.infix.musicappv1.ui.home.rcm_song

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.song.SongRepositoryImpl
import com.infix.musicappv1.data.source.remote.song.SongRemoteDataSource
import com.infix.musicappv1.databinding.FragmentRecommendSongBinding
import com.infix.musicappv1.enums.PlaylistEnum
import com.infix.musicappv1.ui.BasePlayMusicFragment
import com.infix.musicappv1.ui.detail.PlaylistDetailViewModel
import com.infix.musicappv1.ui.home.HomeViewModel
import com.infix.musicappv1.utils.InjectUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecommendSongFragment : BasePlayMusicFragment() {
    private var navigatePlaylistDetailJob: Job? = null
    private val rcmSongViewModel: RecommendSongViewModel by activityViewModels {
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
    private val playlistDetailViewModel: PlaylistDetailViewModel by activityViewModels {
        PlaylistDetailViewModel.Factory(
            InjectUtils.getPlaylistRepository(requireContext().applicationContext)
        )
    }

    private val homeViewModel: HomeViewModel by activityViewModels {
        HomeViewModel.Factory(
            InjectUtils.getSongRepository(requireContext().applicationContext),
            InjectUtils.getPlaylistRepository(requireContext().applicationContext)
        )
    }
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

    override fun onDestroyView() {
        super.onDestroyView()
        navigatePlaylistDetailJob?.cancel()
    }

    private fun setupEvent() {
        //text view rcm song
        binding.tvTitleRcmSong.setOnClickListener { navigateToPlaylistDetail() }
        //btn image more song
        binding.btnMoreRcmSong.setOnClickListener { navigateToPlaylistDetail() }
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
                        pos,
                        Playlist(namePlaylist = PlaylistEnum.RECOMMENDED.value),
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

    private fun navigateToPlaylistDetail() {
        navigatePlaylistDetailJob?.cancel()
        navigatePlaylistDetailJob = lifecycleScope.launch(Dispatchers.IO) {
            var playlist =
                playlistDetailViewModel.getPlaylistWithName(PlaylistEnum.MORE_RCM_SONG.value)
            playlist = playlist ?: Playlist(
                namePlaylist = PlaylistEnum.MORE_RCM_SONG.value,
                playlistId = PlaylistEnum.MORE_RCM_SONG.playlistId
            )
            withContext(Dispatchers.Main) {
                playlist.updateSongs(homeViewModel.songsLocal.value ?: emptyList())
                playlistDetailViewModel.setPlaylist(playlist)
                findNavController().navigate(R.id.action_navigation_home_to_detail_playlist)

            }
        }
    }
}