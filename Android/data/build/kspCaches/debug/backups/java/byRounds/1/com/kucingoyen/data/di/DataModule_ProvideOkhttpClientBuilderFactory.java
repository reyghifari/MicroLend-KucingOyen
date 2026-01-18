package com.kucingoyen.data.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

@ScopeMetadata
@QualifierMetadata("javax.inject.Named")
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class DataModule_ProvideOkhttpClientBuilderFactory implements Factory<OkHttpClient.Builder> {
  private final DataModule module;

  private final Provider<Boolean> debugModeProvider;

  private final Provider<Interceptor> authInterceptorProvider;

  private final Provider<Interceptor> loggingInterceptorProvider;

  private final Provider<Interceptor> chuckerInterceptorProvider;

  public DataModule_ProvideOkhttpClientBuilderFactory(DataModule module,
      Provider<Boolean> debugModeProvider, Provider<Interceptor> authInterceptorProvider,
      Provider<Interceptor> loggingInterceptorProvider,
      Provider<Interceptor> chuckerInterceptorProvider) {
    this.module = module;
    this.debugModeProvider = debugModeProvider;
    this.authInterceptorProvider = authInterceptorProvider;
    this.loggingInterceptorProvider = loggingInterceptorProvider;
    this.chuckerInterceptorProvider = chuckerInterceptorProvider;
  }

  @Override
  public OkHttpClient.Builder get() {
    return provideOkhttpClientBuilder(module, debugModeProvider.get(), authInterceptorProvider.get(), loggingInterceptorProvider.get(), chuckerInterceptorProvider.get());
  }

  public static DataModule_ProvideOkhttpClientBuilderFactory create(DataModule module,
      javax.inject.Provider<Boolean> debugModeProvider,
      javax.inject.Provider<Interceptor> authInterceptorProvider,
      javax.inject.Provider<Interceptor> loggingInterceptorProvider,
      javax.inject.Provider<Interceptor> chuckerInterceptorProvider) {
    return new DataModule_ProvideOkhttpClientBuilderFactory(module, Providers.asDaggerProvider(debugModeProvider), Providers.asDaggerProvider(authInterceptorProvider), Providers.asDaggerProvider(loggingInterceptorProvider), Providers.asDaggerProvider(chuckerInterceptorProvider));
  }

  public static DataModule_ProvideOkhttpClientBuilderFactory create(DataModule module,
      Provider<Boolean> debugModeProvider, Provider<Interceptor> authInterceptorProvider,
      Provider<Interceptor> loggingInterceptorProvider,
      Provider<Interceptor> chuckerInterceptorProvider) {
    return new DataModule_ProvideOkhttpClientBuilderFactory(module, debugModeProvider, authInterceptorProvider, loggingInterceptorProvider, chuckerInterceptorProvider);
  }

  public static OkHttpClient.Builder provideOkhttpClientBuilder(DataModule instance,
      boolean debugMode, Interceptor authInterceptor, Interceptor loggingInterceptor,
      Interceptor chuckerInterceptor) {
    return Preconditions.checkNotNullFromProvides(instance.provideOkhttpClientBuilder(debugMode, authInterceptor, loggingInterceptor, chuckerInterceptor));
  }
}
