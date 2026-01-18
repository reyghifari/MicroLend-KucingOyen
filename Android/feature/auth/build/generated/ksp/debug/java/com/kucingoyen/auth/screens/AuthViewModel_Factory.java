package com.kucingoyen.auth.screens;

import com.kucingoyen.core.utils.ViewModelUtils;
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
public final class AuthViewModel_Factory implements Factory<AuthViewModel> {
  private final Provider<ViewModelUtils> viewModelUtilsProvider;

  public AuthViewModel_Factory(Provider<ViewModelUtils> viewModelUtilsProvider) {
    this.viewModelUtilsProvider = viewModelUtilsProvider;
  }

  @Override
  public AuthViewModel get() {
    return newInstance(viewModelUtilsProvider.get());
  }

  public static AuthViewModel_Factory create(
      javax.inject.Provider<ViewModelUtils> viewModelUtilsProvider) {
    return new AuthViewModel_Factory(Providers.asDaggerProvider(viewModelUtilsProvider));
  }

  public static AuthViewModel_Factory create(Provider<ViewModelUtils> viewModelUtilsProvider) {
    return new AuthViewModel_Factory(viewModelUtilsProvider);
  }

  public static AuthViewModel newInstance(ViewModelUtils viewModelUtils) {
    return new AuthViewModel(viewModelUtils);
  }
}
