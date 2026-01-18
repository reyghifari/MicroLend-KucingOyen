package com.kucingoyen.microlend.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.kucingoyen.data.di.TagInjection
import com.kucingoyen.microlend.BuildConfig
import com.kucingoyen.microlend.interceptor.AuthInterceptor
import com.kucingoyen.microlend.interceptor.TokenAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Named


@Module
@InstallIn(SingletonComponent::class)
internal class NetworkModule {

    @Provides
    @Named(TagInjection.BASE_URL)
    fun provideBaseUrl(): String = BuildConfig.BASE_URL

    @Provides
    @Named(TagInjection.LOGGING_INTERCEPTOR)
    fun provideHttpLoggingInterceptor(
        @Named(TagInjection.IS_DEBUG_MODE) debugMode: Boolean,
    ): Interceptor = if (debugMode) HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    } else Interceptor { chain -> chain.proceed(chain.request()) }

    @Provides
    @Named(TagInjection.CHUCKER_INTERCEPTOR)
    fun provideChuckerInterceptor(
        @Named(TagInjection.IS_DEBUG_MODE) debugMode: Boolean,
        @ApplicationContext context: Context,
    ): Interceptor = if (debugMode) ChuckerInterceptor.Builder(context).build()
    else Interceptor { chain -> chain.proceed(chain.request()) }

    @Provides
    @Named(TagInjection.TOKEN_AUTHENTICATOR)
    fun provideTokenAuthenticator(impl: TokenAuthenticator): Authenticator = impl

    @Provides
    @Named(TagInjection.AUTH_INTERCEPTOR)
    fun provideAuthInterceptor(impl: AuthInterceptor): Interceptor = impl


}