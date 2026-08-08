package com.infix.musicappv1.utils

import android.util.Patterns
import android.webkit.URLUtil
import androidx.core.net.toUri

object FormatSongPathUtils {
    fun isAndroidUri(input: String): Boolean {
        if (input.isBlank()) return false
        return try {
            val uri = input.toUri()
            val scheme = uri.scheme?.lowercase()
            scheme in listOf("content", "file", "android.resource")
        } catch (e: Exception) {
            false
        }
    }

    fun isValidUriOrUrl(input: String): Boolean {
        if (input.isBlank()) return false

        val isWebUrl = (URLUtil.isHttpUrl(input) || URLUtil.isHttpsUrl(input))
                && Patterns.WEB_URL.matcher(input).matches()

        return isWebUrl || isAndroidUri(input)
    }
}