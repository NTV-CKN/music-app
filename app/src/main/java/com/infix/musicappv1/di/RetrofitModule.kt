package com.infix.musicappv1.di

import com.infix.musicappv1.data.source.remote.MusicService
import com.infix.musicappv1.data.source.remote.user.UserService
import com.infix.musicappv1.utils.ApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import retrofit2.Retrofit

@Module
@InstallIn(ActivityRetainedComponent::class)
object RetrofitModule {
    @Provides
    fun getRetrofit(): Retrofit = ApiClient.getRetrofitClient()

    @Provides
    fun getUserService(retrofit: Retrofit): UserService = retrofit.create(UserService::class.java)

    @Provides
    fun getMusicService(retrofit: Retrofit): MusicService = retrofit.create(MusicService::class.java)
}