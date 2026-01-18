package com.kucingoyen.data.di;

import com.kucingoyen.data.service.UserService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import retrofit2.Retrofit;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
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
public final class ServiceModule_ProvideUserServiceFactory implements Factory<UserService> {
  private final ServiceModule module;

  private final Provider<Retrofit> retrofitProvider;

  public ServiceModule_ProvideUserServiceFactory(ServiceModule module,
      Provider<Retrofit> retrofitProvider) {
    this.module = module;
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public UserService get() {
    return provideUserService(module, retrofitProvider.get());
  }

  public static ServiceModule_ProvideUserServiceFactory create(ServiceModule module,
      javax.inject.Provider<Retrofit> retrofitProvider) {
    return new ServiceModule_ProvideUserServiceFactory(module, Providers.asDaggerProvider(retrofitProvider));
  }

  public static ServiceModule_ProvideUserServiceFactory create(ServiceModule module,
      Provider<Retrofit> retrofitProvider) {
    return new ServiceModule_ProvideUserServiceFactory(module, retrofitProvider);
  }

  public static UserService provideUserService(ServiceModule instance, Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(instance.provideUserService(retrofit));
  }
}
