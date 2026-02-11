package com.infix.musicappv1.utils

import android.content.Context
import com.infix.musicappv1.data.repository.PlaybackRepository
import com.infix.musicappv1.data.source.local.db.MusicDatabase
import com.infix.musicappv1.data.source.local.song.SongLocalDataSource

object InjectUtils {
    fun getSongLocalDataSource(context: Context): SongLocalDataSource {
        val db = MusicDatabase.getInstance(context)
        return SongLocalDataSource(db.songDao())
    }

    fun getPlaybackRepository(context: Context): PlaybackRepository {
        val db = MusicDatabase.getInstance(context.applicationContext)
        return PlaybackRepository.getInstance(
            db
        )
    }
}