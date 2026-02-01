package com.kucingoyen.dashboard.di

import com.kucingoyen.dashboard.repository.DashboardRepository
import com.kucingoyen.dashboard.repository.DashboardRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class DashboardModule {

    @Binds
    @Singleton
    internal abstract fun provideDashboardRepository(impl: DashboardRepositoryImpl): DashboardRepository

}
