package com.infix.musicappv1.di.source.album

import com.infix.musicappv1.data.source.AlbumDataSource
import com.infix.musicappv1.data.source.local.album.AlbumLocalDataSource
import com.infix.musicappv1.data.source.remote.album.AlbumRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class AlbumDataSourceModule {
    @Binds
    @ActivityRetainedScoped
    abstract fun getAlbumLocalDataSource(local: AlbumLocalDataSource): AlbumDataSource.Local

    @Binds
    @ActivityRetainedScoped
    abstract fun getAlbumRemoteDataSource(remote: AlbumRemoteDataSource): AlbumDataSource.Remote
}