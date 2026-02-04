package com.infix.musicappv1.ui.home.rcm_song

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.song.SongRepositoryImpl
import com.infix.musicappv1.data.source.local.SongLocalDataSource
import com.infix.musicappv1.data.source.remote.SongRemoteDataSource
import com.infix.musicappv1.databinding.FragmentRecommendSongBinding

class RecommendSongFragment : Fragment() {
    private val viewModel: RecommendSongViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(RecommendSongViewModel::class.java))
                    return RecommendSongViewModel(
                        SongRepositoryImpl(
                            SongRemoteDataSource(),
                            SongLocalDataSource()
                        )
                    ) as T
                throw IllegalArgumentException("Model class illegal")
            }
        }
    }
    private lateinit var binding: FragmentRecommendSongBinding
    private lateinit var adapter: SongAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecommendSongBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //init recycler view
        initRecyclerView()
        observeViewModel()
    }

    private fun observeViewModel() {
        binding.progressRcmSong.visibility = View.VISIBLE
        viewModel.songs.observe(viewLifecycleOwner) { songs ->
            adapter.updateSongs(songs.subList(0, 10))
            binding.progressRcmSong.visibility = View.GONE
        }
    }

    private fun initRecyclerView() {
        adapter = SongAdapter(
            object : SongAdapter.SongClickListener {
                override fun onSongClick(song: Song) {

                }
            },
            object : SongAdapter.OptionSongClickListener {
                override fun onOptionClick(song: Song) {

                }
            }
        )

        binding.includeRcmSong.rvSongList.adapter = adapter
    }
}