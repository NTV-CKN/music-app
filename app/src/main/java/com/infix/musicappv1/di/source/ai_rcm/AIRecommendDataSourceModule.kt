package com.infix.musicappv1.di.source.ai_rcm

import com.infix.musicappv1.data.source.AIRecommendDataSource
import com.infix.musicappv1.data.source.remote.ai_rcm.AIRecommendRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class AIRecommendDataSourceModule {
    @Binds
    @ActivityRetainedScoped
    abstract fun bindAiRcmRemoteDataSource(
        remote: AIRecommendRemoteDataSource
    ): AIRecommendDataSource.Remote
}