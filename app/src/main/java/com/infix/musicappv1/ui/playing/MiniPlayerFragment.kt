package com.infix.musicappv1.ui.playing

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.media3.session.MediaController
import com.bumptech.glide.Glide
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.repository.PermissionRepository
import com.infix.musicappv1.databinding.FragmentMiniPlayerBinding
import com.infix.musicappv1.media.MediaControllerService
import com.infix.musicappv1.ui.viewmodels.PlayingSongSharedViewModel
import com.infix.musicappv1.utils.MusicAppUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MiniPlayerFragment : Fragment(), View.OnClickListener {
    private var fractionDisk: Float = 0.0f
    private lateinit var binding: FragmentMiniPlayerBinding
    private lateinit var animatorBtnPressed: Animator
    private lateinit var animatorRotatingDisk: ObjectAnimator
    private var controller: MediaController? = null

    private val miniPlayerViewModel: MiniPlayerViewModel by activityViewModels()
    private val playingSongSharedViewModel: PlayingSongSharedViewModel by activityViewModels()

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            name: ComponentName?,
            service: IBinder?
        ) {
            if (service == null || controller != null) return
            //guarantee update new controller if mediacontroller is null
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    val binder =
                        service as? MediaControllerService.BinderImpl ?: return@repeatOnLifecycle
                    binder.controllerFlow.collectLatest { controllerTmp ->
                        controller = controllerTmp
                    }
                }
            }
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            controller = null
        }
    }

    private val nowPlayingLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { intent ->
                fractionDisk = intent.getFloatExtra(MusicAppUtils.KEY_FRACTION_EXTRA, fractionDisk)

                val mediaController = controller ?: return@let
                animatorRotatingDisk.start()
                animatorRotatingDisk.setCurrentFraction(fractionDisk)
                if (!mediaController.isPlaying) {
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

    override fun onStart() {
        super.onStart()
        requireActivity().bindService(
            Intent(requireContext(), MediaControllerService::class.java),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onStop() {
        super.onStop()
        requireActivity().unbindService(serviceConnection)
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
                },
                ActivityOptionsCompat.makeCustomAnimation(
                    requireContext(),
                    R.anim.slide_up,
                    R.anim.fade_out
                )
            )
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
            Log.d("MiniPlayerFragment", "PlayingSong with ${song}")
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
            Log.d("MiniPlayerFragment", "PlayListCurrent1 ")
            if (it == null) return@observe
            Log.d(
                "MiniPlayerFragment",
                "Playlist current $it \nplaylist track ${playingSongSharedViewModel.getPlaylistTrackCurrent()}"
            )

            if (it != playingSongSharedViewModel.getPlaylistTrackCurrent()) {
                Log.d("MiniPlayerFragment", "THOA1")
                controller?.setMediaItems(it.getMediaItems())
                Log.d("MiniPlayerFragment", "controller: $controller")
            }
        }

        //current index to play
        playingSongSharedViewModel.indexToPlay.observe(viewLifecycleOwner) { indexToPlay ->
            indexToPlay?.indexToPlay?.let {
                controller ?: return@observe
                val indexMediaCur = playingSongSharedViewModel.getMediaItemIndexCurrent()
                // if old playlist same current playlist and index both same -> ignore

                if (it == indexMediaCur && trackOldPlaylist == playingSongSharedViewModel.getPlaylistTrackCurrent())
                    return@observe
                trackOldPlaylist = playingSongSharedViewModel.getPlaylistTrackCurrent()
                Log.d("MiniPlayerFragment", "MEDIA ${indexMediaCur} IT ${it}")
                if (it > -1 && it < controller!!.mediaItemCount && PermissionRepository.getInstance().isGrantedNotification.value ?: false) {
                    controller!!.seekTo(it, 0)
                    controller!!.prepare()
                    controller!!.play()
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
            controller ?: return@let
            if (PermissionRepository.getInstance().isGrantedNotification.value ?: false)
                if (isPlaying)
                    controller!!.pause()
                else
                    controller!!.play()
        }
    }

    private fun skipNextMusic() {
        val mediaController = controller ?: return
        if (PermissionRepository.getInstance().isGrantedNotification.value ?: false)
            if (mediaController.hasNextMediaItem()) {
                mediaController.seekToNextMediaItem()
                animatorRotatingDisk.end()
            }
    }

    private fun updateSongFavorite() {
        val songCurrent = playingSongSharedViewModel.playingSongLivedata.value?.song
        songCurrent?.let { song ->
            if (PermissionRepository.getInstance().isGrantedNotification.value ?: false) {
                val isFavorite = !song.favorite
                song.favorite = isFavorite
                playingSongSharedViewModel.updateSongFavorite(song.id, isFavorite)
            }
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
        var trackOldPlaylist: Playlist? = Playlist(playlistId = -1)
    }
}
