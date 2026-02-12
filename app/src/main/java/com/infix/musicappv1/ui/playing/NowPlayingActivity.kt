package com.infix.musicappv1.ui.playing

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Timeline
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
import com.infix.musicappv1.utils.MusicAppUtils
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
    private lateinit var animatorRotatingDisk: ObjectAnimator

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
        setupEvent()
        setupObserver()
        setupSeekbar()
    }

    override fun onPause() {
        super.onPause()
        if (isFinishing)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                overrideActivityTransition(
                    OVERRIDE_TRANSITION_CLOSE,
                    R.anim.fade_in,
                    R.anim.slide_down
                )
            } else
                @Suppress("DEPRECATION")
                overridePendingTransition(R.anim.fade_in, R.anim.slide_down)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaController?.removeListener(this)
        seekbarJob?.cancel()
    }

    override fun onBackPressed() {
        animatorRotatingDisk.pause()
        val resultIntent = Intent().putExtra(
            MusicAppUtils.KEY_FRACTION_EXTRA,
            animatorRotatingDisk.animatedFraction
        )
        setResult(RESULT_OK, resultIntent)
        super.onBackPressed()
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
            AnimatorInflater.loadAnimator(this, R.animator.rotating_disk) as ObjectAnimator

        //get fraction
        val fraction = intent.getFloatExtra(MusicAppUtils.KEY_FRACTION_EXTRA, 0.0f)
        animatorRotatingDisk.setCurrentFraction(fraction)
        animatorRotatingDisk.setTarget(binding.imgArtSongNowPlaying)
    }

    private fun setupObserver() {
        //setup media controller
        playbackViewModel.mediaController.observe(this) {
            it?.let { controller ->
                mediaController = controller
                addListenerMediaController()
                //setup repeat mode
                updateIconRepeatMode()
                //setup mode shuffle
                updateIconShuffle()
            }
        }

        //playing song
        nowPlayingViewModel.playingSongLivedata.observe(this) {
            it?.let {
                binding.seekbarNowPlaying.progress = 0
                showInfoSong(it.song)
                animatorRotatingDisk.start()
                //set max duration for seekbar
                setMaxDurationForSeekbar()
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

    private fun updateIconRepeatMode() {
        val repeatMode = mediaController?.repeatMode ?: return
        val ic = when (repeatMode) {
            Player.REPEAT_MODE_ALL -> R.drawable.ic_repeat_on
            Player.REPEAT_MODE_OFF -> R.drawable.ic_repeat_off
            Player.REPEAT_MODE_ONE -> R.drawable.ic_repeat_one
            else -> R.drawable.ic_repeat_off
        }
        binding.btnRepeatNowPlaying.setImageResource(ic)
    }

    private fun updateIconShuffle() {
        mediaController?.let { controller ->
            val isEnable = controller.shuffleModeEnabled
            val ic = if (isEnable)
                R.drawable.ic_shuffle_on
            else
                R.drawable.ic_shuffle_off

            binding.btnShuffleNowPlaying.setImageResource(ic)
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
        mediaController?.addListener(this)
    }

    private fun setMaxDurationForSeekbar() {
        val controller = playbackViewModel.mediaController.value ?: return
        val duration = controller.duration
        if (duration != C.TIME_UNSET) {
            val totalDuration = FormatTimeUtils.getMinuteAndSecond(duration)
            binding.seekbarNowPlaying.max = duration.toInt()
            binding.tvLabelTotalTime.text = totalDuration
        } else {
            Log.d("NowPlayingActivity", "MAX 0")
            binding.tvLabelTotalTime.text = FormatTimeUtils.getMinuteAndSecond(0)
            binding.seekbarNowPlaying.max = 0
        }
    }

    override fun onTimelineChanged(timeline: Timeline, reason: Int) {
        super.onTimelineChanged(timeline, reason)
        Log.d("NowPlayingActivity", "TimelineChanged")
        if (binding.seekbarNowPlaying.max == 0 && (mediaController?.duration ?: 0L) > 0L)
        //set max duration for seekbar
            setMaxDurationForSeekbar()
    }

    override fun onClick(v: View?) {
        animatorBtnPressed.setTarget(v)
        animatorBtnPressed.start()
        when (v?.id) {
            R.id.btn_pause_play_now_playing -> playPauseSong()
            R.id.btn_skip_next_now_playing -> skipNext()
            R.id.btn_skip_prev_now_playing -> skipPrev()
            R.id.btn_shuffle_now_playing -> shuffleSong()
            R.id.btn_repeat_now_playing -> repeatSongOrPlaylist()
            R.id.btn_add_favorite_now_playing -> addFavorite()
        }
    }

    private fun addFavorite() {
        val isFavorite = nowPlayingViewModel.isFavorite.value ?: return
        val songCurrent = nowPlayingViewModel.playingSongLivedata.value?.song ?: return
        songCurrent.favorite = !isFavorite
        nowPlayingViewModel.updateFavorite(songCurrent.id, !isFavorite)
    }

    private fun repeatSongOrPlaylist() {
        mediaController?.let { controller ->
            val nowMode = controller.repeatMode
            controller.repeatMode = when (nowMode) {
                Player.REPEAT_MODE_ALL -> Player.REPEAT_MODE_ONE
                Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ALL
                Player.REPEAT_MODE_ONE -> Player.REPEAT_MODE_OFF
                else -> Player.REPEAT_MODE_OFF
            }

            updateIconRepeatMode()
        }
    }

    private fun shuffleSong() {
        val isEnable = mediaController?.shuffleModeEnabled ?: return
        mediaController?.shuffleModeEnabled = !isEnable
        updateIconShuffle()
    }

    private fun skipPrev() {
        mediaController?.let { controller ->
            if (controller.hasPreviousMediaItem())
                controller.seekToPrevious()
        }
    }

    private fun skipNext() {
        mediaController?.let { controller ->
            if (controller.hasNextMediaItem())
                controller.seekToNext()
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

