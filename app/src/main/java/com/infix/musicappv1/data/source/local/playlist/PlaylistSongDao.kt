package com.infix.musicappv1.data.source.local.playlist

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.infix.musicappv1.data.model.playlist.PlaylistSong

@Dao
interface PlaylistSongDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg playlistSong: PlaylistSong)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertStrict(playlistSong: PlaylistSong)
}