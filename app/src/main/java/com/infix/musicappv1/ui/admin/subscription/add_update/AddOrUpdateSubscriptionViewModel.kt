package com.infix.musicappv1.ui.admin.subscription.add_update

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infix.musicappv1.data.dto.BaseResultResponse
import com.infix.musicappv1.data.model.Subscription
import com.infix.musicappv1.data.repository.subscription.SubscriptionRepository
import com.infix.musicappv1.data.source.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddOrUpdateSubscriptionViewModel @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    data class AddOrUpdateSubscriptionParams(
        val isUpdate: Boolean,
        val subscription: Subscription = Subscription(),
        val current: Long = System.currentTimeMillis()
    )

    sealed class ValidationError {
        object EmptyName : ValidationError()
        object EmptyDescription : ValidationError()
        object InvalidPrice : ValidationError()
        object InvalidDuration : ValidationError()
    }

    private val _params = MutableStateFlow<AddOrUpdateSubscriptionParams?>(null)
    val params = _params.asStateFlow()

    fun setIsUpdateSubscriptionState(params: AddOrUpdateSubscriptionParams?) {
        _params.value = params
    }

    fun validateSubscription(): ValidationError? {
        val subscription = _params.value?.subscription ?: return ValidationError.EmptyName

        return when {
            subscription.name.isBlank() -> ValidationError.EmptyName
            subscription.description.isBlank() -> ValidationError.EmptyDescription
            subscription.price < 0.0 -> ValidationError.InvalidPrice
            subscription.durationDays <= 0 -> ValidationError.InvalidDuration
            else -> null
        }
    }

    fun saveSubscription(callback: (baseResult: BaseResultResponse) -> Unit) {
        if (validateSubscription() != null) return

        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            val subscription = params.value!!.subscription
            val params = params.value ?: return@launch
            val response = if (params.isUpdate) {
                subscriptionRepository.updateSubscription(subscription)
            } else {
                subscriptionRepository.saveSubscription(subscription)
            }

            withContext(Dispatchers.Main) {
                _isLoading.value = false

                if (response is Result.Success) {
                    callback.invoke(response.data)
                } else if (response is Result.Error) {
                    callback.invoke(
                        BaseResultResponse(
                            success = false,
                            message = response.err.message ?: "Unknown error"
                        )
                    )
                }
            }
        }
    }
}