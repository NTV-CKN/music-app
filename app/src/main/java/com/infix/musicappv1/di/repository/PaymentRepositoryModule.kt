package com.infix.musicappv1.di.repository

import com.infix.musicappv1.data.repository.subscription.payment.ISubscriptionPaymentRepository
import com.infix.musicappv1.data.repository.subscription.payment.SubscriptionPaymentRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class PaymentRepositoryModule {
    @Binds
    @ActivityRetainedScoped
    abstract fun bindPaymentRepository(paymentRepositoryImpl: SubscriptionPaymentRepositoryImpl): ISubscriptionPaymentRepository
}