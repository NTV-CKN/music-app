package com.infix.musicappv1.ui.admin.subscription.add_update

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.infix.musicappv1.R
import com.infix.musicappv1.data.model.Subscription
import com.infix.musicappv1.databinding.FragmentAddOrUpdateSubscriptionBinding
import com.infix.musicappv1.ui.dialog.LoadingDialogFragment
import com.infix.musicappv1.utils.SnackbarUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class AddOrUpdateSubscriptionFragment : Fragment() {

    private lateinit var binding: FragmentAddOrUpdateSubscriptionBinding
    private val addOrUpdateSubscriptionViewModel by activityViewModels<AddOrUpdateSubscriptionViewModel>()

    private lateinit var loadingDialogFragment: LoadingDialogFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddOrUpdateSubscriptionBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialogFragment = LoadingDialogFragment()

        observeAddOrUpdateVM()
        setupEvents()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        addOrUpdateSubscriptionViewModel.setIsUpdateSubscriptionState(null)
    }

    private fun observeAddOrUpdateVM() {
        // Subscription and Option update
        addOrUpdateSubscriptionViewModel.params
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { params ->
                if (params != null)
                    handleAddOrUpdateParams(params)
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        // Is loading
        addOrUpdateSubscriptionViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
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

    private fun setupEvents() {
        // Name
        binding.edtSubscriptionName.doOnTextChanged { text, _, _, _ ->
            addOrUpdateSubscriptionViewModel.params.value?.subscription?.name = text.toString()
        }

        // Description
        binding.edtSubscriptionDescription.doOnTextChanged { text, _, _, _ ->
            addOrUpdateSubscriptionViewModel.params.value?.subscription?.description =
                text.toString()
        }

        // Price
        binding.edtSubscriptionPrice.doOnTextChanged { text, _, _, _ ->
            val price = text.toString().toDoubleOrNull() ?: 0.0
            addOrUpdateSubscriptionViewModel.params.value?.subscription?.price = price
        }

        // Duration Days
        binding.edtSubscriptionDuration.doOnTextChanged { text, _, _, _ ->
            val duration = text.toString().toIntOrNull() ?: 0
            addOrUpdateSubscriptionViewModel.params.value?.subscription?.durationDays = duration
        }

        // Switch Active Status
        binding.switchIsActive.setOnCheckedChangeListener { _, isChecked ->
            addOrUpdateSubscriptionViewModel.params.value?.subscription?.isActive = isChecked
        }

        // Save Subscription
        handleSaveSubscription()
    }

    private fun handleAddOrUpdateParams(
        params: AddOrUpdateSubscriptionViewModel.AddOrUpdateSubscriptionParams
    ) {
        bindSubscriptionData(params.subscription)
    }

    private fun handleSaveSubscription() {
        binding.btnSave.setOnClickListener {
            val error = addOrUpdateSubscriptionViewModel.validateSubscription()
            if (error != null) {
                handleValidationError(error)
                return@setOnClickListener
            }

            addOrUpdateSubscriptionViewModel.saveSubscription { resultResponse ->
                Toast.makeText(
                    requireContext(),
                    resultResponse.message,
                    Toast.LENGTH_SHORT
                ).show()

                if (resultResponse.success) {
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun handleValidationError(error: AddOrUpdateSubscriptionViewModel.ValidationError) {
        when (error) {
            is AddOrUpdateSubscriptionViewModel.ValidationError.EmptyName -> {
                val message = getString(R.string.txt_name_subscription)
                binding.edtSubscriptionName.error = message
                binding.edtSubscriptionName.requestFocus()
                SnackbarUtils.showBaseSnackbar(
                    binding.root,
                    message,
                    Snackbar.LENGTH_SHORT
                )
            }

            is AddOrUpdateSubscriptionViewModel.ValidationError.EmptyDescription -> {
                val message = getString(R.string.txt_description_subscription)
                binding.edtSubscriptionDescription.error = message
                binding.edtSubscriptionDescription.requestFocus()
                SnackbarUtils.showBaseSnackbar(
                    binding.root,
                    message,
                    Snackbar.LENGTH_SHORT
                )
            }

            is AddOrUpdateSubscriptionViewModel.ValidationError.InvalidPrice -> {
                val message = getString(R.string.txt_price_subscription)
                binding.edtSubscriptionPrice.error = message
                binding.edtSubscriptionPrice.requestFocus()
                SnackbarUtils.showBaseSnackbar(
                    binding.root,
                    message,
                    Snackbar.LENGTH_SHORT
                )
            }

            is AddOrUpdateSubscriptionViewModel.ValidationError.InvalidDuration -> {
                val message = getString(R.string.txt_duration_subscription)
                binding.edtSubscriptionDuration.error = message
                binding.edtSubscriptionDuration.requestFocus()
                SnackbarUtils.showBaseSnackbar(
                    binding.root,
                    message,
                    Snackbar.LENGTH_SHORT
                )
            }
        }
    }

    private fun bindSubscriptionData(subscription: Subscription) {
        binding.edtSubscriptionName.setText(subscription.name)
        binding.edtSubscriptionDescription.setText(subscription.description)
        binding.edtSubscriptionPrice.setText(
            if (subscription.price > 0.0) subscription.price.toString() else ""
        )
        binding.edtSubscriptionDuration.setText(
            if (subscription.durationDays > 0) subscription.durationDays.toString() else ""
        )
        binding.switchIsActive.isChecked = subscription.isActive
    }
}