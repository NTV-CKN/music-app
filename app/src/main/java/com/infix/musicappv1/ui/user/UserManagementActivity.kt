package com.infix.musicappv1.ui.user

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
import com.infix.musicappv1.data.model.user.User
import com.infix.musicappv1.databinding.ActivityUserManageBinding
import com.infix.musicappv1.ui.MainActivity
import com.infix.musicappv1.ui.auth.AuthViewModel
import com.infix.musicappv1.utils.ApiClient
import com.infix.musicappv1.utils.MusicAppUtils
import com.infix.musicappv1.utils.SnackbarUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserManagementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserManageBinding
    private var navController: NavController? = null

    private val authViewModel by viewModels<AuthViewModel>()
    private val userManagementViewMode: UserManagementViewModel by viewModels()

    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUserManageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //If extract user from intent succeeds
        if (extractUser()) {
            initDrawerAndNavController()
            setupToolbarMenu()
            setupOnLogout()
        }
    }

    private fun extractUser(): Boolean {
        val user = intent.getSerializableExtra(USER_KEY)
        if (user is User) {
            this.user = user
            userManagementViewMode.setUserState(this.user)
            return true
        }

        return false
    }

    private fun initDrawerAndNavController() {
        navController = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment_container_user_manage
        )?.findNavController()

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigate_home,
                R.id.navigate_profile,
                R.id.navigate_my_packages
            ),
            drawerLayout = binding.drawerLayout
        )

        if (navController == null) return

        binding.toolbar.setupWithNavController(navController!!, appBarConfiguration)
        binding.navigationView.setupWithNavController(navController!!)

        //check role
        if (user != null) {
            if (user!!.role != MusicAppUtils.ROLE_ADMIN)
                binding.navigationView.menu
                    .findItem(R.id.navigate_admin_panel).isVisible = false
        }
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

    private fun setupOnLogout() {
        ApiClient.setOnLogoutListener {
            handleOnLogout()
        }
    }

    private fun handleOnLogout() {
        authViewModel.logout()
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }

    companion object {
        const val USER_KEY = "com.infix.musicappv1.ui.user.UserManagementActivity.USER"
    }
}