package com.infix.musicappv1.ui.discovery.artist.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.databinding.FragmentArtistDetailBinding
import com.infix.musicappv1.ui.BasePlayMusicFragment
import com.infix.musicappv1.ui.home.rcm_song.SongAdapter
import com.infix.musicappv1.utils.InjectUtils

class ArtistDetailFragment : BasePlayMusicFragment() {
    private lateinit var binding: FragmentArtistDetailBinding
    private lateinit var adapter: SongAdapter
    private val artistDetailViewModel: ArtistDetailViewModel by activityViewModels {
        ArtistDetailViewModel.Factory(InjectUtils.getArtistRepository(requireContext().applicationContext))
    }

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
        artistDetailViewModel.artistWithSongs.observe(viewLifecycleOwner) {
            it?.let { artistWithSongs ->
                val artist = artistWithSongs.artist
                binding.tvNameArtistDetail.text = getString(R.string.txt_name_artist, artist.name)
                binding.tvAmountInterestedArtistDetail.text =
                    getString(R.string.txt_amount_of_interested, artist.interested)
                binding.tvIsInterestedArtistDetail.text =
                    getString(R.string.txt_your_interested_artist, artist.isInterested)

                Glide.with(binding.root)
                    .load(artist.avatar)
                    .error(R.drawable.ic_artist_24px)
                    .circleCrop()
                    .into(binding.imgArtistDetail)
                adapter.updateSongs(artistWithSongs.songs)
            }

        }
    }

    private fun initRecycleView() {
        adapter = SongAdapter(
            object : SongAdapter.SongClickListener {
                override fun onSongClick(song: Song, pos: Int) {
                    val artistWithSongs = artistDetailViewModel.artistWithSongs.value ?: return
                    playSong(
                        pos,
                        Playlist(
                            playlistId = artistWithSongs.artist.id,
                            namePlaylist = artistWithSongs.artist.name,
                            artwork = artistWithSongs.artist.avatar
                        ),
                        artistWithSongs.songs
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