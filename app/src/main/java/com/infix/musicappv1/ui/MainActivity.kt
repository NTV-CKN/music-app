package com.infix.musicappv1.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.DisplayMetrics
import android.util.DisplayMetrics.DENSITY_DEFAULT
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.infix.musicappv1.R
import com.infix.musicappv1.data.repository.PermissionRepository
import com.infix.musicappv1.databinding.ActivityMainBinding
import com.infix.musicappv1.ui.MainActivity.Companion.PREF_PREV_SESSION
import com.infix.musicappv1.ui.viewmodels.PlayingSongSharedViewModel
import com.infix.musicappv1.utils.MusicAppUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

//data store pref (Guarantee must only one instance for one file
val Context.datastorePrefSession by preferencesDataStore(name = PREF_PREV_SESSION)

@AndroidEntryPoint
@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var permissionRepository: PermissionRepository
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val playingSongSharedViewModel: PlayingSongSharedViewModel by viewModels()

    //    private val homeViewModel: HomeViewModel by viewModels {
//        HomeViewModel.Factory(
//            InjectUtils.getSongRepository(this.applicationContext),
//            InjectUtils.getPlaylistRepository(this.applicationContext),
//            MusicDatabase.getInstance(this.applicationContext)
//        )
//    }
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionRepository.setGrantedNotification(isGranted)
        if (!isGranted) {
            Snackbar.make(
                binding.root,
                getString(R.string.txt_permission_denied),
                Snackbar.LENGTH_SHORT
            ).setAnchorView(binding.bottomNav)
                .show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//        if (savedInstanceState != null) {
//            if (!savedInstanceState.getBoolean(KEY_IS_LOADED, false))
//                homeViewModel.setupDataTmp()
//            else
//                homeViewModel.loadLocalData()
//        }
        initializeNavHostFragment()
        setupObserver()
        calculateDensityOfApp()
    }

    private fun calculateDensityOfApp() {
        //for android >= 11
        MusicAppUtils.density = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetric = windowManager.currentWindowMetrics
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
                windowMetric.density
            else
                (windowMetric.bounds.width() / DENSITY_DEFAULT) * 1f
        } else {
            val displayMetric = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetric)
            displayMetric.density
        }
    }

    override fun onStop() {
        super.onStop()
        saveSessionPlaying()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putBoolean(KEY_IS_LOADED, true)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    @SuppressLint("InlinedApi")
    private fun setupObserver() {
        //restore previous session
        playingSongSharedViewModel.isDataReady.observe(this) { isReady ->
            if (isReady)
                lifecycleScope.launch(Dispatchers.IO) {
                    restorePrevSession()
                }
        }
        //observe require ask permission
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                //notification
                launch {
                    permissionRepository.askPermissionNotification.collectLatest {
                        val isGranted =
                            permissionRepository.isGrantedNotification.value
                        if (it == null || isGranted == null) return@collectLatest
                        if (!isGranted && it) {
                            showAskPermission(Manifest.permission.POST_NOTIFICATIONS)
                            permissionRepository.setAskPermissionNotification(false)
                        }
                    }
                }//end coroutine notification
            }
        }
    }

    private fun saveSessionPlaying() {
        val currentPlaylist = playingSongSharedViewModel.currentPlaylist.value
        val playingSong = playingSongSharedViewModel.playingSongLivedata.value?.song
        if (currentPlaylist == null || playingSong == null) return
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                withContext(NonCancellable) {
                    datastorePrefSession.edit { pref ->
                        pref[KEY_ID_PLAYLIST] = currentPlaylist.playlistId
                        pref[KEY_SONG_ID] = playingSong.id
//                        Log.d("FIX", "save session playing: ${playingSong.id}")
                    }
                }
            } catch (e: Exception) {
//                Log.e("DataStore", "Error saving session", e)
            }
        }
    }

    private fun initializeNavHostFragment() {
        val navHost = supportFragmentManager.findFragmentById(R.id.fragment_host_container)
            ?: throw Exception("Cannot find nav host fragment")
        navController = navHost.findNavController()

        binding.bottomNav.setupWithNavController(navController)
    }


    //restore playlist play lastest, if song get from API but still not save in room yet,
    //restore may not load full song of this playlist
    private suspend fun restorePrevSession() {
        val dataPref = datastorePrefSession.data.first()
        val playlistId = dataPref[KEY_ID_PLAYLIST]
        val songId = dataPref[KEY_SONG_ID]
//        Log.d("FIX", "restoePrevSession: songId ${songId} and playlistId ${playlistId}")
        playingSongSharedViewModel.restorePrevSession(songId, playlistId)
    }

    private fun showAskPermission(namePermission: String) {
        when (namePermission) {
            Manifest.permission.POST_NOTIFICATIONS -> askNotificationPermission()
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {

            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.txt_re_require_permission_notification),
                    Snackbar.LENGTH_INDEFINITE
                ).setAnchorView(binding.bottomNav)
                    .setAction(getString(R.string.txt_agree)) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                    .show()
            } else {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }


    companion object {
        const val KEY_IS_LOADED = "KEY_IS_LOADED"
        const val PREF_PREV_SESSION = "com.infix.musicappv1.ui.MainActivity.PREF_PREV_SESSION"
        val KEY_SONG_ID: Preferences.Key<String> =
            stringPreferencesKey("com.infix.musicappv1.ui.MainActivity.KEY_SONG_ID")
        val KEY_ID_PLAYLIST: Preferences.Key<Int> =
            intPreferencesKey("com.infix.musicappv1.ui.MainActivity.KEY_ID_ALBUM")
        val KEY_ID_PLAYLIST_CUSTOM =
            intPreferencesKey("com.infix.musicappv1.ui.MainActivity.KEY_ID_PLAYLIST_CUSTOM")
    }
}