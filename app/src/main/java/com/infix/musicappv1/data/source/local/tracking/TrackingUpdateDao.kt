package com.infix.musicappv1.data.source.local.tracking

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.infix.musicappv1.data.model.tracking.TrackingUpdate

@Dao
interface TrackingUpdateDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg trackingUpdate: TrackingUpdate)

    @Query(
        """
       SELECT song_update_at
       FROM tracking_update
       ORDER BY song_update_at DESC
       LIMIT 1
       )
   """
    )
    suspend fun getLastUpdateSongs(): Long
}