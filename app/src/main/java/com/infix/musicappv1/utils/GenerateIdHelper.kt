package com.infix.musicappv1.utils

import kotlin.random.Random

object GenerateIdHelper {
    fun generateSongId(): String {
        return (System.currentTimeMillis() + Random.nextInt(1, 1000)).toString()
    }
}