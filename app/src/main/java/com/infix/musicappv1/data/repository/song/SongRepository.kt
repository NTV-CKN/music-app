package com.infix.musicappv1.data.repository.song

import com.infix.musicappv1.data.model.song.SongList
import com.infix.musicappv1.data.source.Result

interface SongRepository {
    suspend fun loadSongs(): Result<SongList>
}