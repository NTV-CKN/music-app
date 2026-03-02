package com.infix.musicappv1.di.source.playlist

import com.infix.musicappv1.data.source.PlaylistDataSource
import com.infix.musicappv1.data.source.local.playlist.PlaylistLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class PlaylistLocalDataSourceModule {
    @Binds
    @ActivityRetainedScoped
    abstract fun getPlaylistLocalDataSource(local: PlaylistLocalDataSource): PlaylistDataSource.Local
}