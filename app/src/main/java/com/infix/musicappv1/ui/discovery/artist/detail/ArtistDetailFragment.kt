package com.infix.musicappv1.ui.discovery.artist.detail

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
import com.infix.musicappv1.databinding.FragmentArtistDetailBinding
import com.infix.musicappv1.ui.BasePlayMusicFragment
import com.infix.musicappv1.ui.adapter.song.SongAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArtistDetailFragment : BasePlayMusicFragment() {
    private var songs: List<Song>? = null
    private lateinit var binding: FragmentArtistDetailBinding
    private lateinit var adapter: SongAdapter
    private val artistDetailViewModel: ArtistDetailViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArtistDetailBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbarArtistDetail.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        initRecycleView()
        setupObserve()
    }

    private fun setupObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                artistDetailViewModel.songs.collectLatest {
                    it?.let { songs ->
                        this@ArtistDetailFragment.songs = songs
                        val artist = artistDetailViewModel.getArtist() ?: return@let
                        binding.tvNameArtistDetail.text =
                            getString(R.string.txt_name_artist, artist.name)
                        binding.tvAmountInterestedArtistDetail.text =
                            getString(R.string.txt_amount_of_interested, artist.interested)
                        binding.tvIsInterestedArtistDetail.text =
                            getString(R.string.txt_your_interested_artist, "" + artist.isInterested)

                        Glide.with(binding.root)
                            .load(artist.avatar)
                            .error(R.drawable.ic_artist_24px)
                            .circleCrop()
                            .into(binding.imgArtistDetail)
                        adapter.updateSongs(songs)
                    }

                }
            }
        }
    }

    private fun initRecycleView() {
        adapter = SongAdapter(
            object : SongAdapter.SongClickListener {
                override fun onSongClick(song: Song, pos: Int) {
                    val artist = artistDetailViewModel.getArtist() ?: return
                    val songs = songs ?: return
                    playSong(
                        pos,
                        Playlist(
                            playlistId = artist.id,
                            namePlaylist = artist.name,
                            artwork = artist.avatar
                        ),
                        songs
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
}