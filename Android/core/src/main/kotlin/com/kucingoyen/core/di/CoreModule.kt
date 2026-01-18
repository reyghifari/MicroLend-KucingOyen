package com.kucingoyen.core.di

import com.kucingoyen.core.utils.ErrorParser
import com.kucingoyen.core.utils.ErrorParserImpl
import com.kucingoyen.core.utils.RemoteConfig
import com.kucingoyen.core.utils.RemoteConfigImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CoreModule {

    @Binds
    @Singleton
    internal abstract fun provideErrorParser(impl: ErrorParserImpl): ErrorParser

    @Binds
    @Singleton
    internal abstract fun provideRemoteConfigUtil(impl: RemoteConfigImpl): RemoteConfig

}
