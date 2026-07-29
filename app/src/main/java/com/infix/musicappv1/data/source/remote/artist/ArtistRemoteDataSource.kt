package com.infix.musicappv1.data.source.remote.artist

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.source.ArtistDataSource
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.source.remote.RetrofitHelper
import com.infix.musicappv1.data.source.remote.param.PagingParam
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ArtistRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) : ArtistDataSource.Remote {
    override suspend fun loadArtistsRemote(): Result<List<Artist>> {
        return try {
            val response = RetrofitHelper.musicService.loadArtists()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null)
                    Result.Success(body.artists)
                else
                    Result.Error(Exception("Body of response artists is null"))
            } else
                Result.Error(Exception(response.message()))
        } catch (ex: Exception) {
            Result.Error(Exception(ex.message ?: "Unknown error"))
        }
    }

    override suspend fun loadArtistsPaging(pagingParam: PagingParam): List<Artist>? {
        return try {
            RetrofitHelper.musicService.loadArtistsPaging(pagingParam).body()?.artists
        } catch (ex: Exception) {
            null
        }
    }

    override suspend fun loadSongsByArtistId(artistId: Int): Result<List<Song>> {
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
}