package com.infix.musicappv1.di.source.song

import com.infix.musicappv1.data.source.SongDataSource
import com.infix.musicappv1.data.source.local.song.SongLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class SongLocalDataSourceModule {
    @Binds
    @ActivityRetainedScoped
    abstract fun getSongLocalDataSource(local: SongLocalDataSource): SongDataSource.Local
}