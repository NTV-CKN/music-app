package com.infix.musicappv1.ui.admin.subscription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.Subscription
import com.infix.musicappv1.databinding.FragmentSubscriptionManagementBinding
import com.infix.musicappv1.ui.adapter.SubscriptionPagingDataAdapter
import com.infix.musicappv1.ui.admin.subscription.add_update.AddOrUpdateSubscriptionViewModel
import com.infix.musicappv1.ui.dialog.CRUDOptionDialog
import com.infix.musicappv1.ui.dialog.LoadingDialogFragment
import com.infix.musicappv1.utils.SnackbarUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SubscriptionManagementFragment : Fragment() {

    private lateinit var binding: FragmentSubscriptionManagementBinding
    private lateinit var adapter: SubscriptionPagingDataAdapter
    private lateinit var crudOptionDialog: CRUDOptionDialog<Subscription>
    private lateinit var navController: NavController
    private lateinit var loadingDialogFragment: LoadingDialogFragment

    private val addOrUpdateSubscriptionViewModel by activityViewModels<AddOrUpdateSubscriptionViewModel>()
    private val viewModel by viewModels<SubscriptionManagementViewModel>()

    private var searchJob: Job? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSubscriptionManagementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        loadingDialogFragment = LoadingDialogFragment()

        initCrudOptDialog()
        observeViewModel()
        initRecyclerView()
        setupSearch()
        setupEvents()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchJob = null
    }

    private fun initCrudOptDialog() {
        crudOptionDialog = CRUDOptionDialog(
            onUpdate = { subscription ->
                addOrUpdateSubscriptionViewModel.setIsUpdateSubscriptionState(
                    AddOrUpdateSubscriptionViewModel.AddOrUpdateSubscriptionParams(
                        isUpdate = true,
                        subscription.clone()
                    )
                )

                findNavController().navigate(
                    SubscriptionManagementFragmentDirections.actionNavigateManageVipPackagesToNavigateAddOrUpdateSubscription(
                        R.string.txt_update_subscription
                    )
                )
            },
            onView = { subscription -> },
            onDelete = ::handleRemoveSubscription
        )
    }

    private fun observeViewModel() {
        viewModel.subscriptionPagingData
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { adapter.submitData(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading == null) return@observe
            try {
                if (isLoading)
                    loadingDialogFragment.show(requireActivity().supportFragmentManager, null)
                else
                    loadingDialogFragment.dismissNow()
            } catch (_: Exception) {
            }
        }
    }

    private fun initRecyclerView() {
        adapter = SubscriptionPagingDataAdapter(
            isAdmin = true,
            onSubscriptionClick = { subscription -> },
            onOptionClick = { subscription -> showCRUDDialog(subscription) }
        )

        binding.rvSubscriptions.adapter = adapter
    }

    private fun showCRUDDialog(subscription: Subscription) {
        crudOptionDialog.setData(subscription, isVisibleDelete = false)
        crudOptionDialog.show(requireActivity().supportFragmentManager, null)
    }

    private fun setupSearch() {
        binding.edtSearch.doOnTextChanged { text, _, _, _ ->
            searchJob?.cancel()
            searchJob = viewLifecycleOwner.lifecycleScope.launch {
                delay(400)
                val query = text?.toString() ?: ""
                viewModel.setSubscriptionPagingState(query)
            }
        }
    }

    private fun setupEvents() {
        //fab
        binding.fabAddSubscription.setOnClickListener {
            addOrUpdateSubscriptionViewModel.setIsUpdateSubscriptionState(
                AddOrUpdateSubscriptionViewModel.AddOrUpdateSubscriptionParams(
                    isUpdate = false
                )
            )

            findNavController().navigate(
                SubscriptionManagementFragmentDirections.actionNavigateManageVipPackagesToNavigateAddOrUpdateSubscription(
                    R.string.txt_add_subscription
                )
            )
        }

        //swipe
        binding.swipeRefresh.setOnRefreshListener {
            adapter.refresh()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun handleRemoveSubscription(subscription: Subscription) {
        val callback: (success: Boolean, msg: String) -> Unit = { _, msg ->
            SnackbarUtils.showBaseSnackbar(binding.root, msg, Snackbar.LENGTH_SHORT)
        }

        SnackbarUtils.showSnackbarWithAction(
            binding.root,
            getString(R.string.txt_confirm_clear_all_data),
            "Ok",
            Snackbar.LENGTH_LONG
        ) {
            viewModel.removeSubscription(subscription, callback)
        }
    }
}