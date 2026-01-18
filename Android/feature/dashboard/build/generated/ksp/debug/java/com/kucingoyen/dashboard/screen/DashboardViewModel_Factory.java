package com.kucingoyen.dashboard.screen;

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
public final class DashboardViewModel_Factory implements Factory<DashboardViewModel> {
  private final Provider<ViewModelUtils> viewModelUtilsProvider;

  public DashboardViewModel_Factory(Provider<ViewModelUtils> viewModelUtilsProvider) {
    this.viewModelUtilsProvider = viewModelUtilsProvider;
  }

  @Override
  public DashboardViewModel get() {
    return newInstance(viewModelUtilsProvider.get());
  }

  public static DashboardViewModel_Factory create(
      javax.inject.Provider<ViewModelUtils> viewModelUtilsProvider) {
    return new DashboardViewModel_Factory(Providers.asDaggerProvider(viewModelUtilsProvider));
  }

  public static DashboardViewModel_Factory create(Provider<ViewModelUtils> viewModelUtilsProvider) {
    return new DashboardViewModel_Factory(viewModelUtilsProvider);
  }

  public static DashboardViewModel newInstance(ViewModelUtils viewModelUtils) {
    return new DashboardViewModel(viewModelUtils);
  }
}
