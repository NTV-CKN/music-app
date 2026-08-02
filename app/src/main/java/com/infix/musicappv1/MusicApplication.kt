package com.infix.musicappv1

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.firebase.auth.FirebaseAuth
import com.infix.musicappv1.data.repository.PermissionRepository
import com.infix.musicappv1.data.source.local.db.MusicDatabase
import com.infix.musicappv1.ui.base.NetworkCallback
import com.infix.musicappv1.utils.ApiClient
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MusicApplication : Application() {
    @Inject
    lateinit var permissionRepository: PermissionRepository

    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var musicDb: MusicDatabase

    @Inject
    lateinit var networkCallback: NetworkCallback

    override fun onCreate() {
        super.onCreate()
        ApiClient.init(auth, musicDb)
        registerNetworkCallback()
        //observe lifecycle for process of application
        ProcessLifecycleOwner.get().lifecycle.addObserver(object :
            DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val notificationGranted =
                        checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
                    permissionRepository.setGrantedNotification(notificationGranted)
                }
            }
        })
    }

    private fun registerNetworkCallback() {
        val connectManager = getSystemService(ConnectivityManager::class.java)
        connectManager.registerDefaultNetworkCallback(networkCallback)
    }
}