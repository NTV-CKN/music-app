package com.infix.musicappv1.data.source.remote.song

import android.util.Log
import androidx.core.net.toUri
import com.google.firebase.storage.FirebaseStorage
import com.infix.musicappv1.data.dto.BaseResultResponse
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.model.song.SongList
import com.infix.musicappv1.data.source.SongDataSource
import com.infix.musicappv1.data.source.remote.MusicService
import com.infix.musicappv1.ui.admin.song.update_add.AddOrUpdateSongViewModel
import com.infix.musicappv1.utils.FormatSongPathUtils
import kotlinx.coroutines.tasks.await
import retrofit2.Response
import javax.inject.Inject

class SongRemoteDataSource @Inject constructor(
    private val musicService: MusicService,
    private val storage: FirebaseStorage
) : SongDataSource.Remote {
    override suspend fun loadSongs(query: String, limit: Int, key: Int): SongList? {
        return try {
            val response = musicService.loadSongsPaging(query, limit, key)
            Log.d("SongRemoteDataSource", "response: " + response.body())
            response.body()
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun saveSong(song: Song): Response<BaseResultResponse> {
        return musicService.saveSong(song)
    }

    override suspend fun uploadSourcesSong(
        id: String,
        image: String?,
        source: String?
    ): AddOrUpdateSongViewModel.MediaUploadResult {
        var uploadedImageUrl: String? = null
        var uploadedSourceUrl: String? = null

        if (image != null && FormatSongPathUtils.isAndroidUri(image)) {
            val imageUri = image.toUri()
            val imageRef = storage.reference.child("temp_storage/images/$id.jpg")
            imageRef.putFile(imageUri).await()
            uploadedImageUrl = imageRef.downloadUrl.await().toString()
        }

        if (source != null && FormatSongPathUtils.isAndroidUri(source)) {
            val sourceUri = source.toUri()
            val sourceRef = storage.reference.child("temp_storage/audio/$id.mp3")
            sourceRef.putFile(sourceUri).await()
            uploadedSourceUrl = sourceRef.downloadUrl.await().toString()
        }

        return AddOrUpdateSongViewModel.MediaUploadResult(
            imageUrl = uploadedImageUrl,
            sourceUrl = uploadedSourceUrl
        )
    }
}