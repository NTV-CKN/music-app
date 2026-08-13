package com.infix.musicappv1.data.source.remote.album

import androidx.core.net.toUri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import com.google.firebase.storage.FirebaseStorage
import com.infix.musicappv1.data.dto.BaseResultResponse
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.model.album.AlbumList
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.source.AlbumDataSource
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.source.remote.MusicService
import com.infix.musicappv1.utils.FormatSongPathUtils
import kotlinx.coroutines.tasks.await
import retrofit2.Response
import javax.inject.Inject

class AlbumRemoteDataSource @Inject constructor(
    private val musicService: MusicService,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : AlbumDataSource.Remote {
    override suspend fun loadAlbumsPaging(
        query: String,
        limit: Int,
        key: Int
    ): AlbumList? {
        val result = musicService.loadAlbumsPaging(query, limit, key)
        return if (result.isSuccessful) {
            result.body() ?: AlbumList()
        } else
            AlbumList()
    }


    override suspend fun loadSongsByAlbumId(albumId: String): Result<List<Song>> {
        return try {
            val albumSnapshot = firestore.collection("albums").document(albumId).get().await()

            if (!albumSnapshot.exists()) {
                return Result.Error(Exception("Album không tồn tại"))
            }

            val nameAlbum = albumSnapshot.getString("name")
                ?: return Result.Error(Exception("Tên album bị rỗng"))

            val songsSnapshot = firestore.collection("songs")
                .whereEqualTo("album", nameAlbum)
                .get()
                .await()

            val songs = songsSnapshot.toObjects<Song>()

            return Result.Success(songs)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteAlbum(body: Map<String, String>): Response<BaseResultResponse> {
        return musicService.deleteAlbum(body)
    }

    override suspend fun saveAlbum(album: Album): Response<BaseResultResponse> {
        return musicService.saveAlbum(album)
    }

    override suspend fun uploadArtwork(artwork: String, id: String): String? {
        var uploadedArtworkUrl: String? = null

        if (FormatSongPathUtils.isAndroidUri(artwork)) {
            val artworkUri = artwork.toUri()
            val imageRef = storage.reference.child("temp_storage/albums/artwork/$id.jpg")
            imageRef.putFile(artworkUri).await()
            uploadedArtworkUrl = imageRef.downloadUrl.await().toString()
        }

        return uploadedArtworkUrl
    }
}