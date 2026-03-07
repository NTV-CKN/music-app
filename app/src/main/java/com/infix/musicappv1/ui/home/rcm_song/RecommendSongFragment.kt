package com.infix.musicappv1.ui.home.rcm_song

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.PermissionRepository
import com.infix.musicappv1.databinding.FragmentRecommendSongBinding
import com.infix.musicappv1.enums.PlaylistEnum
import com.infix.musicappv1.ui.adapter.song.SongAdapter
import com.infix.musicappv1.ui.adapter.song.SongPagingDataAdapter
import com.infix.musicappv1.ui.base.BasePlayMusicFragment
import com.infix.musicappv1.ui.home.HomeFragmentDirections
import com.infix.musicappv1.ui.home.HomeViewModel
import com.infix.musicappv1.utils.MusicAppUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RecommendSongFragment : BasePlayMusicFragment() {
    @Inject
    lateinit var permissionRepository: PermissionRepository
    private val rcmSongViewModel: RecommendSongViewModel by activityViewModels()

    private val homeViewModel: HomeViewModel by activityViewModels()

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

    private fun setupEvent() {
        //text view rcm song
        binding.tvTitleRcmSong.setOnClickListener { navigateToPlaylistDetail() }
        //btn image more song
        binding.btnMoreRcmSong.setOnClickListener { navigateToPlaylistDetail() }
    }

    private fun collectData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                rcmSongViewModel.songs.collectLatest { adapter.submitData(it) }
            }
        }
    }

    private fun initRecyclerView() {
        adapter = SongPagingDataAdapter(
            object : SongAdapter.SongClickListener {
                override fun onSongClick(song: Song, pos: Int) {
                    Log.d("SSSS", "" + homeViewModel.songLocal.value)
                    val songs =
                        homeViewModel.songLocal.value?.subList(0, RecommendSongViewModel.SIZE_SONG)
                            ?: return
                    val indexToPlay = MusicAppUtils.getIndexOfSong(song, songs)
                    playSong(
                        indexToPlay,
                        Playlist(namePlaylist = PlaylistEnum.RECOMMENDED.value),
                        songs
                    )
                }
            },
            object : SongAdapter.OptionSongClickListener {
                override fun onOptionClick(song: Song) {
                    showDialogSongOptionMenu(song)
                }
            }, permissionRepository
        )

        binding.includeRcmSong.rvSongList.adapter = adapter
    }

    private fun navigateToPlaylistDetail() {
        findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToNavigateMoreSongRecommend())
    }
}
