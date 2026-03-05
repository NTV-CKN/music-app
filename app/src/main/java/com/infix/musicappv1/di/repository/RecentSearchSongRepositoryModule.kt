package com.infix.musicappv1.di.repository

import com.infix.musicappv1.data.repository.search.song.RecentSearchSongRepository
import com.infix.musicappv1.data.repository.search.song.RecentSearchSongRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class RecentSearchSongRepositoryModule {
    @Binds
    @ActivityRetainedScoped
    abstract fun getRecentSearchSongRepository(impl: RecentSearchSongRepositoryImpl): RecentSearchSongRepository
}