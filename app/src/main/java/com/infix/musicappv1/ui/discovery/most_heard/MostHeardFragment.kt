package com.infix.musicappv1.ui.discovery.most_heard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.databinding.FragmentMostHeardBinding
import com.infix.musicappv1.enums.PlaylistEnum
import com.infix.musicappv1.ui.BasePlayMusicFragment
import com.infix.musicappv1.ui.home.rcm_song.SongAdapter
import com.infix.musicappv1.utils.InjectUtils

class MostHeardFragment : BasePlayMusicFragment() {
    private lateinit var binding: FragmentMostHeardBinding
    private lateinit var adapter: SongAdapter
    private val mostHeardViewModel: MostHeardViewModel by viewModels {
        MostHeardViewModel.Factory(InjectUtils.getSongRepository(requireContext().applicationContext))
    }

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
            }
        )

        binding.includeListSongMostHeard.rvSongList.adapter = adapter
    }

    private fun setupObserve() {
        mostHeardViewModel.top15SongMostHeard.observe(viewLifecycleOwner) {
            adapter.updateSongs(it ?: emptyList())
            binding.progressSongMostHeard.visibility= View.GONE
        }
    }
}