package com.infix.musicappv1.data.source.local.artist

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.infix.musicappv1.data.model.artist.ArtistSongCrossRef

@Dao
interface ArtistSongDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg artistSongCrossRef: ArtistSongCrossRef)
}