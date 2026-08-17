package com.infix.musicappv1.di.repository

import com.infix.musicappv1.data.repository.user.IUserRepository
import com.infix.musicappv1.data.repository.user.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class UserRepositoryModule {
    @Binds
    @ActivityRetainedScoped
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): IUserRepository
}