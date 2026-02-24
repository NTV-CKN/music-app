package com.infix.musicappv1.ui.home.rcm_song

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.source.local.db.MusicDatabase
import com.infix.musicappv1.databinding.FragmentRecommendSongBinding
import com.infix.musicappv1.enums.PlaylistEnum
import com.infix.musicappv1.ui.BasePlayMusicFragment
import com.infix.musicappv1.ui.adapter.SongAdapter
import com.infix.musicappv1.ui.adapter.SongPagingDataAdapter
import com.infix.musicappv1.ui.detail.PlaylistDetailViewModel
import com.infix.musicappv1.ui.home.HomeViewModel
import com.infix.musicappv1.utils.InjectUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecommendSongFragment : BasePlayMusicFragment() {
    private var navigatePlaylistDetailJob: Job? = null
    private val rcmSongViewModel: RecommendSongViewModel by activityViewModels {
        RecommendSongViewModel.Factory(
            InjectUtils.getSongRepository(requireContext().applicationContext),
            MusicDatabase.getInstance(requireContext().applicationContext)
        )
    }
    private val playlistDetailViewModel: PlaylistDetailViewModel by activityViewModels {
        PlaylistDetailViewModel.Factory(
            InjectUtils.getPlaylistRepository(requireContext().applicationContext)
        )
    }

    private val homeViewModel: HomeViewModel by activityViewModels {
        HomeViewModel.Factory(
            InjectUtils.getSongRepository(requireContext().applicationContext),
            InjectUtils.getPlaylistRepository(requireContext().applicationContext),
            MusicDatabase.getInstance(requireContext().applicationContext)
        )
    }
//    private val miniPlayerViewModel: MiniPlayerViewModel by activityViewModels()

    private lateinit var binding: FragmentRecommendSongBinding
    private lateinit var adapter: SongPagingDataAdapter

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
        collectData()
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

    private fun collectData() {
        binding.progressRcmSong.visibility = View.VISIBLE
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                rcmSongViewModel.songs.collectLatest { adapter.submitData(it) }
            }
        }
    }

    private fun initRecyclerView() {
        adapter = SongPagingDataAdapter(
            object : SongAdapter.SongClickListener {
                override fun onSongClick(song: Song, pos: Int) {
                    playSong(
                        pos,
                        Playlist(namePlaylist = PlaylistEnum.RECOMMENDED.value),
                        adapter.snapshot().items
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
             //   playlist.updateSongs(homeViewModel.songsLocal.value ?: emptyList())
                playlistDetailViewModel.setPlaylist(playlist)
                findNavController().navigate(R.id.action_navigation_home_to_detail_playlist)

            }
        }
    }
}