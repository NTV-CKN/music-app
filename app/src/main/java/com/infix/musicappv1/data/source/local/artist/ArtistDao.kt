package com.infix.musicappv1.data.source.local.artist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.infix.musicappv1.data.model.artist.Artist
import com.infix.musicappv1.data.model.artist.ArtistWithSongs
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtistDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg artist: Artist)
    @Delete
    suspend fun delete(vararg artist: Artist)
    @Update
    suspend fun update(vararg artist: Artist)
    @Query("""
        SELECT *
        FROM artists
    """)
    fun getAllArtists(): Flow<List<Artist>>

    @Query("""
        SELECT *
        FROM artists
        LIMIT :limit
    """)
    fun getLimitArtists(limit: Int = 10): Flow<List<Artist>>

    @Transaction
    @Query("""
        SELECT *
        FROM artists
        WHERE artist_id = :artistId
    """)
    suspend fun getArtistWithSongsByArtistId(artistId: Int): ArtistWithSongs?
}