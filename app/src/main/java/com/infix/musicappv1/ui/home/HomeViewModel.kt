package com.infix.musicappv1.ui.home

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.application
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.infix.musicappv1.data.model.ai_rcm.AiRecommendationResponse
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.repository.ai_rcm.IAIRecommendRepository
import com.infix.musicappv1.data.repository.album.AlbumRepository
import com.infix.musicappv1.data.repository.paging.mediator.SongRemoteMediator
import com.infix.musicappv1.data.repository.song.SongRepository
import com.infix.musicappv1.data.source.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
@Suppress("UNCHECKED_CAST")
class HomeViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val albumRepository: AlbumRepository,
    private val aiRcmRepository: IAIRecommendRepository,
    factory: SongRemoteMediator.FactoryAssisted,
    application: Application
) : AndroidViewModel(application) {
    //when any fragment has paging song, room update more song and notify for songLocal
    val songLocal: LiveData<List<Song>?> = songRepository.getAllSongsFlow().asLiveData()
    val albumLocal: LiveData<List<Album>?> = albumRepository.loadAllAlbumsFlow().asLiveData()

    @OptIn(ExperimentalPagingApi::class)
    val songs = Pager(
        config = PagingConfig(
            pageSize = 20,
            initialLoadSize = 20,
            prefetchDistance = 1,
            enablePlaceholders = false
        ),
        remoteMediator = factory.create(true),
    ) {
        songRepository.getNSongsPaging(SIZE_SONG)
    }.flow.cachedIn(viewModelScope)//cached to avoid refresh when user return app

    fun loadRecommendSongsByAI(
        promptClient: String,
        onSuccess: (songs: AiRecommendationResponse) -> Unit,
        onFailed: () -> Unit
    ) {
        if (promptClient.isEmpty()) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val result = aiRcmRepository.loadSongRecommend(promptClient)
            withContext(Dispatchers.Main) {
                if (result is com.infix.musicappv1.data.source.Result.Success) {
                    onSuccess.invoke(result.data)
                } else if (result is Result.Error) {
                    Toast.makeText(
                        application.applicationContext,
                        result.err.message ?: "Cannot load recommend songs",
                        Toast.LENGTH_SHORT
                    ).show()
                    onFailed.invoke()
                }
            }
        }
    }

    companion object {
        const val SIZE_SONG = 10
    }
}