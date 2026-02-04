package com.infix.musicappv1.ui.home.album.detail

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.databinding.FragmentAlbumnHotBinding
import com.infix.musicappv1.databinding.FragmentDetailAlbumBinding
import com.infix.musicappv1.ui.home.rcm_song.SongAdapter

class DetailAlbumFragment : Fragment() {
    private lateinit var binding: FragmentDetailAlbumBinding
    private val detailAlbumViewModel: DetailAlbumViewModel by activityViewModels()
    private lateinit var adapter: SongAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailAlbumBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.includeAlbumDetail.toolbarAlbumDetail.setupWithNavController(findNavController())
        setupRecyclerView()
        setupObserveDetailAlbumViewModel()
    }

    private fun setupRecyclerView() {
        adapter = SongAdapter(
            object : SongAdapter.SongClickListener {
                override fun onSongClick(song: Song) {

                }
            },
            object : SongAdapter.OptionSongClickListener {
                override fun onOptionClick(song: Song) {

                }
            }
        )

        binding.includeAlbumDetail.includeSongList.rvSongList.adapter = adapter
    }

    private fun setupObserveDetailAlbumViewModel() {
        //album
        detailAlbumViewModel.album.observe(viewLifecycleOwner) { album ->
            binding.includeAlbumDetail.tvAmountSongAlbumDetail.text =
                getString(R.string.txt_amount_of_songs, album.songs.size)
            binding.includeAlbumDetail.tvTitleAlbumDetail.text = album.name
            Glide.with(binding.root)
                .load(album.artwork)
                .error(R.drawable.ic_song_24)
                .into(binding.includeAlbumDetail.imgArtworkAlbumDetail)
        }
        //songs
        detailAlbumViewModel.songs.observe(viewLifecycleOwner) { songs ->
            adapter.updateSongs(songs)
        }
    }
}