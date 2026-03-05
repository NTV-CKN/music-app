package com.infix.musicappv1.di.source.search.song

import com.infix.musicappv1.data.source.RecentSearchSongDataSource
import com.infix.musicappv1.data.source.local.search.song.RecentSearchSongLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class RecentSearchSongDataSourceModule {
    @Binds
    @ActivityRetainedScoped
    abstract fun getRecentSearchSongLocalDataSource(local: RecentSearchSongLocalDataSource): RecentSearchSongDataSource.Local
}