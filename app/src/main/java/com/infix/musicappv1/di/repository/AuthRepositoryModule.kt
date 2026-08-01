package com.infix.musicappv1.di.repository

import com.infix.musicappv1.data.repository.auth.AuthRepository
import com.infix.musicappv1.data.repository.auth.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class AuthRepositoryModule {
    @Binds
    @ActivityRetainedScoped
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository
}