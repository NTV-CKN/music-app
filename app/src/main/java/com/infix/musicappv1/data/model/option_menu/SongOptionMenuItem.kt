package com.infix.musicappv1.data.model.option_menu

import com.infix.musicappv1.enums.SongMenuOptionEnum

data class SongOptionMenuItem(
    val option: SongMenuOptionEnum,
    val title: Int,
    val icon: Int
)
