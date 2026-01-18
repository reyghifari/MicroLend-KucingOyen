package com.kucingoyen.core.utils;

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
public final class ViewModelUtils_Factory implements Factory<ViewModelUtils> {
  private final Provider<DispatcherProvider> dispatcherProvider;

  private final Provider<ErrorParser> errorParserProvider;

  public ViewModelUtils_Factory(Provider<DispatcherProvider> dispatcherProvider,
      Provider<ErrorParser> errorParserProvider) {
    this.dispatcherProvider = dispatcherProvider;
    this.errorParserProvider = errorParserProvider;
  }

  @Override
  public ViewModelUtils get() {
    return newInstance(dispatcherProvider.get(), errorParserProvider.get());
  }

  public static ViewModelUtils_Factory create(
      javax.inject.Provider<DispatcherProvider> dispatcherProvider,
      javax.inject.Provider<ErrorParser> errorParserProvider) {
    return new ViewModelUtils_Factory(Providers.asDaggerProvider(dispatcherProvider), Providers.asDaggerProvider(errorParserProvider));
  }

  public static ViewModelUtils_Factory create(Provider<DispatcherProvider> dispatcherProvider,
      Provider<ErrorParser> errorParserProvider) {
    return new ViewModelUtils_Factory(dispatcherProvider, errorParserProvider);
  }

  public static ViewModelUtils newInstance(DispatcherProvider dispatcherProvider,
      ErrorParser errorParser) {
    return new ViewModelUtils(dispatcherProvider, errorParser);
  }
}
