package com.infix.musicappv1.ui.library.favorite_song

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.song.SongRepositoryImpl
import com.infix.musicappv1.data.source.remote.song.SongRemoteDataSource
import com.infix.musicappv1.databinding.FragmentFavoriteSongsBinding
import com.infix.musicappv1.enums.PlaylistEnum
import com.infix.musicappv1.ui.BasePlayMusicFragment
import com.infix.musicappv1.ui.home.rcm_song.SongAdapter
import com.infix.musicappv1.utils.InjectUtils

class FavoriteSongsFragment : BasePlayMusicFragment() {
    private lateinit var binding: FragmentFavoriteSongsBinding
    private lateinit var adapter: SongAdapter
    private val favoriteSongsViewModel: FavoriteSongsViewModel by activityViewModels {
        FavoriteSongsViewModel.Factory(
            SongRepositoryImpl(
                SongRemoteDataSource(),
                InjectUtils.getSongLocalDataSource(requireContext().applicationContext)
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteSongsBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressFavoriteSong.visibility = View.VISIBLE
        initRecyclerView()
        setupObserve()
    }

    private fun initRecyclerView() {
        adapter = SongAdapter(
            object : SongAdapter.SongClickListener {
                override fun onSongClick(
                    song: Song,
                    pos: Int
                ) {
                    playSong(
                        pos,
                        Playlist(namePlaylist = PlaylistEnum.FAVORITES.value),
                        favoriteSongsViewModel.songsFavorite.value ?: emptyList()
                    )
                }

            },
            object : SongAdapter.OptionSongClickListener {
                override fun onOptionClick(song: Song) {
                    showDialogSongOptionMenu(song)
                }

            }
        )

        binding.includeSongList.rvSongList.adapter = adapter
    }

    private fun setupObserve() {
        favoriteSongsViewModel.songsFavorite.observe(viewLifecycleOwner) {
            adapter.updateSongs(
                it ?: emptyList()
            )
            binding.progressFavoriteSong.visibility = View.GONE
        }
    }
}