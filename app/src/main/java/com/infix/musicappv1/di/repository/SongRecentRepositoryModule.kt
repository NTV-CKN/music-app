package com.infix.musicappv1.di.repository

import com.infix.musicappv1.data.repository.song_recent.SongRecentRepository
import com.infix.musicappv1.data.repository.song_recent.SongRecentRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class SongRecentRepositoryModule {
    @Binds
    @ActivityRetainedScoped
    abstract fun bindSongRecentRepository(impl: SongRecentRepositoryImpl): SongRecentRepository
}