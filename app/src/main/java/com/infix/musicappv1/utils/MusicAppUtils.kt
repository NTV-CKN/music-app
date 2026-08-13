package com.infix.musicappv1.utils

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import com.infix.musicappv1.data.model.song.Song

object MusicAppUtils {
    const val DEFAULT_PLAYLIST_ID_CUSTOM = 50000L
    const val KEY_FRACTION_EXTRA = "KEY_FRACTION_EXTRA"
    const val ROLE_ADMIN = "admin"
    const val ROLE_USER = "user"

    var density: Float = 0f

    fun getIndexOfSong(song: Song, songs: List<Song>): Int {
        val index = songs.indexOf(song)
        return if (index == -1) 0 else index
    }

    fun getAudioDurationInSeconds(context: Context, uri: Uri): Long {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(context, uri)

            val durationMsStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            val durationMs = durationMsStr?.toLongOrNull() ?: 0L

            durationMs / 1000
        } catch (e: Exception) {
            e.printStackTrace()
            0L
        } finally {
            retriever.release()
        }
    }
}