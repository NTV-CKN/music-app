package com.infix.musicappv1.di.now_playing

import com.infix.musicappv1.ui.playing.now_playing.ProcessEventNowPlaying
import com.infix.musicappv1.ui.playing.now_playing.ProcessEventNowPlayingImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
abstract class ProcessEventNowPlayingModule {
    @Binds
    @ActivityScoped
    abstract fun getProcessEventNowPlaying(impl: ProcessEventNowPlayingImpl): ProcessEventNowPlaying
}