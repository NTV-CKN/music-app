package com.infix.musicappv1.ui.library.favorite_song

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.song.SongRepositoryImpl
import com.infix.musicappv1.data.source.remote.song.SongRemoteDataSource
import com.infix.musicappv1.databinding.FragmentFavoriteSongsBinding
import com.infix.musicappv1.enums.PlaylistEnum
import com.infix.musicappv1.ui.BasePlayMusicFragment
import com.infix.musicappv1.ui.detail.PlaylistDetailViewModel
import com.infix.musicappv1.ui.home.rcm_song.SongAdapter
import com.infix.musicappv1.utils.InjectUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteSongsFragment : BasePlayMusicFragment() {
    private var navigatePlaylistDetailJob: Job? = null
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
    private val playlistDetailViewModel: PlaylistDetailViewModel by activityViewModels {
        PlaylistDetailViewModel.Factory(
            InjectUtils.getPlaylistRepository(requireContext().applicationContext)
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
        binding.tvLabelFavoriteSong.setOnClickListener {
            navigatePlaylistDetail()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        navigatePlaylistDetailJob?.cancel()
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
                        Playlist(
                            namePlaylist = PlaylistEnum.FAVORITES.value,
                            playlistId = PlaylistEnum.FAVORITES.playlistId
                        ),
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

    private fun navigatePlaylistDetail() {
        navigatePlaylistDetailJob?.cancel()
        navigatePlaylistDetailJob = lifecycleScope.launch(Dispatchers.IO) {
            var playlist =
                playlistDetailViewModel.getPlaylistWithName(PlaylistEnum.FAVORITES.value)
            playlist = playlist ?: Playlist(
                namePlaylist = PlaylistEnum.FAVORITES.value,
                playlistId = PlaylistEnum.FAVORITES.playlistId
            )
            withContext(Dispatchers.Main) {
                playlist.updateSongs(favoriteSongsViewModel.songsFavorite.value ?: emptyList())
                playlistDetailViewModel.setPlaylist(playlist)
                findNavController().navigate(R.id.action_navigation_library_to_navigation_detail_playlist)

            }
        }
    }
}