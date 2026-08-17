package com.infix.musicappv1.ui.vip_subscription.payment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.infix.musicappv1.R
import com.infix.musicappv1.data.source.Result
import com.infix.musicappv1.databinding.FragmentSubscriptionPaymentBinding
import com.infix.musicappv1.ui.dialog.LoadingDialogFragment
import com.infix.musicappv1.utils.FormatUnitUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubscriptionPaymentFragment : Fragment() {

    private lateinit var binding: FragmentSubscriptionPaymentBinding
    private val subscriptionPaymentVM by viewModels<SubscriptionPaymentViewModel>()
    private val args by navArgs<SubscriptionPaymentFragmentArgs>()

    private lateinit var loadingDialogFragment: LoadingDialogFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSubscriptionPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialogFragment = LoadingDialogFragment()

        setupUI()
        observeViewModel()

        subscriptionPaymentVM.fetchUserVipExpiry()
    }

    private fun setupUI() {
        val subscription = args.subscriptionInfo

        binding.apply {
            tvPlanName.text = subscription.name
            tvPlanPrice.text = FormatUnitUtils.toVndFormatted(subscription.price)
            tvPlanDuration.text = getString(R.string.txt_duration_days_args, subscription.durationDays.toString())

            btnConfirmPayment.setOnClickListener {
//                subscriptionPaymentVM.createPaymentUrl(subscription.id)
            }
        }
    }

    private fun observeViewModel() {
        //vipExpiryDate
        subscriptionPaymentVM.userVipExpiryMs.observe(viewLifecycleOwner) { currentExpiryMs ->
            val (currentStr, newStr) = subscriptionPaymentVM.calculateExpiryDates(
                args.subscriptionInfo.durationDays,
                currentExpiryMs
            )
            binding.tvCurrentExpiry.text = currentStr
            binding.tvNewExpiry.text = newStr
        }

        //show loading
        subscriptionPaymentVM.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading == true) {
                loadingDialogFragment.show(requireActivity().supportFragmentManager, null)
            } else {
                try {
                    loadingDialogFragment.dismissNow()
                } catch (_: Exception) {
                }
            }
        }

        //result paymentUrl
        subscriptionPaymentVM.paymentUrlResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    openVNPayCustomTab(result.data)
                }

                is Result.Error -> {
                    Toast.makeText(
                        requireContext(),
                        result.err.message ?: "Khởi tạo thanh toán thất bại",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    /**
     * Open URL VNPay by Chrome Custom Tabs
     */
    private fun openVNPayCustomTab(url: String) {
        val customTabsIntent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .build()

        customTabsIntent.launchUrl(requireContext(), Uri.parse(url))
    }
}