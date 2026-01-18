package com.kucingoyen.auth.repository;

import com.kucingoyen.core.utils.DispatcherProvider;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
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
public final class AuthRepositoryImpl_Factory implements Factory<AuthRepositoryImpl> {
  private final Provider<DispatcherProvider> dispatcherProvider;

  public AuthRepositoryImpl_Factory(Provider<DispatcherProvider> dispatcherProvider) {
    this.dispatcherProvider = dispatcherProvider;
  }

  @Override
  public AuthRepositoryImpl get() {
    return newInstance(dispatcherProvider.get());
  }

  public static AuthRepositoryImpl_Factory create(
      javax.inject.Provider<DispatcherProvider> dispatcherProvider) {
    return new AuthRepositoryImpl_Factory(Providers.asDaggerProvider(dispatcherProvider));
  }

  public static AuthRepositoryImpl_Factory create(Provider<DispatcherProvider> dispatcherProvider) {
    return new AuthRepositoryImpl_Factory(dispatcherProvider);
  }

  public static AuthRepositoryImpl newInstance(DispatcherProvider dispatcher) {
    return new AuthRepositoryImpl(dispatcher);
  }
}
