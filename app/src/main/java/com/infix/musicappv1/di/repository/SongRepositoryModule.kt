package com.infix.musicappv1.di.repository

import com.infix.musicappv1.data.repository.song.SongRepository
import com.infix.musicappv1.data.repository.song.SongRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class SongRepositoryModule {
    @Binds
    @ActivityRetainedScoped
    abstract fun getSongRepository(impl: SongRepositoryImpl): SongRepository
}