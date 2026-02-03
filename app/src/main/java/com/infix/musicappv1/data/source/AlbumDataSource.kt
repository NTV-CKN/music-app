package com.infix.musicappv1.data.source

import com.infix.musicappv1.data.model.album.AlbumList

interface AlbumDataSource {
    interface Remote {
       suspend fun loadAlbums(): Result<AlbumList>
    }

    interface Local {
        //TODO
    }
}