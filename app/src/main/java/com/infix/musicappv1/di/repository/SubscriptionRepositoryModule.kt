package com.infix.musicappv1.di.repository

import com.infix.musicappv1.data.repository.subscription.SubscriptionRepository
import com.infix.musicappv1.data.repository.subscription.SubscriptionRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class SubscriptionRepositoryModule {

    @Binds
    @ActivityRetainedScoped
    abstract fun bindSubscriptionRepository(
        impl: SubscriptionRepositoryImpl
    ): SubscriptionRepository
}
