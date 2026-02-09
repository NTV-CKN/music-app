package com.infix.musicappv1.ui.home.album.more_album

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.song.SongRepositoryImpl
import com.infix.musicappv1.data.source.remote.SongRemoteDataSource
import com.infix.musicappv1.databinding.FragmentMoreAlbumBinding
import com.infix.musicappv1.ui.home.HomeViewModel
import com.infix.musicappv1.ui.home.album.detail.DetailAlbumViewModel
import com.infix.musicappv1.utils.InjectUtils

class MoreAlbumFragment : Fragment() {
    private lateinit var binding: FragmentMoreAlbumBinding
    private lateinit var adapter: MoreAlbumAdapter

    private val moreAlbumViewModel: MoreAlbumViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(HomeViewModel::class.java))
                    return HomeViewModel(
                        SongRepositoryImpl(
                            SongRemoteDataSource(),
                            InjectUtils.getSongLocalDataSource(requireContext().applicationContext)
                        )
                    ) as T
                throw IllegalArgumentException("Model class is not legal")
            }
        }
    }
    private val albumDetailViewModel: DetailAlbumViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoreAlbumBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbarMoreAlbum.setupWithNavController(findNavController())
        setupRecyclerView()
        setupEvent()
    }

    private fun setupRecyclerView() {
        adapter = MoreAlbumAdapter(object : MoreAlbumAdapter.AlbumClickListener {
            override fun onClick(album: Album) {
                albumDetailViewModel.setAlbumAndSongs(album, extractSongsByAlbum(album))
                findNavController().navigate(R.id.action_navigation_more_album_to_navigation_detail_album)
            }
        })

        binding.rvMoreAlbum.adapter = adapter
    }

    private fun extractSongsByAlbum(album: Album): List<Song> {
        val songs = homeViewModel.songs.value
        val result = mutableListOf<Song>()
        songs?.let { songs ->
            for (songId in album.songs) {
                val index = songs.indexOfFirst { song -> song.id == songId }
                if (index != -1)
                    result.add(songs[index])
            }
        }

        return result
    }

    private fun setupEvent() {
        moreAlbumViewModel.albums.observe(viewLifecycleOwner) { albums ->
            adapter.updateAlbums(albums)
        }
    }
}