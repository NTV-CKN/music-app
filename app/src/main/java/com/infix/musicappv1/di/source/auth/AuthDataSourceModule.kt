package com.infix.musicappv1.di.source.auth

import com.infix.musicappv1.data.source.AuthDataSource
import com.infix.musicappv1.data.source.remote.auth.AuthRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped


@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class AuthDataSourceModule {
    @Binds
    @ActivityRetainedScoped
    abstract fun bindAuthRemoteDataSource(authRemote: AuthRemoteDataSourceImpl): AuthDataSource.Remote
}