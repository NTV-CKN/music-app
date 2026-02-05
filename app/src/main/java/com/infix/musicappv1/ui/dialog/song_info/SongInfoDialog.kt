package com.infix.musicappv1.ui.dialog.song_info

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.databinding.FragmentSongInfoDialogBinding
import com.infix.musicappv1.utils.FormatTimeUtils

class SongInfoDialog : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentSongInfoDialogBinding
    private val songInfoViewModel: SongInfoDialogViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSongInfoDialogBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserveSongInfoViewModel()
    }

    private fun setupObserveSongInfoViewModel() {
        songInfoViewModel.song.observe(viewLifecycleOwner, ::showSongInfo)
    }

    private fun showSongInfo(song: Song) {
        binding.tvSongInfoAlbum.text = getString(R.string.txt_album_args, song.album)
        binding.tvSongInfoTitle.text = getString(R.string.txt_song_title_args, song.title)
        binding.tvSongInfoArtist.text = getString(R.string.txt_artist_args, song.artist)
        binding.tvSongInfoCategory.text = getString(R.string.txt_category_args, "N/A")
        binding.tvSongInfoDuration.text = getString(
            R.string.txt_duration_args,
            FormatTimeUtils.formatSecondToMinute(song.duration)
        )
        binding.tvSongInfoNumberPlay.text =
            getString(R.string.txt_number_of_plays_args, "" + song.counter)
        binding.tvSongInfoPublishYear.text = getString(R.string.txt_publish_year_args, "N/A")
        binding.tvSongInfoIsFavorite.text =
            getString(R.string.txt_is_your_favorite_song_args, "" + song.favorite)

        Glide.with(binding.root)
            .load(song.image)
            .error(R.drawable.ic_song_24)
            .into(binding.imgSongInfo)
    }

    companion object {
        const val TAG = "SongInfoDialog"
    }
}