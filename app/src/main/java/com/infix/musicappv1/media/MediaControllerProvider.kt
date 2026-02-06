package com.infix.musicappv1.media

import android.content.ComponentName
import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors

object MediaControllerProvider {
    private var mediaController: MediaController? = null
    private lateinit var mediaFuture: ListenableFuture<MediaController>

    @OptIn(UnstableApi::class)
    fun createMediaController(applicationContext: Context) {
        //create session token
        val sessionToken = SessionToken(
            applicationContext, ComponentName(
                applicationContext,
                PlaybackService::class.java
            )
        )

        //media controller
        mediaFuture = MediaController.Builder(applicationContext, sessionToken)
            .buildAsync()
        mediaFuture.addListener({
            if (mediaFuture.isDone && !mediaFuture.isCancelled) {
                try {
                    mediaController = mediaFuture.get()
                } catch (e: Exception) {
                    Log.e("SVU", e.message ?: "Unknow error at Mediacontrollerprovider")
                }
            } else
                mediaController = null
        }, MoreExecutors.directExecutor())
    }

    fun getMediaController() = mediaController
}