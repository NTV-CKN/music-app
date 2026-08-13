package com.infix.musicappv1.utils

import kotlin.random.Random

object GenerateIdHelper {
    fun generateId(): String {
        return (System.currentTimeMillis() + Random.nextInt(1, 1000)).toString()
    }
}