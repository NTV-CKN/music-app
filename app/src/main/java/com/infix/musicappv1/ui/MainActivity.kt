package com.infix.musicappv1.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.infix.musicappv1.R
import com.infix.musicappv1.data.repository.PlaybackRepository
import com.infix.musicappv1.data.source.local.db.MusicDatabase
import com.infix.musicappv1.databinding.ActivityMainBinding
import com.infix.musicappv1.ui.MainActivity.Companion.PREF_PREV_SESSION
import com.infix.musicappv1.ui.viewmodels.Factory
import com.infix.musicappv1.ui.viewmodels.PlayingSongSharedViewModel
import com.infix.musicappv1.utils.InjectUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.getValue

//data store pref (Guarantee must only one instance for one file
private val Context.datastorePrefSession by preferencesDataStore(name = PREF_PREV_SESSION)

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val playingSongSharedViewModel: PlayingSongSharedViewModel by viewModels {
        Factory(InjectUtils.getPlaybackRepository(this))
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
        initializeNavHostFragment()
        setupObserver()
    }


    override fun onStop() {
        super.onStop()
        saveSessionPlaying()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun setupObserver() {
        playingSongSharedViewModel.isDataReady.observe(this) { isReady ->
            if (isReady)
                lifecycleScope.launch(Dispatchers.IO) {
                    restorePrevSession()
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
                        pref[KEY_ID_ALBUM] = currentPlaylist.idPlaylist
                        pref[KEY_SONG_ID] = playingSong.id
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


    private suspend fun restorePrevSession() {
        val dataPref = datastorePrefSession.data.first()
        val playlistId = dataPref[KEY_ID_ALBUM]
        val songId = dataPref[KEY_SONG_ID]
        Log.d("MainActivity", "restoePrevSession: songId ${songId} and playlistId ${playlistId}")
        playingSongSharedViewModel.restorePrevSession(songId, playlistId)
    }


    companion object {
        const val PREF_PREV_SESSION = "com.infix.musicappv1.ui.MainActivity.PREF_PREV_SESSION"
        val KEY_SONG_ID: Preferences.Key<String> =
            stringPreferencesKey("com.infix.musicappv1.ui.MainActivity.KEY_SONG_ID")
        val KEY_ID_ALBUM: Preferences.Key<Int> =
            intPreferencesKey("com.infix.musicappv1.ui.MainActivity.KEY_ID_ALBUM")
    }
}