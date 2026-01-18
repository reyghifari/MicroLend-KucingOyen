package com.kucingoyen.auth.di



import com.kucingoyen.auth.repository.AuthRepository
import com.kucingoyen.auth.repository.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    @Singleton
    internal abstract fun provideUseRepository(impl: AuthRepositoryImpl): AuthRepository

}
