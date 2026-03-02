package com.infix.musicappv1.di.source.artist

import com.infix.musicappv1.data.source.ArtistDataSource
import com.infix.musicappv1.data.source.local.artist.ArtistLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ArtistLocalDataSourceModule {
    @Binds
    @ActivityRetainedScoped
    abstract fun getArtistLocalDataSource(local: ArtistLocalDataSource): ArtistDataSource.Local
}