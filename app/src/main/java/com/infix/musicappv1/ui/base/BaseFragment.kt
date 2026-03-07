package com.infix.musicappv1.ui.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.infix.musicappv1.R
import com.infix.musicappv1.data.repository.NetworkRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
open class BaseFragment : Fragment() {
    @Inject
    lateinit var networkRepository: NetworkRepository

    //avoid multi fragment extend this class will duplicate Toast
    private var trackPrevNetwork: Boolean? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                networkRepository.hasNetwork.collectLatest { isNetwork ->
                    if (isNetwork != null) {
                        if (isNetwork == trackPrevNetwork) return@collectLatest
                        if (!isNetwork) showToastWhenNotInternet()
                        trackPrevNetwork = isNetwork
                    }
                }
            }
        }
    }

    protected fun showToastWhenNotInternet() {
        Toast.makeText(
            requireContext(),
            requireContext().getString(R.string.txt_notify_when_lost_internet),
            Toast.LENGTH_SHORT
        ).show()
    }

    protected fun checkNetwork(): Boolean {
        val isNetwork = networkRepository.hasNetwork.value ?: false
        if (!isNetwork) {
            showToastWhenNotInternet()
            return false
        }
        return true
    }
}