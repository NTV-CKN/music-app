package com.infix.musicappv1.di.source.user

import com.infix.musicappv1.data.source.UserDataSource
import com.infix.musicappv1.data.source.local.user.UserLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class UserLocalDataSource {
    @Binds
    @ActivityRetainedScoped
    abstract fun bindUserLocalDataSource(userLocalDataSource: UserLocalDataSourceImpl): UserDataSource.Local
}