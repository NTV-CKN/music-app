package com.infix.musicappv1.ui.playing

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.transition.ViewPropertyTransition
import com.infix.musicappv1.R
import com.infix.musicappv1.databinding.FragmentMiniPlayerBinding
import com.infix.musicappv1.ui.viewmodels.PlaybackViewModel

class MiniPlayerFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentMiniPlayerBinding
    private lateinit var objectAnimator: Animator
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
        objectAnimator = AnimatorInflater.loadAnimator(requireContext(), R.animator.button_pressed)
        binding.includeItemMiniPlayer.btnPausePlayMiniPlayer.setOnClickListener(this)
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
        miniPlayerViewModel.mediaItem.observe(viewLifecycleOwner) { mediaItem ->
            if (mediaItem == null) return@observe
            playbackViewModel.mediaController.value?.let { controller ->
                controller.setMediaItem(mediaItem)
                //prepare MediaSource load buffering
                controller.prepare()
                //start play
                controller.play()

                //contract listener MediaPlayer
                contractEventPlayer(controller)
            }
        }
        //is playing
        miniPlayerViewModel.isPlaying.observe(viewLifecycleOwner) {
            it?.let { isPlaying ->
                if (isPlaying)
                    binding.includeItemMiniPlayer.btnPausePlayMiniPlayer.setImageResource(R.drawable.ic_pause_circle_48px)
                else
                    binding.includeItemMiniPlayer.btnPausePlayMiniPlayer.setImageResource(R.drawable.ic_play_circle_48px)
            }
        }
    }

    private fun MiniPlayerFragment.contractEventPlayer(controller: MediaController) {
        controller.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                miniPlayerViewModel.setPlaying(isPlaying)
            }
        })
    }

    private fun pausePlayMusic() {
        miniPlayerViewModel.isPlaying.value?.let { isPlaying ->
            val controller = playbackViewModel.mediaController.value ?: return@let
            if (isPlaying)
                controller.pause()
            else
                controller.play()
        }
    }

    override fun onClick(v: View?) {
        if (v == null) return
        objectAnimator.setTarget(v)
        objectAnimator.start()
        when (v.id) {
            R.id.btn_pause_play_mini_player -> pausePlayMusic()
            R.id.btn_favorite_mini_player -> {}
            R.id.btn_skip_next_mini_player -> {}
            else -> {}
        }
    }
}
