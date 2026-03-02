package com.infix.musicappv1.di.repository

import com.infix.musicappv1.data.repository.album.AlbumRepository
import com.infix.musicappv1.data.repository.album.AlbumRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class AlbumRepositoryModule {
    @Binds
    @ActivityRetainedScoped
    abstract fun bindAlbumRepository(impl: AlbumRepositoryImpl): AlbumRepository
}