package com.infix.musicappv1.ui.vip_subscription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.Subscription
import com.infix.musicappv1.databinding.FragmentSubscriptionBinding
import com.infix.musicappv1.ui.adapter.SubscriptionPagingDataAdapter
import com.infix.musicappv1.ui.admin.subscription.SubscriptionManagementViewModel
import com.infix.musicappv1.ui.auth.AuthViewModel
import com.infix.musicappv1.ui.dialog.LoadingDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SubscriptionFragment : Fragment() {

    private lateinit var binding: FragmentSubscriptionBinding
    private lateinit var adapter: SubscriptionPagingDataAdapter
    private lateinit var navController: NavController
    private lateinit var loadingDialogFragment: LoadingDialogFragment

    private val subscriptionManagementVM by viewModels<SubscriptionManagementViewModel>()
    private val authViewModel by viewModels<AuthViewModel>()
    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSubscriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        loadingDialogFragment = LoadingDialogFragment()

        initRecyclerView()
        observeViewModel()
        setupSearch()
        setupEvents()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchJob = null
    }

    private fun initRecyclerView() {
        adapter = SubscriptionPagingDataAdapter(
            isAdmin = false,
            onSubscriptionClick = { subscription ->
                navigateToCheckout(subscription)
            }
        )
        binding.rvSubscriptions.adapter = adapter
    }

    private fun observeViewModel() {
        //subscriptions
        subscriptionManagementVM.subscriptionPagingData
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { adapter.submitData(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        //loading subscriptionManagementVM
        subscriptionManagementVM.isLoading.observe(viewLifecycleOwner, ::handleLoadingState)
    }

    private fun handleLoadingState(isLoading: Boolean?) {
        if (isLoading == null) return

        try {
            if (isLoading) {
                loadingDialogFragment.show(requireActivity().supportFragmentManager, null)
            } else {
                loadingDialogFragment.dismissNow()
            }
        } catch (_: Exception) {
        }
    }

    private fun setupSearch() {
        binding.edtSearch.doOnTextChanged { text, _, _, _ ->
            val query = text?.toString()?.trim() ?: ""
            if (query == subscriptionManagementVM.currentQuery.value.query) return@doOnTextChanged

            searchJob?.cancel()
            searchJob = viewLifecycleOwner.lifecycleScope.launch {
                delay(400)
                subscriptionManagementVM.setSubscriptionPagingState(query)
            }
        }
    }

    private fun setupEvents() {
        binding.swipeRefresh.setOnRefreshListener {
            adapter.refresh()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun navigateToCheckout(subscription: Subscription) {
        val userSession = authViewModel.userSession.value
        if (userSession != null)
            findNavController().navigate(
                SubscriptionFragmentDirections.actionNavigateSubscriptionToNavigateSubscriptionPayment(
                    subscription
                )
            )
        else
            findNavController().navigate(
                R.id.navigation_user_profile
            )
    }
}