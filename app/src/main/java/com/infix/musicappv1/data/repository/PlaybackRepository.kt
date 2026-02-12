package com.infix.musicappv1.data.repository

import androidx.room.withTransaction
import com.infix.musicappv1.data.model.now_playing.MediaItemTransitionWrap
import com.infix.musicappv1.data.model.playlist.Playlist
import com.infix.musicappv1.data.model.playlist.PlaylistSong
import com.infix.musicappv1.data.model.playlist.PlaylistWithSongs
import com.infix.musicappv1.data.model.recent.SongRecent
import com.infix.musicappv1.data.model.song.Song
import com.infix.musicappv1.data.source.local.db.MusicDatabase
import com.infix.musicappv1.enums.PlaylistEnum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import kotlin.collections.set

class PlaybackRepository private constructor(
    private val db: MusicDatabase
) {
    //observe song transition
    private val _mediaItemTransition: MutableStateFlow<MediaItemTransitionWrap?> =
        MutableStateFlow(null)
    val mediaItemTransition: StateFlow<MediaItemTransitionWrap?> = _mediaItemTransition

    //observe current playlist when song clicked at any playlist
    private val _currentPlaylist: MutableStateFlow<Playlist?> = MutableStateFlow(null)
    val currentPlaylist: StateFlow<Playlist?> = _currentPlaylist

    //observe index to play when PlayerMedia recall mediaItemTransition
    private val _indexToPlay = MutableStateFlow<IndexToPlayDate?>(null)
    val indexToPlay: StateFlow<IndexToPlayDate?> = _indexToPlay

    //observe status Player isplaying
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    //observe song favorite
    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    private var mediaItemCurrentIndex: Int? = null

    //guarantee when user comeback app after swipe out
    private var playlistTrackCurrent: Playlist? = null
    private val playlists: MutableMap<String, Playlist> = hashMapOf()

    init {
        PlaylistEnum.entries.forEach { enum ->
            playlists[enum.value] = (Playlist(namePlaylist = enum.value))
        }
    }

    suspend fun updateMediaTransition(
        mediaItemTransitionWrap: MediaItemTransitionWrap?
    ) {
        _mediaItemTransition.value = mediaItemTransitionWrap
        mediaItemTransitionWrap?.index?.let {
            //when user not in app and comeback, this code help miniplayer update correct song on notification media
            updateIndexToPlay(it)
            updateIsFavorite()
            val playlist = currentPlaylist.value
            val songs = playlist?.songs
            if (playlist != null)
                withContext(Dispatchers.IO) {
                    insertPlaylistAndSongs(playlist, songs!!)
                }
        }
    }

    fun getIndexToPlay() = indexToPlay.value

    fun updateIndexToPlay(index: Int) {
        _indexToPlay.value = IndexToPlayDate(index)
        mediaItemCurrentIndex = index
    }

    fun updatePlaylist(playlist: Playlist) {
        _currentPlaylist.value = playlist
        playlistTrackCurrent = playlist
    }

    fun getMediaItemIndexCurrent() = mediaItemCurrentIndex

    fun getPlaylists() = playlists

    fun getPlaylistTrackCurrent() =
        playlistTrackCurrent

    fun setIsPlaying(boolean: Boolean) {
        _isPlaying.value = boolean
    }

    suspend fun insertSongRecent(songRecent: SongRecent) {
        db.songRecentDao().insert(songRecent)
        db.songRecentDao().trimSongRecents()
    }

    suspend fun updateSong(song: Song) {
        db.songDao().update(song)
    }

    suspend fun updateSongFavorite(id: String, isFavorite: Boolean) {
        db.songDao().updateFavorite(id, isFavorite)
        //if current song playing is favorite, we need update UI for that
        //else we only write favorite for song id and non update UI
        _indexToPlay.value?.let { indexToPlay ->
            val currentSong = playlistTrackCurrent?.songs?.getOrNull(indexToPlay.indexToPlay ?: -1)
            if (currentSong != null && currentSong.id == id)
                _isFavorite.update { isFavorite }
//            Log.d("SVU", isFavorite.toString())
        }
    }

    suspend fun insertPlaylistAndSongs(playlist: Playlist, songs: List<Song>) {
        db.withTransaction {
            db.playlistDao().insert(playlist)
            val playlistSongs = mutableListOf<PlaylistSong>()
            for (song in songs)
                playlistSongs.add(PlaylistSong(playlist.idPlaylist, song.id))
            db.playlistSongDao().insert(*playlistSongs.toTypedArray())
        }
    }

    suspend fun getPlaylistWithSongs(playlistId: Int): PlaylistWithSongs? {
        return db.playlistDao().getPlaylistWithSongsById(playlistId)
    }

    private fun updateIsFavorite() {
        val song = playlistTrackCurrent?.songs?.getOrNull(_indexToPlay.value?.indexToPlay ?: -1)
        song?.let {
            _isFavorite.value = song.favorite
        }
    }

    data class IndexToPlayDate(
        val indexToPlay: Int?,
        val eventId: Long = System.nanoTime()
    )

    companion object {
        @Volatile
        private var instance: PlaybackRepository? = null
        fun getInstance(
            db: MusicDatabase
        ): PlaybackRepository {
            if (instance == null) {
                synchronized(this) {
                }
                if (instance == null)
                    instance =
                        PlaybackRepository(db)
            }
            return instance!!
        }
    }
}