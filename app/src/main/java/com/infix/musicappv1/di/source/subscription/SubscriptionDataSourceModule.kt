package com.infix.musicappv1.di.source.subscription

import com.infix.musicappv1.data.source.SubscriptionDataSource
import com.infix.musicappv1.data.source.remote.subscription.SubscriptionRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class SubscriptionDataSourceModule {

    @Binds
    @ActivityRetainedScoped
    abstract fun bindSubscriptionRemoteDataSource(
        impl: SubscriptionRemoteDataSource
    ): SubscriptionDataSource.Remote
}
