package com.infix.musicappv1.ui.home.album.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.PermissionRepository
import com.infix.musicappv1.databinding.FragmentAlbumDetailBinding
import com.infix.musicappv1.ui.BasePlayMusicFragment
import com.infix.musicappv1.ui.adapter.song.SongAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
//cause song paging so ROOM cannot provide full song for one of albums, we need load songs of album specified with API
class AlbumDetailFragment : BasePlayMusicFragment() {
    @Inject
    lateinit var permissionRepository: PermissionRepository
    private lateinit var binding: FragmentAlbumDetailBinding
    private lateinit var adapter: SongAdapter
    private val albumDetailViewModel: AlbumDetailViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumDetailBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.includeDetaiAlbum.toolbarPlaylistDetail.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        initRecycleView()
        setupObserve()
    }

    private fun initRecycleView() {
        adapter = SongAdapter(
            object : SongAdapter.SongClickListener {
                override fun onSongClick(
                    song: Song,
                    pos: Int
                ) {
                    try {
                        val album = albumDetailViewModel.album.value ?: return
                        val songs = albumDetailViewModel.getSongsTrack() ?: return
                        playSong(
                            pos,
                            Playlist(
                                namePlaylist = album.name,
                                playlistId = album.id.toInt()
                            ),
                            songs
                        )
                    } catch (_: Exception) {
                    }
                }
            },
            object : SongAdapter.OptionSongClickListener {
                override fun onOptionClick(song: Song) {
                    showDialogSongOptionMenu(song)
                }
            }, permissionRepository
        )

        binding.includeDetaiAlbum.includeSongList.rvSongList.adapter = adapter
    }

    private fun setupObserve() {
        //album
        albumDetailViewModel.album.observe(viewLifecycleOwner) {
            it?.let { album ->
                albumDetailViewModel.loadSongs(album.id)

                binding.includeDetaiAlbum.tvTitlePlaylistDetail.text = album.name
                binding.includeDetaiAlbum.tvAmountSongPlaylistDetail.text =
                    getString(R.string.txt_amount_of_songs, "" + album.size)
                Glide.with(binding.root)
                    .load(album.artwork)
                    .error(R.drawable.ic_song_24)
                    .into(binding.includeDetaiAlbum.imgArtworkPlaylistDetail)
            }
        }
        //songs
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                albumDetailViewModel.songs.collectLatest {
                    adapter.updateSongs(it)
                }
            }
        }
    }
}