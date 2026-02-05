package com.infix.musicappv1.utils

import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.option_menu.SongOptionMenuItem
import com.infix.musicappv1.enums.SongMenuOptionEnum

object SongOptionMenuUtils {
    val optionMenuSongs: List<SongOptionMenuItem> = listOf(
        SongOptionMenuItem(SongMenuOptionEnum.DOWNLOAD, R.string.txt_download, R.drawable.ic_download_24px),
        SongOptionMenuItem(SongMenuOptionEnum.ADD_TO_FAVORITES, R.string.txt_add_to_favorite, R.drawable.ic_favorite_24px),
        SongOptionMenuItem(SongMenuOptionEnum.ADD_TO_PLAYLIST, R.string.txt_add_to_playlist, R.drawable.ic_playlist_add_24px),
        SongOptionMenuItem(SongMenuOptionEnum.PLAY_NEXT, R.string.txt_play_next, R.drawable.ic_play_next_music_24px),
        SongOptionMenuItem(SongMenuOptionEnum.VIEW_ALBUM, R.string.txt_view_album, R.drawable.ic_album_24px),
        SongOptionMenuItem(SongMenuOptionEnum.VIEW_ARTIST, R.string.txt_view_artist, R.drawable.ic_artist_24px),
        SongOptionMenuItem(SongMenuOptionEnum.BLOCK_SONG, R.string.txt_block_song, R.drawable.ic_block_24px),
        SongOptionMenuItem(SongMenuOptionEnum.REPORT_ERROR, R.string.txt_report_err, R.drawable.ic_bug_report_24px),
        SongOptionMenuItem(SongMenuOptionEnum.VIEW_SONG_INFO, R.string.txt_view_song_info, R.drawable.ic_info_24px)
    )
}