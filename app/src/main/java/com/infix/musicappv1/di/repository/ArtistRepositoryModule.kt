package com.infix.musicappv1.di.repository

import com.infix.musicappv1.data.repository.artist.ArtistRepository
import com.infix.musicappv1.data.repository.artist.ArtistRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ArtistRepositoryModule {
    @Binds
    @ActivityRetainedScoped
    abstract fun getArtistRepository(impl: ArtistRepositoryImpl): ArtistRepository
}