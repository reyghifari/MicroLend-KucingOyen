package com.kucingoyen.data.cache.impl;

import android.content.SharedPreferences;
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
public final class AppSessionCacheImpl_Factory implements Factory<AppSessionCacheImpl> {
  private final Provider<SharedPreferences> prefProvider;

  public AppSessionCacheImpl_Factory(Provider<SharedPreferences> prefProvider) {
    this.prefProvider = prefProvider;
  }

  @Override
  public AppSessionCacheImpl get() {
    return newInstance(prefProvider.get());
  }

  public static AppSessionCacheImpl_Factory create(
      javax.inject.Provider<SharedPreferences> prefProvider) {
    return new AppSessionCacheImpl_Factory(Providers.asDaggerProvider(prefProvider));
  }

  public static AppSessionCacheImpl_Factory create(Provider<SharedPreferences> prefProvider) {
    return new AppSessionCacheImpl_Factory(prefProvider);
  }

  public static AppSessionCacheImpl newInstance(SharedPreferences pref) {
    return new AppSessionCacheImpl(pref);
  }
}
