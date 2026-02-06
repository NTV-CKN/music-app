package com.infix.musicappv1.ui

import android.app.Application
import com.infix.musicappv1.media.MediaControllerProvider

class MusicApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MediaControllerProvider.createMediaController(applicationContext)
    }
}