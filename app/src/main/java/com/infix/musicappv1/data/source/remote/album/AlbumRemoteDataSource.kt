package com.infix.musicappv1.data.source.remote.album

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import com.infix.musicappv1.data.model.album.AlbumList
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.source.AlbumDataSource
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.data.source.remote.MusicService
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AlbumRemoteDataSource @Inject constructor(
    private val musicService: MusicService,
    private val firestore: FirebaseFirestore
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
}