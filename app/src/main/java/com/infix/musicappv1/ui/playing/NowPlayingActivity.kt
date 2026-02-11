package com.infix.musicappv1.ui.playing

import android.animation.Animator
import android.animation.AnimatorInflater
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
import com.infix.musicappv1.utils.InjectUtils

class NowPlayingActivity : AppCompatActivity(), View.OnClickListener, Player.Listener {
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
            it?.let { showInfoSong(it.song) }
        }
        //is playing
        nowPlayingViewModel.isPlaying.observe(this) {isPlaying->
            var icPauseNext: Int
            if(isPlaying) {
                icPauseNext = R.drawable.ic_pause_circle_48px
                animatorRotatingDisk.start()
            } else {
                icPauseNext = R.drawable.ic_play_circle_48px
            }

            binding.btnPausePlayNowPlaying.setImageResource(icPauseNext)
        }
        //is favorite
        nowPlayingViewModel.isFavorite.observe(this) {
            val icFavorite =
            if(it) {
                 R.drawable.ic_favorite_on
            }else {
                R.drawable.ic_favorite_off
            }
            binding.btnAddFavoriteNowPlaying.setImageResource(icFavorite)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaController?.removeListener(this)
    }

    private fun setupMediaController() {
        playbackViewModel.mediaController.observe(this) {
            mediaController = it
            addListenerMediaController()
        }
    }

    private fun setupEvent() {
        binding.toolbarNowPlaying.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
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

    override fun onClick(v: View?) {

    }
}

