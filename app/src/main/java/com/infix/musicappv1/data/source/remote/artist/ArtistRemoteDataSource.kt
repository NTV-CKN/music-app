package com.infix.musicappv1.data.source.remote.artist

import androidx.core.net.toUri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import com.google.firebase.storage.FirebaseStorage
import com.infix.musicappv1.data.dto.BaseResultResponse
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.data.model.artist.ArtistList
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.source.ArtistDataSource
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.source.remote.MusicService
import com.infix.musicappv1.utils.FormatSongPathUtils
import kotlinx.coroutines.tasks.await
import retrofit2.Response
import javax.inject.Inject

class ArtistRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val musicService: MusicService,
    private val storage: FirebaseStorage
) : ArtistDataSource.Remote {
//    override suspend fun loadArtistsRemote(): Result<List<Artist>> {
//        return try {
//            val response = RetrofitHelper.musicService.loadArtists()
//            if (response.isSuccessful) {
//                val body = response.body()
//                if (body != null)
//                    Result.Success(body.artists)
//                else
//                    Result.Error(Exception("Body of response artists is null"))
//            } else
//                Result.Error(Exception(response.message()))
//        } catch (ex: Exception) {
//            Result.Error(Exception(ex.message ?: "Unknown error"))
//        }
//    }

    override suspend fun loadArtistsPaging(
        query: String,
        limit: Int,
        key: Int
    ): ArtistList? {
        val result = musicService.loadArtistsPaging(query, limit, key)
        return if (result.isSuccessful) {
            result.body() ?: ArtistList()
        } else
            ArtistList()
    }

    override suspend fun loadSongsByArtistId(artistId: Long): Result<List<Song>> {
        return try {
            val task = firestore.collection("songs")
                .whereEqualTo("artistId", artistId)
                .get()
                .await()

            Result.Success(task.toObjects<Song>())
        } catch (ex: Exception) {
            Result.Error(Exception(ex.message ?: "Unknown error"))
        }
    }

    override suspend fun saveArtist(artist: Artist): Response<BaseResultResponse> {
        return musicService.saveArtist(artist)
    }

    override suspend fun uploadAvatar(avatar: String, id: String): String? {
        var uploadedAvatarUrl: String? = null

        if (FormatSongPathUtils.isAndroidUri(avatar)) {
            val avatarUri = avatar.toUri()
            val imageRef = storage.reference.child("temp_storage/artists/avatar/$id.jpg")
            imageRef.putFile(avatarUri).await()
            uploadedAvatarUrl = imageRef.downloadUrl.await().toString()
        }

        return uploadedAvatarUrl
    }

    override suspend fun deleteArtist(body: Map<String, Long>): Response<BaseResultResponse> {
        return musicService.deleteArtist(body)
    }
}