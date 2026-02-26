package com.infix.musicappv1.data.source.local.album

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.infix.musicappv1.data.model.album.Album

@Dao
interface AlbumDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg album: Album)

    @Query("""
        DELETE FROM albums
    """)
    suspend fun clear()

    @Query("""
        SELECT * 
        FROM albums
    """)
    fun loadAlbumsPaging(): PagingSource<Int, Album>

    @Query("""
        SELECT * 
        FROM albums
        LIMIT :limit
    """)
    fun loadNAlbumsPaging(limit: Int = 9): PagingSource<Int, Album>
}