package com.infix.musicappv1.di.repository

import com.infix.musicappv1.data.repository.search.song.SearchKeySongRepository
import com.infix.musicappv1.data.repository.search.song.SearchKeySongRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class SearchKeySongRepositoryModule {
    @Binds
    @ActivityRetainedScoped
    abstract fun getSearchKeySongRepository(impl: SearchKeySongRepositoryImpl): SearchKeySongRepository
}