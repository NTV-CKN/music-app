package com.infix.musicappv1.ui.search.history.recent_song_search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.PermissionRepository
import com.infix.musicappv1.databinding.FragmentSearchSongRecentBinding
import com.infix.musicappv1.enums.PlaylistEnum
import com.infix.musicappv1.ui.adapter.song.SongAdapter
import com.infix.musicappv1.ui.base.BasePlayMusicFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchSongRecentFragment : BasePlayMusicFragment() {
    @Inject
    lateinit var permissionRepository: PermissionRepository
    private lateinit var binding: FragmentSearchSongRecentBinding
    private lateinit var adapter: SongAdapter
    private val searchSongRecentViewModel: SearchSongRecentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchSongRecentBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        setupObserve()
        binding.tvClearAll.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle(requireContext().getString(R.string.txt_confirm_clear_all_data))
                .setNegativeButton(requireContext().getString(R.string.txt_cancel)) { _, _ -> }
                .setPositiveButton(requireContext().getString(R.string.txt_agree)) { _, _ ->
                    searchSongRecentViewModel.clearAll()
                    adapter.updateSongs(emptyList())
                }
                .create().show()
        }
    }

    private fun initRecyclerView() {
        adapter = SongAdapter(
            object : SongAdapter.SongClickListener {
                override fun onSongClick(song: Song, pos: Int) {
                    val songs = searchSongRecentViewModel.songs.value ?: return
                    playSong(
                        pos,
                        Playlist(namePlaylist = PlaylistEnum.RECENT_SEARCH_SONG.value),
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

        binding.includeSongList.rvSongList.adapter = adapter
    }

    private fun setupObserve() {
        searchSongRecentViewModel.songs.observe(viewLifecycleOwner) {
            it?.let { songs -> adapter.updateSongs(songs) }
        }
    }
}