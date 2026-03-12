package com.infix.musicappv1.ui.home

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.media3.session.MediaController
import androidx.navigation.fragment.findNavController
import com.infix.musicappv1.databinding.FragmentHomeBinding
import com.infix.musicappv1.media.MediaControllerService
import com.infix.musicappv1.ui.viewmodels.PlayingSongSharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private var isSongsReady = false
    private var isAlbumReady = false
    private var isMediaControllerReady = false
    private var mediaController: MediaController? = null

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val playingSongSharedViewModel: PlayingSongSharedViewModel by activityViewModels()

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            name: ComponentName?,
            service: IBinder?
        ) {
            if (service == null || mediaController != null) return
            //guarantee update new controller if mediacontroller is null
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    val binder =
                        service as? MediaControllerService.BinderImpl ?: return@repeatOnLifecycle
                    binder.controllerFlow.collectLatest { controller ->
                        if (controller == null) return@collectLatest
                        mediaController = controller
                        isMediaControllerReady = true
                        playingSongSharedViewModel.setIsDataReady(isAlbumReady && isSongsReady && isMediaControllerReady)
                    }
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mediaController = null
        }
    }

    private var isObserve = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEvent()
        if (!isObserve) {
            setupInitDataTmp()
            isObserve = true
        }
        if (savedInstanceState != null) {
            val scrollY = savedInstanceState.getInt(SCROLL_POS_Y, 0)
            binding.homeScrollView.post {
                binding.homeScrollView.scrollTo(0, scrollY)
            }
        }
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //cause when app config change but not in home, when app change again => Error uninitialize
        if (::binding.isInitialized) {
            val scrollY = binding.root.scrollY
            outState.putInt(SCROLL_POS_Y, scrollY)
        }
    }

    private fun setupEvent() {
        binding.btnSearchHome.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToNavigateSearchSong())
        }
    }

    private fun setupInitDataTmp() {
        //song data
        homeViewModel.songLocal.observe(viewLifecycleOwner) { songs ->
            songs ?: return@observe
            isSongsReady = true
            playingSongSharedViewModel.setIsDataReady(isAlbumReady && isSongsReady && isMediaControllerReady)
        }
        //album data
        homeViewModel.albumLocal.observe(viewLifecycleOwner) { albums ->
            albums ?: return@observe
            isAlbumReady = true
            playingSongSharedViewModel.setIsDataReady(isAlbumReady && isSongsReady && isMediaControllerReady)
        }
    }

    companion object {
        const val SCROLL_POS_Y = "com.infix.musicappv1.ui.home.HomeFragment.SCROLL_POS_Y"
    }
}