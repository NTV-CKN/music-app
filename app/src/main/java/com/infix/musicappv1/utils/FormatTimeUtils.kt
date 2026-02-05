package com.infix.musicappv1.utils

import android.R.attr.duration
import android.annotation.SuppressLint
import kotlin.time.Duration.Companion.seconds

object FormatTimeUtils {
    @SuppressLint("DefaultLocale")
    fun formatSecondToMinute(second: Int): String {
        val duration = second.seconds
        return duration.toComponents { minutes, seconds, _ ->
            String.format("%02d:%02d", minutes, seconds)
        }
    }
}