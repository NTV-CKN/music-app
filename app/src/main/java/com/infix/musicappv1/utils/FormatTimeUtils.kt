package com.infix.musicappv1.utils

import android.R.attr.duration
import android.annotation.SuppressLint
import java.util.Locale
import kotlin.time.Duration.Companion.seconds

object FormatTimeUtils {
    @SuppressLint("DefaultLocale")
    fun formatSecondToMinute(second: Int): String {
        val duration = second.seconds
        return duration.toComponents { minutes, seconds, _ ->
            String.format("%02d:%02d", minutes, seconds)
        }
    }

    fun getMinuteAndSecond(duration: Long): String {
        val minute = duration / (1000 * 60)
        val second = (duration / 1000) % 60
        if (minute == 0L && second == 0L)
            return "00:00"

        return String.format(Locale.ENGLISH, "%02d:%02d", minute, second)
    }
}