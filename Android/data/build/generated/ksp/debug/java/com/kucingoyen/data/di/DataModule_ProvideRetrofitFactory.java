package com.kucingoyen.data.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import okhttp3.Authenticator;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

@ScopeMetadata("javax.inject.Singleton")
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
public final class DataModule_ProvideRetrofitFactory implements Factory<Retrofit> {
  private final DataModule module;

  private final Provider<OkHttpClient.Builder> clientBuilderProvider;

  private final Provider<String> baseUrlProvider;

  private final Provider<Authenticator> authenticatorProvider;

  public DataModule_ProvideRetrofitFactory(DataModule module,
      Provider<OkHttpClient.Builder> clientBuilderProvider, Provider<String> baseUrlProvider,
      Provider<Authenticator> authenticatorProvider) {
    this.module = module;
    this.clientBuilderProvider = clientBuilderProvider;
    this.baseUrlProvider = baseUrlProvider;
    this.authenticatorProvider = authenticatorProvider;
  }

  @Override
  public Retrofit get() {
    return provideRetrofit(module, clientBuilderProvider.get(), baseUrlProvider.get(), authenticatorProvider.get());
  }

  public static DataModule_ProvideRetrofitFactory create(DataModule module,
      javax.inject.Provider<OkHttpClient.Builder> clientBuilderProvider,
      javax.inject.Provider<String> baseUrlProvider,
      javax.inject.Provider<Authenticator> authenticatorProvider) {
    return new DataModule_ProvideRetrofitFactory(module, Providers.asDaggerProvider(clientBuilderProvider), Providers.asDaggerProvider(baseUrlProvider), Providers.asDaggerProvider(authenticatorProvider));
  }

  public static DataModule_ProvideRetrofitFactory create(DataModule module,
      Provider<OkHttpClient.Builder> clientBuilderProvider, Provider<String> baseUrlProvider,
      Provider<Authenticator> authenticatorProvider) {
    return new DataModule_ProvideRetrofitFactory(module, clientBuilderProvider, baseUrlProvider, authenticatorProvider);
  }

  public static Retrofit provideRetrofit(DataModule instance, OkHttpClient.Builder clientBuilder,
      String baseUrl, Authenticator authenticator) {
    return Preconditions.checkNotNullFromProvides(instance.provideRetrofit(clientBuilder, baseUrl, authenticator));
  }
}
