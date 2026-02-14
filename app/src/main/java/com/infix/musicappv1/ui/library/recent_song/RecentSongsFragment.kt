package com.infix.musicappv1.ui.library.recent_song

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.model.recent.SongRecent
import com.infix.musicappv1.databinding.FragmentRecentSongsBinding
import com.infix.musicappv1.enums.PlaylistEnum
import com.infix.musicappv1.ui.BasePlayMusicFragment
import com.infix.musicappv1.ui.detail.PlaylistDetailViewModel
import com.infix.musicappv1.utils.InjectUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecentSongsFragment : BasePlayMusicFragment() {
    private var navigatePlaylistDetailJob: Job? = null
    private lateinit var binding: FragmentRecentSongsBinding
    private lateinit var adapter: RecentSongAdapter
    private val songRecentViewModel: RecentSongsViewModel by activityViewModels {
        RecentSongsViewModel.Factory(InjectUtils.getSongRecentRepository(requireContext().applicationContext))
    }
    private val playlistDetailViewModel: PlaylistDetailViewModel by activityViewModels {
        PlaylistDetailViewModel.Factory(
            InjectUtils.getPlaylistRepository(requireContext().applicationContext)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecentSongsBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressRecentSong.visibility = View.VISIBLE
        initRecyclerView()
        setupObserve()
        binding.tvLabelRecentSong.setOnClickListener { navigatePlaylistDetail() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        navigatePlaylistDetailJob?.cancel()
    }

    private fun initRecyclerView() {
        adapter = RecentSongAdapter(
            object : RecentSongAdapter.OnRecentSongClick {
                override fun onClick(
                    recentSong: SongRecent,
                    pos: Int
                ) {
                    playSong(
                        pos,
                        Playlist(
                            namePlaylist = PlaylistEnum.RECENT.value,
                            playlistId = PlaylistEnum.RECENT.playlistId
                        ),
                        songRecentViewModel.songRecents.value ?: emptyList()
                    )
                }

            },
            object : RecentSongAdapter.OnMenuOptionClick {
                override fun onClick(recentSong: SongRecent) {
                    showDialogSongOptionMenu(recentSong)
                }

            }
        )

        val recyclerView = binding.includeSongList.rvSongList
        val layoutManager = GridLayoutManager(requireContext(), 3)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    private fun setupObserve() {
        songRecentViewModel.songRecents.observe(viewLifecycleOwner) {
            adapter.updateSongRecent(it ?: emptyList())
            binding.progressRecentSong.visibility = View.GONE
        }
    }

    private fun navigatePlaylistDetail() {
        navigatePlaylistDetailJob?.cancel()
        navigatePlaylistDetailJob = lifecycleScope.launch(Dispatchers.IO) {
            var playlist =
                playlistDetailViewModel.getPlaylistWithName(PlaylistEnum.RECENT.value)
            playlist = playlist ?: Playlist(
                namePlaylist = PlaylistEnum.RECENT.value,
                playlistId = PlaylistEnum.RECENT.playlistId
            )
            withContext(Dispatchers.Main) {
                playlist.updateSongs(songRecentViewModel.songRecents.value ?: emptyList())
                playlistDetailViewModel.setPlaylist(playlist)
                findNavController().navigate(R.id.action_navigation_library_to_navigation_detail_playlist)

            }
        }
    }
}