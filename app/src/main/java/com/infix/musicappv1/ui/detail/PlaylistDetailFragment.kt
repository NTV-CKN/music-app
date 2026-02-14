package com.infix.musicappv1.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.databinding.FragmentDetailPlaylistBinding
import com.infix.musicappv1.ui.BasePlayMusicFragment
import com.infix.musicappv1.ui.home.rcm_song.SongAdapter
import com.infix.musicappv1.utils.InjectUtils

class PlaylistDetailFragment : BasePlayMusicFragment() {
    private lateinit var binding: FragmentDetailPlaylistBinding
    private lateinit var songAdapter: SongAdapter

    private val playlistDetailViewModel: PlaylistDetailViewModel by activityViewModels {
        PlaylistDetailViewModel.Factory(
            InjectUtils.getPlaylistRepository(requireContext().applicationContext)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailPlaylistBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.includePlaylistDetail.toolbarPlaylistDetail.setupWithNavController(findNavController())
        initRecyclerView()
        setupObserve()
    }

    private fun setupObserve() {
        playlistDetailViewModel.playlist.observe(viewLifecycleOwner) {
            it?.let { playlist ->
                binding.includePlaylistDetail.tvTitlePlaylistDetail.text = playlist.namePlaylist
                binding.includePlaylistDetail.tvAmountSongPlaylistDetail.text =
                    getString(R.string.txt_amount_of_songs, "" + playlist.songsObject.size)
                Glide.with(binding.root)
                    .load(playlist.artwork)
                    .error(R.drawable.ic_song_24)
                    .into(binding.includePlaylistDetail.imgArtworkPlaylistDetail)
                songAdapter.updateSongs(it.songsObject)
            }
        }
    }

    private fun initRecyclerView() {
        songAdapter = SongAdapter(
            object : SongAdapter.SongClickListener {
                override fun onSongClick(song: Song, pos: Int) {
                    playlistDetailViewModel.playlist.value?.let {
                        playSong(
                            pos,
                            it,
                            playlistDetailViewModel.playlist.value!!.songsObject
                        )
                    }
                }
            },
            object : SongAdapter.OptionSongClickListener {
                override fun onOptionClick(song: Song) {
                    showDialogSongOptionMenu(song)
                }
            }
        )

        binding.includePlaylistDetail.includeSongList.rvSongList.adapter = songAdapter
    }
}