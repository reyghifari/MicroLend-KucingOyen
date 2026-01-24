package com.kucingoyen.data.di

import com.kucingoyen.data.service.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal class ServiceModule {

    @Singleton
    @Provides
    fun provideUserService(
        retrofit: Retrofit
    ): AuthService = retrofit.create(AuthService::class.java)

}