package com.infix.musicappv1.di.source.search.song

import com.infix.musicappv1.data.source.SearchKeySongDataSource
import com.infix.musicappv1.data.source.local.search.song.SearchKeySongLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class SearchKeySongDataSourceModule {
    @Binds
    @ActivityRetainedScoped
    abstract fun getSearchKeySongLocalDataSource(local: SearchKeySongLocalDataSource): SearchKeySongDataSource.Local
}