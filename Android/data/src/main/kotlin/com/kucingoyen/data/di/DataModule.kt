package com.kucingoyen.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun provideOkhttpClientBuilder(
        @Named(TagInjection.IS_DEBUG_MODE) debugMode: Boolean,
        @Named(TagInjection.AUTH_INTERCEPTOR) authInterceptor: Interceptor,
        @Named(TagInjection.LOGGING_INTERCEPTOR) loggingInterceptor: Interceptor,
        @Named(TagInjection.CHUCKER_INTERCEPTOR) chuckerInterceptor: Interceptor
    ): OkHttpClient.Builder {

        val builder = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)

        if (debugMode) {
            builder.addInterceptor(loggingInterceptor)
            builder.addInterceptor(chuckerInterceptor)
        }

        builder.addInterceptor(authInterceptor)

        return builder
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        clientBuilder: OkHttpClient.Builder,
        @Named(TagInjection.BASE_URL) baseUrl: String,
        @Named(TagInjection.TOKEN_AUTHENTICATOR) authenticator: Authenticator
    ): Retrofit {
        clientBuilder.authenticator(authenticator)
        return Retrofit.Builder()
            .client(clientBuilder.build())
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

}