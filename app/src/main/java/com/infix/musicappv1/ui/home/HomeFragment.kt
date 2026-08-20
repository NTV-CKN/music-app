package com.infix.musicappv1.ui.home

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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.media3.session.MediaController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.infix.musicappv1.data.model.ai_rcm.AiMoodUiState
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.PermissionRepository
import com.infix.musicappv1.databinding.FragmentHomeBinding
import com.infix.musicappv1.enums.PlaylistEnum
import com.infix.musicappv1.media.MediaControllerService
import com.infix.musicappv1.ui.adapter.home.SectionAiRecommendationAdapter
import com.infix.musicappv1.ui.adapter.home.SectionAlbumListAdapter
import com.infix.musicappv1.ui.adapter.home.SectionHeaderHomeAdapter
import com.infix.musicappv1.ui.adapter.home.SectionSongListAdapter
import com.infix.musicappv1.ui.adapter.song.SongAdapter
import com.infix.musicappv1.ui.adapter.song.SongPagingDataAdapter
import com.infix.musicappv1.ui.base.BasePlayMusicFragment
import com.infix.musicappv1.ui.viewmodels.PlayingSongSharedViewModel
import com.infix.musicappv1.utils.MusicAppUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BasePlayMusicFragment() {
    @Inject
    lateinit var permissionRepository: PermissionRepository

    private lateinit var binding: FragmentHomeBinding
    private lateinit var sectionHeaderHomeAdapter: SectionHeaderHomeAdapter
    private lateinit var sectionAlbumListAdapter: SectionAlbumListAdapter
    private lateinit var sectionSongListAdapter: SectionSongListAdapter
    private lateinit var sectionSongPagingAdapter: SongPagingDataAdapter
    private lateinit var sectionAiRecommend: SectionAiRecommendationAdapter

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

//    private var isObserve = false

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
        setupInitDataTmp()
        initRecyclerView()
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

    private fun initRecyclerView() {
        sectionHeaderHomeAdapter = SectionHeaderHomeAdapter {
            findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToNavigateSearchSong())
        }
        sectionSongListAdapter = SectionSongListAdapter {
            findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToNavigateMoreSongRecommend())
        }

        sectionAiRecommend = SectionAiRecommendationAdapter(
            onSuggestClick = ::handleClickAIRecommend,
            onSongClick = { song, pos -> },
            onOptionClick = { song -> },
            permissionRepository = permissionRepository
        )

        sectionAlbumListAdapter = SectionAlbumListAdapter()

        sectionSongPagingAdapter = SongPagingDataAdapter(
            object : SongAdapter.SongClickListener {
                override fun onSongClick(song: Song, pos: Int) {
                    Log.d("SSSS", "" + homeViewModel.songLocal.value)
                    val songs =
                        homeViewModel.songLocal.value?.subList(0, HomeViewModel.SIZE_SONG)
                            ?: return
                    val indexToPlay = MusicAppUtils.getIndexOfSong(song, songs)
                    playSong(
                        indexToPlay,
                        Playlist(namePlaylist = PlaylistEnum.RECOMMENDED.value),
                        songs
                    )
                }
            },
            { song -> showDialogSongOptionMenu(song) }, permissionRepository
        )
        sectionSongPagingAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        val concatAdapter =
            ConcatAdapter(
                sectionHeaderHomeAdapter,
                sectionAlbumListAdapter,
                sectionAiRecommend,
                sectionSongListAdapter,
                sectionSongPagingAdapter
            )

        collectData()

        binding.recyclerViewHome.adapter = concatAdapter
    }

    private fun collectData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                try {
                    homeViewModel.songs.collectLatest { sectionSongPagingAdapter.submitData(it) }
                } catch (ex: Exception) {
                    Log.d("RecommendSongFragment", ex.message ?: "Unknown")
                }
            }
        }
    }

    private fun handleClickAIRecommend(prompt: String) {
        sectionAiRecommend.updateState(
            AiMoodUiState.Loading
        )

        homeViewModel.loadRecommendSongsByAI(
            prompt,
            onSuccess = { aiRcm ->
                sectionAiRecommend.updateState(
                    AiMoodUiState.Success(aiRcm)
                )
            },
            onFailed = {
                sectionAiRecommend.updateState(
                    AiMoodUiState.Idle
                )
            },
        )
    }
}