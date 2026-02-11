package com.infix.musicappv1.ui.playing

import android.animation.Animator
import android.animation.AnimatorInflater
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.bumptech.glide.Glide
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.PlaybackRepository
import com.infix.musicappv1.data.source.local.db.MusicDatabase
import com.infix.musicappv1.databinding.ActivityNowPlayingBinding
import com.infix.musicappv1.ui.viewmodels.Factory
import com.infix.musicappv1.ui.viewmodels.PlaybackViewModel
import com.infix.musicappv1.utils.FormatTimeUtils
import com.infix.musicappv1.utils.InjectUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NowPlayingActivity : AppCompatActivity(), View.OnClickListener, Player.Listener {
    private var seekbarJob: Job? = null
    private lateinit var binding: ActivityNowPlayingBinding
    private var mediaController: MediaController? = null
    private val playbackViewModel: PlaybackViewModel by viewModels()
    private val nowPlayingViewModel: NowPlayingViewModel by viewModels {
        Factory(
            InjectUtils.getPlaybackRepository(this)
        )
    }
    private lateinit var animatorBtnPressed: Animator
    private lateinit var animatorRotatingDisk: Animator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNowPlayingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupAnimator()
        setupMediaController()
        setupEvent()
        setupObserver()
        setupSeekbar()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaController?.removeListener(this)
        seekbarJob?.cancel()
    }

    private fun setupSeekbar() {
        seekbarJob?.cancel()
        seekbarJob = lifecycleScope.launch(Dispatchers.Main) {
            while (isActive) {
                mediaController?.let { controller ->
                    val currentDuration = controller.currentPosition
                    val labelCurrentDuration = FormatTimeUtils.getMinuteAndSecond(currentDuration)
                    binding.seekbarNowPlaying.progress = currentDuration.toInt()
                    binding.tvLabelTimeCurrent.text = labelCurrentDuration
                }
                delay(1000)
            }
        }
    }

    private fun setupAnimator() {
        animatorBtnPressed =
            AnimatorInflater.loadAnimator(this, R.animator.button_pressed)
        animatorRotatingDisk =
            AnimatorInflater.loadAnimator(this, R.animator.rotating_disk)

        animatorRotatingDisk.setTarget(binding.imgArtSongNowPlaying)
    }

    private fun setupObserver() {
        //playing song
        nowPlayingViewModel.playingSongLivedata.observe(this) {
            it?.let {
                binding.seekbarNowPlaying.progress = 0
                setMaxDurationForSeekbar()
                showInfoSong(it.song)
                animatorRotatingDisk.start()
            }
        }
        //is playing
        nowPlayingViewModel.isPlaying.observe(this) { isPlaying ->
            var icPauseNext: Int
            if (isPlaying) {
                icPauseNext = R.drawable.ic_pause_circle_48px
                if (animatorRotatingDisk.isPaused) animatorRotatingDisk.resume()
            } else {
                icPauseNext = R.drawable.ic_play_circle_48px
                animatorRotatingDisk.pause()
            }

            binding.btnPausePlayNowPlaying.setImageResource(icPauseNext)
        }
        //is favorite
        nowPlayingViewModel.isFavorite.observe(this) {
            val icFavorite =
                if (it) {
                    R.drawable.ic_favorite_on
                } else {
                    R.drawable.ic_favorite_off
                }
            binding.btnAddFavoriteNowPlaying.setImageResource(icFavorite)
        }
    }

    private fun setupMediaController() {
        playbackViewModel.mediaController.observe(this) {
            mediaController = it
            addListenerMediaController()
        }
    }

    private fun setupEvent() {
        //back press toolbar
        binding.toolbarNowPlaying.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        //btn play
        binding.btnPausePlayNowPlaying.setOnClickListener(this)
        binding.btnSkipNextNowPlaying.setOnClickListener(this)
        binding.btnSkipPrevNowPlaying.setOnClickListener(this)
        binding.btnShuffleNowPlaying.setOnClickListener(this)
        binding.btnRepeatNowPlaying.setOnClickListener(this)
        binding.btnShareNowPlaying.setOnClickListener(this)
        binding.btnAddFavoriteNowPlaying.setOnClickListener(this)
        binding.btnMoreOptionNowPlaying.setOnClickListener(this)
        binding.seekbarNowPlaying.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                if (fromUser) {
                    mediaController?.let { controller ->
                        controller.seekTo(progress.toLong())
                        val fromDuration = FormatTimeUtils.getMinuteAndSecond(progress.toLong())
                        binding.tvLabelTimeCurrent.text = fromDuration
                    }
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })
    }

    private fun showInfoSong(song: Song?) {
        song?.let {
            Glide.with(binding.root)
                .load(song.image)
                .error(R.drawable.ic_song_24)
                .circleCrop()
                .into(binding.imgArtSongNowPlaying)
            binding.tvNameAlbumNowPlaying.text = nowPlayingViewModel.getNamePlaylist()
            binding.tvNameArtistNowPlaying.text = song.artist
            binding.tvTitleSongNowPlaying.text = song.title
        }
    }

    private fun addListenerMediaController() {

    }

    private fun setMaxDurationForSeekbar() {
        val controller = playbackViewModel.mediaController.value ?: return
        val duration = controller.duration
        if (duration != C.TIME_UNSET) {
            val totalDuration = FormatTimeUtils.getMinuteAndSecond(duration)
            binding.seekbarNowPlaying.max = duration.toInt()
            binding.tvLabelTotalTime.text = totalDuration
        }
    }

    override fun onClick(v: View?) {
        animatorBtnPressed.setTarget(v)
        animatorBtnPressed.start()
        when (v?.id) {
            R.id.btn_pause_play_now_playing -> playPauseSong()
        }
    }

    private fun playPauseSong() {
        val isPlaying = nowPlayingViewModel.isPlaying.value ?: return
        val controller = playbackViewModel.mediaController.value ?: return
        var icPauseNext: Int
        if (isPlaying) {
            icPauseNext = R.drawable.ic_play_circle_48px
            controller.pause()
        } else {
            icPauseNext = R.drawable.ic_pause_circle_48px
            controller.prepare()
            controller.play()
        }

        binding.btnPausePlayNowPlaying.setImageResource(icPauseNext)
    }
}

