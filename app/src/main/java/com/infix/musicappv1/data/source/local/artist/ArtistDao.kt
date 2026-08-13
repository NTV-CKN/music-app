package com.infix.musicappv1.data.source.local.artist

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.infix.musicappv1.data.model.artist.Artist
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
    suspend fun getAllArtist(): List<Artist>

    @Query("""
        SELECT * 
        FROM artists
        WHERE artist_id = :artistId
    """)
    fun getArtistById(artistId: Long): Artist?

    @Query(
        """
        SELECT *
        FROM artists
    """
    )
    fun getAllArtists(): Flow<List<Artist>>

    @Query(
        """
        SELECT *
        FROM artists
        LIMIT :limit
    """
    )
    fun getLimitArtists(limit: Int = 10): Flow<List<Artist>>

    @Query(
        """
        SELECT *
        FROM artists
        WHERE artist_is_interested = 1
        LIMIT :limit
    """
    )
    fun getLimitArtistInterested(limit: Int = 10): Flow<List<Artist>>

    @Query(
        """
        SELECT *
        FROM artists
        WHERE artist_is_interested = 1
    """
    )
    fun getAllArtistInterested(): Flow<List<Artist>>

    @Query(
        """
        DELETE FROM artists
    """
    )
    suspend fun clear()

    @Query("""
        SELECT * 
        FROM artists
    """)
    fun getArtistsPaging(): PagingSource<Int, Artist>

    @Query("""
        SELECT * 
        FROM artists
        LIMIT :limit
    """)
    fun getNArtistsPaging(limit: Int = 10): PagingSource<Int, Artist>
}