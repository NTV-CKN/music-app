package com.infix.musicappv1.di.repository

import com.infix.musicappv1.data.repository.ai_rcm.AIRecommendRepositoryImpl
import com.infix.musicappv1.data.repository.ai_rcm.IAIRecommendRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class AIRecommendRepositoryModule {
    @Binds
    @ActivityRetainedScoped
    abstract fun bindAIRecommendRepository(
        aiRecommendRepositoryImpl: AIRecommendRepositoryImpl
    ): IAIRecommendRepository
}