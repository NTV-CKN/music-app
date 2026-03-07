package com.infix.musicappv1.ui.search.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.model.search.RecentSearchSong
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.PermissionRepository
import com.infix.musicappv1.databinding.FragmentResultSearchSongBinding
import com.infix.musicappv1.enums.PlaylistEnum
import com.infix.musicappv1.ui.base.BasePlayMusicFragment
import com.infix.musicappv1.ui.adapter.song.SongAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ResultSearchSongFragment : BasePlayMusicFragment() {
    @Inject
    lateinit var permissionRepository: PermissionRepository
    private lateinit var binding: FragmentResultSearchSongBinding
    private lateinit var adapter: SongAdapter
    private val resultSearchSongViewModel: ResultSearchSongViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultSearchSongBinding.inflate(
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
    }

    private fun initRecyclerView() {
        adapter = SongAdapter(
            object : SongAdapter.SongClickListener {
                override fun onSongClick(song: Song, pos: Int) {
                    val songs = resultSearchSongViewModel.songs.value ?: return
                    playSong(
                        pos,
                        Playlist(namePlaylist = PlaylistEnum.RESULT_SEARCH_SONG.value),
                        songs
                    )
                    //insert RecentSearchSong
                    val recentSearchSong: RecentSearchSong = createRecentSearchSong(song)
                    resultSearchSongViewModel.saveSongWhenUserClick(recentSearchSong)
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
        resultSearchSongViewModel.key.observe(viewLifecycleOwner) {
            resultSearchSongViewModel.loadSongsByKey(it ?: "")
        }

        resultSearchSongViewModel.songs.observe(viewLifecycleOwner) {
            it?.let { songs ->
                adapter.updateSongs(songs)
            }
        }
    }

    private fun createRecentSearchSong(song: Song): RecentSearchSong {
        return RecentSearchSong().apply {
            id = song.id
            title = song.title
            album = song.album
            artist = song.artist
            source = song.source
            image = song.image
            duration = song.duration
            favorite = song.favorite
            counter = song.counter
            replay = song.replay
        }
    }
}