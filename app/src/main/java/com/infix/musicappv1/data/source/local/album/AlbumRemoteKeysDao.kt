package com.infix.musicappv1.data.source.local.album

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.infix.musicappv1.data.model.album.Album
import com.infix.musicappv1.data.model.album.AlbumRemoteKeys

@Dao
interface AlbumRemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg albumRemoteKeys: AlbumRemoteKeys)

    @Query("""
        DELETE FROM album_remote_keys
    """)
    suspend fun clear()

    @Query("""
        SELECT * 
        FROM album_remote_keys
        WHERE album_id = :albumId
    """)
    suspend fun getAlbumRemoteKeysByAlbumId(albumId: String): AlbumRemoteKeys?
}