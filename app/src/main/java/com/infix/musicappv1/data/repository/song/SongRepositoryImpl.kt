package com.infix.musicappv1.data.repository.song

import androidx.paging.PagingSource
import com.infix.musicappv1.data.dto.BaseResultResponse
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.model.song.SongList
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.source.SongDataSource
import com.infix.musicappv1.ui.admin.song.update_add.AddOrUpdateSongViewModel
import com.infix.musicappv1.utils.FormatSongPathUtils
import com.infix.musicappv1.utils.GenerateIdHelper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SongRepositoryImpl @Inject constructor(
    private val remote: SongDataSource.Remote,
    private val local: SongDataSource.Local
) : SongRepository {
    override suspend fun loadSongsPaging(query: String, limit: Int, key: Int): SongList? {
        return remote.loadSongs(query, limit, key)
    }

    override fun getAllSongsFlow(): Flow<List<Song>> {
        return local.getAllSongsFlow()
    }


    override suspend fun insert(vararg song: Song) {
        local.insert(*song)
    }

    override fun getSongsFavoriteWithLimit(limit: Int): Flow<List<Song>> {
        return local.getSongsFavoriteWithLimit(limit)
    }

    override fun getSongsFavoriteFlow(): Flow<List<Song>> {
        return local.getSongsFavorite()
    }

    override fun getNSongsPaging(limit: Int): PagingSource<Int, Song> {
        return local.getNSongsPaging(limit)
    }

    override fun getTop15SongMostHeard(): Flow<List<Song>> {
        return local.getTop15SongMostHeard()
    }

    override fun getTop40SongMostHeard(): Flow<List<Song>> {
        return local.getTop40SongMostHeard()
    }

    override fun getAllSongsPaging(): PagingSource<Int, Song> {
        return local.getAllSongsPaging()
    }

    override suspend fun saveSong(song: Song, isUpdate: Boolean): Result<BaseResultResponse> {
        try {
            if (!isUpdate || song.id.isBlank()) {
            song.id = GenerateIdHelper.generateSongId()
        }
            val imageUpload: String? =
                if (!FormatSongPathUtils.isAndroidUri(song.image))
                    null
                else
                    song.image
            val sourceUpload: String? =
                if (!FormatSongPathUtils.isAndroidUri(song.source))
                    null
                else
                    song.source

            val resultUpload = uploadSourcesSong(
                song.id,
                imageUpload,
                sourceUpload
            )

            resultUpload.sourceUrl?.let { song.source = it }
            resultUpload.imageUrl?.let { song.image = it }

            val response = if (isUpdate) {
                remote.updateSong(song)
            } else {
                remote.saveSong(song)
            }

            return if (response.isSuccessful) {
                Result.Success(
                    response.body() ?: BaseResultResponse(
                        true, "Thao tác thành công"
                    )
                )
            } else {
                Result.Success(
                    BaseResultResponse(
                        false, "Thao tác thất bại"
                    )
                )
            }
        } catch (ex: Exception) {
            return Result.Error(ex)
        }
    }

    override suspend fun uploadSourcesSong(
        id: String,
        image: String?,
        source: String?
    ): AddOrUpdateSongViewModel.MediaUploadResult {
        return remote.uploadSourcesSong(id, image = image, source = source)
    }

    override suspend fun getSongsByNameSongOrNameArtist(key: String): List<Song> {
        return local.getSongsByNameSongOrNameArtist(key)
    }
}