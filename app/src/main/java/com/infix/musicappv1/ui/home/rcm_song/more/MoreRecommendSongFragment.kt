package com.infix.musicappv1.ui.home.rcm_song.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.PermissionRepository
import com.infix.musicappv1.databinding.FragmentMoreRecommendSongBinding
import com.infix.musicappv1.enums.PlaylistEnum
import com.infix.musicappv1.ui.adapter.song.SongAdapter
import com.infix.musicappv1.ui.adapter.song.SongPagingDataAdapter
import com.infix.musicappv1.ui.base.BasePlayMusicFragment
import com.infix.musicappv1.ui.home.HomeViewModel
import com.infix.musicappv1.utils.MusicAppUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MoreRecommendSongFragment : BasePlayMusicFragment() {
    @Inject
    lateinit var permissionRepository: PermissionRepository
    private lateinit var binding: FragmentMoreRecommendSongBinding
    private lateinit var adapter: SongPagingDataAdapter
    private val moreRcmSongViewModel: MoreRecommendSongViewModel by activityViewModels()

    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoreRecommendSongBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbarMoreRcmSong.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        initRecycleView()
        observePagingData()
        observeSongLocal()
    }

    private fun initRecycleView() {
        adapter = SongPagingDataAdapter(
            object : SongAdapter.SongClickListener {
                override fun onSongClick(song: Song, pos: Int) {
                    val songs =
                        homeViewModel.songLocal.value ?: return
                    val indexToPlay = MusicAppUtils.getIndexOfSong(song, songs)
                    playSong(
                        indexToPlay,
                        Playlist(namePlaylist = PlaylistEnum.MORE_RCM_SONG.value),
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

        binding.includeSongList.rvSongList.layoutManager =
            SongPagingDataAdapter.WrapContentLinearLayoutManager(requireContext())

        binding.includeSongList.rvSongList.adapter = adapter
    }

    //Cause MoreRecommendSongFragment not in home, we must observe songLocal
    //to update songs of room when paging. Avoid value of songLocal not integrity
    private fun observeSongLocal() {
        homeViewModel.songLocal.observe(viewLifecycleOwner) {}
    }

    private fun observePagingData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                moreRcmSongViewModel.songs.collectLatest { adapter.submitData(it) }
            }
        }
    }
}