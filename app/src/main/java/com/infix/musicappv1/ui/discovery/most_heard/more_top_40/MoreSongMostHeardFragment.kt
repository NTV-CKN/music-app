package com.infix.musicappv1.ui.discovery.most_heard.more_top_40

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.PermissionRepository
import com.infix.musicappv1.databinding.FragmentMoreSongMostHeardBinding
import com.infix.musicappv1.enums.PlaylistEnum
import com.infix.musicappv1.ui.adapter.song.SongAdapter
import com.infix.musicappv1.ui.base.BasePlayMusicFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MoreSongMostHeardFragment : BasePlayMusicFragment() {
    @Inject
    lateinit var permissionRepository: PermissionRepository
    private lateinit var binding: FragmentMoreSongMostHeardBinding
    private lateinit var adapter: SongAdapter
    private val mostHeardViewModel: MoreSongMostHeardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoreSongMostHeardBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbarMoreSongMostHeard.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        initRecycleView()
        setupObserve()
    }
    private fun initRecycleView() {
        adapter = SongAdapter(
            object : SongAdapter.SongClickListener {
                override fun onSongClick(song: Song, pos: Int) {
                    val songs = mostHeardViewModel.top40SongMostHeard.value ?: return
                    playSong(
                        pos,
                        Playlist(
                            playlistId = PlaylistEnum.MORE_SONG_MOST_HEARD.playlistId.toLong(),
                            namePlaylist = PlaylistEnum.MORE_SONG_MOST_HEARD.name
                        ),
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

        binding.includeMoreSongMostHeard.rvSongList.adapter = adapter
    }

    private fun setupObserve() {
        mostHeardViewModel.top40SongMostHeard.observe(viewLifecycleOwner) {
            adapter.updateSongs(it ?: emptyList())
        }
    }
}