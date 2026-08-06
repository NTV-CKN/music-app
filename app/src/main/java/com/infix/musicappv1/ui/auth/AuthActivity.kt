package com.infix.musicappv1.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.user.User
import com.infix.musicappv1.databinding.ActivityAuthBinding
import com.infix.musicappv1.ui.dialog.LoadingDialogFragment
import com.infix.musicappv1.ui.user.UserManagementActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    private var navController: NavController? = null
    private lateinit var binding: ActivityAuthBinding
    private var loadingDialogFragment: LoadingDialogFragment? = null

    //Initially, the user in AuthViewModel is null, so we track and load it from room
    private var isCallLoadUser = false

    private val authViewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        handleLoadingDialog(true)

        navController = supportFragmentManager.findFragmentById(R.id.fragment_host_container_auth)
            ?.findNavController()
        observeAuthVM()
    }

    private fun observeAuthVM() {
        authViewModel.userSession
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach(::handleUserSession)
            .launchIn(lifecycleScope)

        authViewModel.isLoading
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach(::handleLoadingDialog)
            .launchIn(lifecycleScope)
    }

    private fun handleLoadingDialog(isLoading: Boolean) {
        if(loadingDialogFragment == null)
            loadingDialogFragment = LoadingDialogFragment()

        try {
            if (isLoading)
                loadingDialogFragment?.showNow(supportFragmentManager, null)
            else
                loadingDialogFragment?.dismiss()
        } catch (_: Exception) {
        }
    }

    private fun handleUserSession(user: User?) {
        if (!isCallLoadUser) {
            authViewModel.loadUserSession()
            isCallLoadUser = true
            return
        }

        if (user != null) {
            finish()
            handleLoadingDialog(false)
            val intent = Intent(this, UserManagementActivity::class.java)
            intent.apply {
                putExtra(UserManagementActivity.USER_KEY, user)
            }
            startActivity(intent)
        }
    }
}