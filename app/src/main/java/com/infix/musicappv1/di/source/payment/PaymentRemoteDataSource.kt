package com.infix.musicappv1.di.source.payment

import com.infix.musicappv1.data.source.PaymentDataSource
import com.infix.musicappv1.data.source.remote.subscription.payment.SubscriptionPaymentRemoteDS
import com.infix.musicappv1.di.qualifier.SubscriptionPayment
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class PaymentRemoteDataSource {
    @Binds
    @ActivityRetainedScoped
    @SubscriptionPayment
    abstract fun bindSubscriptionPayment(
        subscriptionPaymentRemoteDS: SubscriptionPaymentRemoteDS
    ): PaymentDataSource.Remote
}