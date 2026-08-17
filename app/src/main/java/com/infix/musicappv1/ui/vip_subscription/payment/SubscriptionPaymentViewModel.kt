package com.infix.musicappv1.ui.vip_subscription.payment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.infix.musicappv1.R
import com.infix.musicappv1.data.repository.user.IUserRepository
import com.infix.musicappv1.data.source.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SubscriptionPaymentViewModel @Inject constructor(
    private val userRepository: IUserRepository,
    private val application: Application,
//    private val paymentRepository: IPaymentRepository
) : AndroidViewModel(application) {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _paymentUrlResult = MutableLiveData<Result<String>>()
    val paymentUrlResult: LiveData<Result<String>> = _paymentUrlResult

    private val _userVipExpiryMs = MutableLiveData<Long?>()
    val userVipExpiryMs: LiveData<Long?> = _userVipExpiryMs

    fun fetchUserVipExpiry() {
        viewModelScope.launch {
            val expiryMs = userRepository.getCurrentUserVipExpiry()
            _userVipExpiryMs.value = expiryMs
        }
    }

    fun calculateExpiryDates(durationDays: Int, currentExpiryMs: Long?): Pair<String, String> {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val nowMs = System.currentTimeMillis()

        val currentExpiryStr = if (currentExpiryMs != null && currentExpiryMs > nowMs) {
            dateFormat.format(currentExpiryMs)
        } else {
           application.applicationContext.getString(
               R.string.txt_none_expired
           )
        }

        val baseTimeMs = maxOf(nowMs, currentExpiryMs ?: 0L)
        val calendar = Calendar.getInstance().apply {
            timeInMillis = baseTimeMs
            add(Calendar.DAY_OF_YEAR, durationDays)
        }

        val newExpiryStr = dateFormat.format(calendar.timeInMillis)
        return Pair(currentExpiryStr, newExpiryStr)
    }

//    fun createPaymentUrl(packageId: String) {
//        _isLoading.value = true
//        viewModelScope.launch(Dispatchers.IO) {
//            val result = paymentRepository.createPaymentUrl(packageId)
//            withContext(Dispatchers.Main) {
//                _paymentUrlResult.value = result
//                _isLoading.value = false
//            }
//        }
//    }
}