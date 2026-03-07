package com.infix.musicappv1.ui.base

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.infix.musicappv1.data.repository.NetworkRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkCallback @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val networkRepository: NetworkRepository
) : ConnectivityManager.NetworkCallback() {
    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        val hasInternet =
            networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

        networkRepository.setHasNetwork(hasInternet)
    }

    //when network change capabilities, we guarantee this network must have Internet
    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities)
        val hasInternet =
            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

        networkRepository.setHasNetwork(hasInternet)
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        networkRepository.setHasNetwork(false)
    }
}