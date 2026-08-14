package com.infix.musicappv1.ui.admin

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.infix.musicappv1.R
import com.infix.musicappv1.databinding.ActivityAdminBinding
import com.infix.musicappv1.ui.MainActivity
import com.infix.musicappv1.ui.auth.AuthViewModel
import com.infix.musicappv1.utils.SnackbarUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding
    private val authViewModel by viewModels<AuthViewModel>()

    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initDrawerAndNavController()
        setupToolbarMenu()
    }

    private fun initDrawerAndNavController() {
        navController = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment_container_admin
        )?.findNavController()

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigate_song_management,
                R.id.navigate_manage_albums,
                R.id.navigate_manage_artists,
            ),
            drawerLayout = binding.drawerLayout
        )

        if (navController == null) return

        binding.toolbar.setupWithNavController(navController!!, appBarConfiguration)
        binding.navigationView.setupWithNavController(navController!!)
    }

    private fun setupToolbarMenu() {
        addMenuProvider(object : androidx.core.view.MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_toolbar_user_management, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return if (menuItem.itemId == R.id.menu_action_sign_out) {
                    SnackbarUtils.showSnackbarWithAction(
                        binding.root,
                        getString(R.string.txt_confirm_sign_out),
                        getString(R.string.txt_signout),
                        Snackbar.LENGTH_LONG
                    ) {
                        handleOnLogout()
                    }
                    true
                } else {
                    false
                }
            }
        }, this, Lifecycle.State.RESUMED)
    }

    private fun handleOnLogout() {
        authViewModel.logout()
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }

}