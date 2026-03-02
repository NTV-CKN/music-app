package com.infix.musicappv1.di.source.artist

import com.infix.musicappv1.data.source.ArtistDataSource
import com.infix.musicappv1.data.source.local.artist.ArtistLocalDataSource
import com.infix.musicappv1.data.source.remote.artist.ArtistRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ArtistDataSourceModule {
    @Binds
    @ActivityRetainedScoped
    abstract fun getArtistLocalDataSource(local: ArtistLocalDataSource): ArtistDataSource.Local

    @Binds
    @ActivityRetainedScoped
    abstract fun getArtistRemoteDataSource(remote: ArtistRemoteDataSource): ArtistDataSource.Remote
}