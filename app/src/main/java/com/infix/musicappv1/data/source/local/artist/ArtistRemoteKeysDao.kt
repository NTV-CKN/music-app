package com.infix.musicappv1.data.source.local.artist

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.infix.musicappv1.data.model.artist.ArtistRemoteKeys

@Dao
interface ArtistRemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg artistRemoteKeys: ArtistRemoteKeys)

    @Query(
        """
        SELECT *
        FROM artist_remote_keys
        WHERE artist_id = :artistId
    """
    )
    suspend fun getArtistRemoteKeysByArtistId(artistId: Int): ArtistRemoteKeys?

    @Query(
        """
        DELETE
        FROM artist_remote_keys
    """
    )
    suspend fun clear()
}