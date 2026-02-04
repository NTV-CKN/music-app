package com.infix.musicappv1.ui.home.album

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.album.AlbumRepositoryImpl
import com.infix.musicappv1.data.source.local.AlbumLocalDataSource
import com.infix.musicappv1.data.source.remote.AlbumRemoteDataSource
import com.infix.musicappv1.databinding.FragmentAlbumnHotBinding
import com.infix.musicappv1.ui.home.HomeViewModel
import com.infix.musicappv1.ui.home.album.detail.DetailAlbumViewModel

class AlbumHotFragment : Fragment() {
    private val albumViewModel: AlbumHotViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(AlbumHotViewModel::class.java))
                    return AlbumHotViewModel(
                        AlbumRepositoryImpl(
                            AlbumRemoteDataSource(),
                            AlbumLocalDataSource()
                        )
                    ) as T
                throw IllegalAccessException("model class illegal")
            }
        }
    }
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val albumDetailViewModel: DetailAlbumViewModel by activityViewModels()
    private lateinit var binding: FragmentAlbumnHotBinding
    private lateinit var adapter: AlbumAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumnHotBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observeViewModel()
    }

    private fun observeViewModel() {
        binding.progressAlbumHot.visibility = View.VISIBLE
        albumViewModel.albums.observe(viewLifecycleOwner, { albums ->
            val sortAlbum = albums.sortedBy { it.size }.reversed().subList(0, 8)
            adapter.updateAlbums(sortAlbum)
            binding.progressAlbumHot.visibility = View.GONE
        })
    }

    private fun initRecyclerView() {
        adapter = AlbumAdapter(object : AlbumAdapter.AlbumClickListener {
            override fun onAlbumClick(album: Album) {
                albumDetailViewModel.setAlbumAndSongs(album, extractSongsByAlbum(album))
                findNavController().navigate(R.id.action_navigation_home_to_detailAlbumFragment)
            }
        })
        binding.rvAlbumHot.adapter = adapter
    }

    private fun extractSongsByAlbum(album: Album): List<Song> {
        val songs = homeViewModel.songs.value
        val result = mutableListOf<Song>()
        songs?.let { songs ->
            for (songId in album.songs) {
                val index = songs.indexOfFirst { song -> song.id == songId.toInt() }
                if (index != -1)
                    result.add(songs[index])
            }
        }

        return result
    }
}