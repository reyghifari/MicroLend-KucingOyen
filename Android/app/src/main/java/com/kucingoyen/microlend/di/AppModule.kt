package com.kucingoyen.microlend.di

import android.content.Context
import android.content.SharedPreferences
import com.kucingoyen.core.utils.DispatcherProvider
import com.kucingoyen.data.di.TagInjection
import com.kucingoyen.microlend.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

	@Singleton
	@Provides
	fun provideSharedPref(@ApplicationContext context: Context): SharedPreferences {
		return context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
	}

	@Provides
	@Named(TagInjection.IS_DEBUG_MODE)
	fun provideDebugMode(): Boolean = BuildConfig.DEBUG

	@Provides
	fun provideResources(@ApplicationContext context: Context) = context.resources

	@Provides
	fun provideDispatcherProvider(): DispatcherProvider = object : DispatcherProvider {
		override val main: CoroutineDispatcher get() = Dispatchers.Main
		override val io: CoroutineDispatcher get() = Dispatchers.IO
	}

	@Provides
	@Named(TagInjection.APPLICATION_CONTEXT)
	fun provideApplicationContext(@ApplicationContext context: Context): Context = context

	@Provides
	@Named(TagInjection.APP_VERSION_CODE)
	fun provideAppVersionCode(): Int = BuildConfig.VERSION_CODE

	@Provides
	@Named(TagInjection.APPLICATION_ID)
	fun provideApplicationID(): String = BuildConfig.APPLICATION_ID

	@Provides
	@Named(TagInjection.APP_VERSION_NAME)
	fun provideAppVersionName(): String = BuildConfig.VERSION_NAME
}
