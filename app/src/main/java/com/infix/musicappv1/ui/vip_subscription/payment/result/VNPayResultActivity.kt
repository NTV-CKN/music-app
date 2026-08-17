package com.infix.musicappv1.ui.vip_subscription.payment.result

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import com.infix.musicappv1.R
import com.infix.musicappv1.databinding.ActivityVnpayResultBinding
import com.infix.musicappv1.ui.MainActivity
import com.infix.musicappv1.utils.FormatUnitUtils

class VNPayResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVnpayResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVnpayResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        handleVnpayIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleVnpayIntent(intent)
    }

    private fun setupListeners() {
        binding.btnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
            finish()
        }

        binding.btnRetry.setOnClickListener {
            finish()
        }
    }

    private fun handleVnpayIntent(intent: Intent?) {
        val appLinkData: Uri? = intent?.data

        if (appLinkData != null && appLinkData.scheme == "myapp" && appLinkData.host == "vnpay-return") {
            val responseCode = appLinkData.getQueryParameter("vnp_ResponseCode")
            val orderId = appLinkData.getQueryParameter("vnp_TxnRef") ?: "---"
            val rawAmount = appLinkData.getQueryParameter("vnp_Amount")
            val amountDouble = rawAmount?.toDoubleOrNull()?.div(100) ?: 0.0

            binding.tvOrderId.text = orderId
            binding.tvAmount.text = FormatUnitUtils.toVndFormatted(amountDouble)

            if (responseCode == "00") {
                onPaymentSuccess()
            } else {
                onPaymentFailure(responseCode)
            }
        }
    }

    private fun onPaymentSuccess() {
        binding.apply {
            ivStatusIcon.setImageResource(R.drawable.ic_crown)
            ivStatusIcon.setColorFilter("#4CAF50".toColorInt())

            tvStatusTitle.text = getString(R.string.txt_transaction_success)
            tvStatusTitle.setTextColor("#4CAF50".toColorInt())

            tvStatusMessage.text = getString(R.string.txt_description_after_transaction_success)
            btnRetry.visibility = View.GONE
        }
    }

    private fun onPaymentFailure(errorCode: String?) {
        val errorMessage = when (errorCode) {
            "24" -> getString(R.string.payment_error_cancelled)
            "11" -> getString(R.string.payment_error_expired)
            "51" -> getString(R.string.payment_error_insufficient_balance)
            else -> getString(R.string.payment_error_failed, errorCode)
        }


        binding.apply {
            ivStatusIcon.setImageResource(android.R.drawable.ic_delete)
            ivStatusIcon.setColorFilter("#F44336".toColorInt())

            tvStatusTitle.text = getString(R.string.txt_payment_failed)
            tvStatusTitle.setTextColor("#F44336".toColorInt())

            tvStatusMessage.text = errorMessage
            btnRetry.visibility = View.VISIBLE
        }
    }
}