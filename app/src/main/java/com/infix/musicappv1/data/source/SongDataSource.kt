package com.infix.musicappv1.data.source

import com.infix.musicappv1.data.model.song.SongList

interface SongDataSource {
    interface Remote {
        suspend fun loadSongs(): Result<SongList>
    }

    interface Local {
        //TODO
    }
}