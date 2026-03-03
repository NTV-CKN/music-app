package com.infix.musicappv1.ui.discovery.most_heard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.PermissionRepository
import com.infix.musicappv1.databinding.FragmentMostHeardBinding
import com.infix.musicappv1.enums.PlaylistEnum
import com.infix.musicappv1.ui.BasePlayMusicFragment
import com.infix.musicappv1.ui.adapter.song.SongAdapter
import com.infix.musicappv1.ui.discovery.DiscoveryFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MostHeardFragment : BasePlayMusicFragment() {
    @Inject
    lateinit var permissionRepository: PermissionRepository
    private lateinit var binding: FragmentMostHeardBinding
    private lateinit var adapter: SongAdapter
    private val mostHeardViewModel: MostHeardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMostHeardBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressSongMostHeard.visibility = View.VISIBLE
        binding.tvLabelMostHeard.setOnClickListener {
            findNavController().navigate(DiscoveryFragmentDirections.actionNavigationDiscoveryToNavigateMoreSongMostHeard())
        }
        initRecycleView()
        setupObserve()
    }

    private fun initRecycleView() {
        adapter = SongAdapter(
            object : SongAdapter.SongClickListener {
                override fun onSongClick(song: Song, pos: Int) {
                    val songs = mostHeardViewModel.top15SongMostHeard.value ?: return
                    playSong(
                        pos,
                        Playlist(
                            playlistId = PlaylistEnum.MOST_HEARD.playlistId,
                            namePlaylist = PlaylistEnum.MOST_HEARD.name
                        ),
                        songs
                    )
                }
            },
            object : SongAdapter.OptionSongClickListener {
                override fun onOptionClick(song: Song) {
                    showDialogSongOptionMenu(song)
                }
            }, permissionRepository
        )

        binding.includeListSongMostHeard.rvSongList.adapter = adapter
    }

    private fun setupObserve() {
        mostHeardViewModel.top15SongMostHeard.observe(viewLifecycleOwner) {
            adapter.updateSongs(it ?: emptyList())
            binding.progressSongMostHeard.visibility = View.GONE
        }
    }
}