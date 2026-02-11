package com.infix.musicappv1.ui.playing

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.repository.PlaybackRepository
import com.infix.musicappv1.data.source.local.db.MusicDatabase
import com.infix.musicappv1.databinding.FragmentMiniPlayerBinding
import com.infix.musicappv1.ui.viewmodels.Factory
import com.infix.musicappv1.ui.viewmodels.PlaybackViewModel
import com.infix.musicappv1.ui.viewmodels.PlayingSongSharedViewModel
import com.infix.musicappv1.utils.InjectUtils
import com.infix.musicappv1.utils.MusicAppUtils

class MiniPlayerFragment : Fragment(), View.OnClickListener {
    private var fractionDisk: Float = 0.0f
    private lateinit var binding: FragmentMiniPlayerBinding
    private lateinit var animatorBtnPressed: Animator
    private lateinit var animatorRotatingDisk: ObjectAnimator
    private val miniPlayerViewModel: MiniPlayerViewModel by activityViewModels {
        val db = MusicDatabase.getInstance(requireContext().applicationContext)
        Factory(InjectUtils.getPlaybackRepository(requireContext()))
    }
    private val playbackViewModel: PlaybackViewModel by activityViewModels()

    private val playingSongSharedViewModel: PlayingSongSharedViewModel by activityViewModels {
        Factory(InjectUtils.getPlaybackRepository(requireContext()))
    }

    private val nowPlayingLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { intent ->
                fractionDisk = intent.getFloatExtra(MusicAppUtils.KEY_FRACTION_EXTRA, fractionDisk)

                val mediaController = playbackViewModel.mediaController.value ?: return@let
                animatorRotatingDisk.start()
                animatorRotatingDisk.setCurrentFraction(fractionDisk)
                if (!mediaController.isPlaying){
                  animatorRotatingDisk.pause()
                }
            }
        }
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
        setupEvent()
//        animatorRotatingDisk.setc
    }

    private fun setupEvent() {
        binding.includeItemMiniPlayer.btnPausePlayMiniPlayer.setOnClickListener(this)
        binding.includeItemMiniPlayer.btnFavoriteMiniPlayer.setOnClickListener(this)
        binding.includeItemMiniPlayer.btnSkipNextMiniPlayer.setOnClickListener(this)
        //show now playing
        binding.root.setOnClickListener {
            animatorRotatingDisk.pause()
            nowPlayingLauncher.launch(
                Intent(
                    requireContext(),
                    NowPlayingActivity::class.java
                ).apply {
                    putExtra(
                        MusicAppUtils.KEY_FRACTION_EXTRA,
                        animatorRotatingDisk.animatedFraction
                    )
                })
        }
    }

    private fun setupAnimator() {
        animatorBtnPressed =
            AnimatorInflater.loadAnimator(requireContext(), R.animator.button_pressed)
        animatorRotatingDisk =
            AnimatorInflater.loadAnimator(
                requireContext(),
                R.animator.rotating_disk
            ) as ObjectAnimator

        animatorRotatingDisk.setTarget(binding.includeItemMiniPlayer.imgMiniPlayer)
    }

    private fun setObserve() {
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
                animatorRotatingDisk.end()
                showMiniPlayer(true)
            } ?: showMiniPlayer(false)
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
        playingSongSharedViewModel.indexToPlay.observe(viewLifecycleOwner) { indexToPlay ->
            indexToPlay?.indexToPlay?.let {
                val controller = playbackViewModel.mediaController.value ?: return@observe
                val indexMediaCur = playingSongSharedViewModel.getMediaItemIndexCurrent()
                // if old playlist same current playlist and index both same -> ignore
                if (it == indexMediaCur && trackOldPlaylist == playingSongSharedViewModel.getPlaylistTrackCurrent())
                    return@observe
                trackOldPlaylist = playingSongSharedViewModel.getPlaylistTrackCurrent()
                Log.d("SVU", "MEDIA ${indexMediaCur} IT ${it}")
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
                    if (animatorRotatingDisk.isStarted) {
                        animatorRotatingDisk.pause()
                    }
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

    private fun showMiniPlayer(bool: Boolean) {
        binding.root.visibility = if (bool) View.VISIBLE else View.GONE
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

    companion object {
        var trackOldPlaylist: Playlist? = Playlist(idPlaylist = -1)
    }
}
