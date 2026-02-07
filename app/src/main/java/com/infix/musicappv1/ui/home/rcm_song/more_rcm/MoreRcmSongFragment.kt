package com.infix.musicappv1.ui.home.rcm_song.more_rcm

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.databinding.FragmentMoreRcmSongBinding
import com.infix.musicappv1.databinding.FragmentRecommendSongBinding
import com.infix.musicappv1.enums.PlaylistEnum
import com.infix.musicappv1.ui.BasePlayMusicFragment
import com.infix.musicappv1.ui.home.rcm_song.SongAdapter

class MoreRcmSongFragment : BasePlayMusicFragment() {
    private lateinit var binding: FragmentMoreRcmSongBinding
    private lateinit var adapter: SongAdapter

    private val moreRcmSongViewModel: MoreRcmSongViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoreRcmSongBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbarMoreRcmSong.setupWithNavController(findNavController())
        initRecyclerView()
        setupObserveRcmSongViewModel()
    }

    private fun initRecyclerView() {
        adapter = SongAdapter(
            object : SongAdapter.SongClickListener {
                override fun onSongClick(song: Song, pos: Int) {
                    playSong(
                        song,
                        pos,
                        PlaylistEnum.MORE_RCM_SONG.value,
                        moreRcmSongViewModel.songs.value ?: emptyList()
                    )
                }
            },
            object : SongAdapter.OptionSongClickListener {
                override fun onOptionClick(song: Song) {
                    showDialogSongOptionMenu(song)
                }
            }
        )

        binding.includeMoreRcmSong.rvSongList.adapter = adapter
    }

    private fun setupObserveRcmSongViewModel() {
        moreRcmSongViewModel.songs.observe(viewLifecycleOwner) { songs ->
            adapter.updateSongs(songs)
        }
    }
}