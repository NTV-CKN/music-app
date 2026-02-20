package com.infix.musicappv1.data.source.local.song

import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.source.SongDataSource
import kotlinx.coroutines.flow.Flow

class SongLocalDataSource(
    private val songDao: SongDao
) : SongDataSource.Local {
    override suspend fun update(vararg song: Song) {
        songDao.update(*song)
    }

    override suspend fun insert(vararg song: Song) {
        songDao.insert(*song)
    }

    override suspend fun delete(vararg song: Song) {
        songDao.delete(*song)
    }

    override suspend fun getAllSongs(): List<Song> {
        return songDao.getAllSongs()
    }

    override fun getTop15SongMostHeard(): Flow<List<Song>> {
        return songDao.getTop15SongMostHeard()
    }

    override fun getTop40SongMostHeard(): Flow<List<Song>> {
        return songDao.getTop40SongMostHeard()
    }

    override fun getAllSongsFlow(): Flow<List<Song>> {
        return songDao.getAllSongsFlow()
    }

    override fun getSongsFavorite(): Flow<List<Song>> {
        return songDao.getSongsFavorite()
    }

    override fun getSongsFavoriteWithLimit(limit: Int): Flow<List<Song>> {
        return songDao.getSongsFavoriteWithLimit(limit)
    }

    override suspend fun updateSongFavorite(id: String, isFavorite: Boolean) {
        songDao.updateFavorite(id, isFavorite)
    }
}