package com.infix.musicappv1.media

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.annotation.OptIn
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

//we use bound service to provide media controller for app, it helps improve
//app use RAM instead save media controller in singleton object
class MediaControllerService : Service() {
    private lateinit var mediaFuture: ListenableFuture<MediaController>
    private val _controllerFlow = MutableStateFlow<MediaController?>(null)
    private lateinit var binder: BinderImpl

    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()
        createMediaController(this.applicationContext)
        binder = BinderImpl()
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    @OptIn(UnstableApi::class)
    private fun createMediaController(applicationContext: Context) {
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
                    _controllerFlow.value = mediaFuture.get()
                } catch (e: Exception) {
                    Log.e("SVU", e.message ?: "Unknow error at Mediacontrollerprovider")
                    _controllerFlow.value = null
                }
            } else
                _controllerFlow.value = null
        }, MoreExecutors.directExecutor())
    }

    inner class BinderImpl(
        val controllerFlow: StateFlow<MediaController?> = _controllerFlow
    ): Binder()
}