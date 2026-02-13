package com.infix.musicappv1.ui.library.recent_song

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.model.recent.SongRecent
import com.infix.musicappv1.databinding.FragmentRecentSongsBinding
import com.infix.musicappv1.enums.PlaylistEnum
import com.infix.musicappv1.ui.BasePlayMusicFragment
import com.infix.musicappv1.utils.InjectUtils

class RecentSongsFragment : BasePlayMusicFragment() {
    private lateinit var binding: FragmentRecentSongsBinding
    private lateinit var adapter: RecentSongAdapter
    private val songRecentViewModel: RecentSongsViewModel by activityViewModels {
        RecentSongsViewModel.Factory(InjectUtils.getSongRecentRepository(requireContext().applicationContext))
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
                        Playlist(namePlaylist = PlaylistEnum.RECENT.value),
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
}