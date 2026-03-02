package com.infix.musicappv1.di.source.song

import com.infix.musicappv1.data.source.SongRecentDataSource
import com.infix.musicappv1.data.source.local.recent.SongRecentLocalSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class SongRecentLocalDataSourceModule {
    @Binds
    @ActivityRetainedScoped
    abstract fun getSongRecentLocalDataSource(local: SongRecentLocalSource): SongRecentDataSource.Local
}