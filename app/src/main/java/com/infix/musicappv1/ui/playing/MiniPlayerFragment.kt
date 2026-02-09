package com.infix.musicappv1.ui.playing

import android.animation.Animator
import android.animation.AnimatorInflater
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.infix.musicappv1.R
import com.infix.musicappv1.data.repository.PlaybackRepository
import com.infix.musicappv1.data.source.local.db.MusicDatabase
import com.infix.musicappv1.databinding.FragmentMiniPlayerBinding
import com.infix.musicappv1.ui.viewmodels.Factory
import com.infix.musicappv1.ui.viewmodels.PlaybackViewModel
import com.infix.musicappv1.ui.viewmodels.PlayingSongSharedViewModel

class MiniPlayerFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentMiniPlayerBinding
    private lateinit var animatorBtnPressed: Animator
    private lateinit var animatorRotatingDisk: Animator

    private val miniPlayerViewModel: MiniPlayerViewModel by activityViewModels {
        val db = MusicDatabase.getInstance(requireContext().applicationContext)
        Factory(
            PlaybackRepository.getInstance(
                db.songRecentDao(),
                db.songDao()
            )
        )
    }
    private val playbackViewModel: PlaybackViewModel by activityViewModels()
    private val playingSongSharedViewModel: PlayingSongSharedViewModel by activityViewModels {
        val db = MusicDatabase.getInstance(requireContext().applicationContext)
        Factory(
            PlaybackRepository.getInstance(
                db.songRecentDao(),
                db.songDao()
            )
        )
    }

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
        setupAnimator()
        //setup event
        binding.includeItemMiniPlayer.btnPausePlayMiniPlayer.setOnClickListener(this)
        binding.includeItemMiniPlayer.btnFavoriteMiniPlayer.setOnClickListener(this)
        binding.includeItemMiniPlayer.btnSkipNextMiniPlayer.setOnClickListener(this)
    }

    private fun setupAnimator() {
        animatorBtnPressed =
            AnimatorInflater.loadAnimator(requireContext(), R.animator.button_pressed)
        animatorRotatingDisk =
            AnimatorInflater.loadAnimator(requireContext(), R.animator.rotating_disk)

        animatorRotatingDisk.setTarget(binding.includeItemMiniPlayer.imgMiniPlayer)
    }

    private fun setObserve() {
        //mediacontroller
        playbackViewModel.mediaController.observe(viewLifecycleOwner) {
            //   it?.let { contractEventPlayer(it) }
        }

        //playing song
        playingSongSharedViewModel.playingSongLivedata.observe(viewLifecycleOwner) { playingSong ->
            val song = playingSong?.song
            Log.d("SVU", "PlayingSong with ${song}")
            song?.let {
                binding.includeItemMiniPlayer.tvArtistMiniPlayer.text = it.artist
                binding.includeItemMiniPlayer.tvTitleMiniplayer.text = it.title
                Glide.with(binding.root)
                    .load(it.image)
                    .error(R.drawable.ic_song_24)
                    .into(binding.includeItemMiniPlayer.imgMiniPlayer)
            }
        }

        //playlist current
        playingSongSharedViewModel.currentPlaylist.observe(viewLifecycleOwner) {
            Log.d("SVU", "PlayListCurrent")
            if (it == null) return@observe
            if (it != playingSongSharedViewModel.getPlaylistTrackCurrent())
                miniPlayerViewModel.setMediaItems(it.getMediaItems())
        }

        //media items
        miniPlayerViewModel.mediaItems.observe(viewLifecycleOwner) { mediaItems ->
            if (mediaItems == null) return@observe
            playbackViewModel.mediaController.value?.setMediaItems(mediaItems)
        }

        //current index to play
        playingSongSharedViewModel.indexToPlay.observe(viewLifecycleOwner) {
            it?.let {
                val controller = playbackViewModel.mediaController.value ?: return@observe
                val indexMediaCur = playingSongSharedViewModel.getMediaItemIndexCurrent()
                if (it == indexMediaCur) return@observe
//                Log.d("SVU", "MEDIA ${indexMediaCur} IT ${it}")
                if (it > -1 && it < controller.mediaItemCount) {
                    controller.seekTo(it, 0)
                    controller.prepare()
                    controller.play()
                }
            }
        }

        //is playing
        miniPlayerViewModel.isPlaying.observe(viewLifecycleOwner) {
            it?.let { isPlaying ->
                if (isPlaying) {
                    binding.includeItemMiniPlayer.btnPausePlayMiniPlayer.setImageResource(R.drawable.ic_pause_circle_48px)
                    if (animatorRotatingDisk.isPaused) animatorRotatingDisk.resume()
                    else if (!animatorRotatingDisk.isRunning) animatorRotatingDisk.start()
                } else {
                    binding.includeItemMiniPlayer.btnPausePlayMiniPlayer.setImageResource(R.drawable.ic_play_circle_48px)
                    animatorRotatingDisk.pause()
                }
            }
        }

        //is favorite
        miniPlayerViewModel.isFavorite.observe(viewLifecycleOwner) { isFavorite ->
            val icFavorite = if (isFavorite)
                R.drawable.ic_favorite_on
            else
                R.drawable.ic_favorite_off
            binding.includeItemMiniPlayer.btnFavoriteMiniPlayer.setImageResource(icFavorite)
        }
    }

//    private fun MiniPlayerFragment.contractEventPlayer(controller: MediaController) {
//        controller.addListener(object : Player.Listener {
//            override fun onIsPlayingChanged(isPlaying: Boolean) {
//                super.onIsPlayingChanged(isPlaying)
//                miniPlayerViewModel.setPlaying(isPlaying)
//            }
//        })
//    }

    private fun pausePlayMusic() {
        miniPlayerViewModel.isPlaying.value?.let { isPlaying ->
            val controller = playbackViewModel.mediaController.value ?: return@let
            if (isPlaying)
                controller.pause()
            else
                controller.play()
        }
    }

    private fun skipNextMusic() {
        val mediaController = playbackViewModel.mediaController.value ?: return
        if (mediaController.hasNextMediaItem()) {
            mediaController.seekToNextMediaItem()
            animatorRotatingDisk.end()
        }
    }

    private fun updateSongFavorite() {
        val songCurrent = playingSongSharedViewModel.playingSongLivedata.value?.song
        songCurrent?.let { song ->
            val isFavorite = !song.favorite
            song.favorite = isFavorite
            playingSongSharedViewModel.updateSongFavorite(song.id, isFavorite)
        }
    }

    override fun onClick(v: View?) {
        if (v == null) return
        animatorBtnPressed.setTarget(v)
        animatorBtnPressed.start()
        when (v.id) {
            R.id.btn_pause_play_mini_player -> pausePlayMusic()
            R.id.btn_favorite_mini_player -> updateSongFavorite()
            R.id.btn_skip_next_mini_player -> skipNextMusic()
            else -> {}
        }
    }
}
