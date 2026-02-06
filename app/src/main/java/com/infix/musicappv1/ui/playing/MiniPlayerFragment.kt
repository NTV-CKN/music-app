package com.infix.musicappv1.ui.playing

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.infix.musicappv1.R
import com.infix.musicappv1.databinding.FragmentMiniPlayerBinding
import com.infix.musicappv1.ui.viewmodels.PlaybackViewModel

class MiniPlayerFragment : Fragment() {
    private lateinit var binding: FragmentMiniPlayerBinding
    private val miniPlayerViewModel: MiniPlayerViewModel by activityViewModels()
    private val playbackViewModel: PlaybackViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMiniPlayerBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObserve()
    }

    private fun setObserve() {
        //song
        miniPlayerViewModel.song.observe(viewLifecycleOwner) {
            it?.let { song ->
                binding.includeItemMiniPlayer.tvArtistMiniPlayer.text = song.artist
                binding.includeItemMiniPlayer.tvTitleMiniplayer.text = song.title
                Glide.with(binding.root)
                    .load(song.image)
                    .error(R.drawable.ic_song_24)
                    .into(binding.includeItemMiniPlayer.imgMiniPlayer)
            }
        }
        //mediaitem
        miniPlayerViewModel.mediaItem.observe(viewLifecycleOwner) {mediaItem ->
            if(mediaItem == null) return@observe
            playbackViewModel.mediaController.value?.let { controller ->
                controller.setMediaItem(mediaItem)
                //prepare MediaSource load buffering
                controller.prepare()
                //start play
                controller.play()
            }
        }

    }
}