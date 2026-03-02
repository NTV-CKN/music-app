package com.infix.musicappv1.di.repository

import com.infix.musicappv1.data.repository.playlist.PlaylistRepository
import com.infix.musicappv1.data.repository.playlist.PlaylistRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class PlaylistRepositoryModule {
    @Binds
    @ActivityRetainedScoped
    abstract fun bindPlaylistRepository(impl: PlaylistRepositoryImpl): PlaylistRepository
}