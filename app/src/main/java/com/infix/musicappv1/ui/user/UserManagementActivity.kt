package com.infix.musicappv1.ui.user

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.user.User
import com.infix.musicappv1.databinding.ActivityUserManageBinding

class UserManagementActivity : AppCompatActivity() {
    val userKeyIntent = "com.infix.musicappv1.ui.user.UserManagementActivity.USER"

    private lateinit var binding: ActivityUserManageBinding
    private var navController: NavController? = null

    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUserManageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //If extract user from intent succeeds
        if (extractUser()) {
            initDrawerAndNavController()
        }
    }

    private fun extractUser(): Boolean {
        val user = intent.getSerializableExtra(userKeyIntent)
        if (user is User) {
            this.user = user
            return true
        }

        return false
    }

    private fun initDrawerAndNavController() {
        navController = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment_container_user_manage
        )?.findNavController()

        setSupportActionBar(binding.toolbar)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigate_home,
                R.id.navigate_profile,
                R.id.navigate_my_packages,
                R.id.navigate_admin_dashboard
            ),
            drawerLayout = binding.drawerLayout
        )

        if (navController == null) return

        binding.toolbar.setupWithNavController(navController!!, appBarConfiguration)
        binding.navigationView.setupWithNavController(navController!!)
    }
}