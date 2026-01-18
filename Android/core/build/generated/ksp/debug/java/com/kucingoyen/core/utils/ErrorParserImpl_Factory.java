package com.kucingoyen.core.utils;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class ErrorParserImpl_Factory implements Factory<ErrorParserImpl> {
  private final Provider<Context> contextProvider;

  public ErrorParserImpl_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public ErrorParserImpl get() {
    return newInstance(contextProvider.get());
  }

  public static ErrorParserImpl_Factory create(javax.inject.Provider<Context> contextProvider) {
    return new ErrorParserImpl_Factory(Providers.asDaggerProvider(contextProvider));
  }

  public static ErrorParserImpl_Factory create(Provider<Context> contextProvider) {
    return new ErrorParserImpl_Factory(contextProvider);
  }

  public static ErrorParserImpl newInstance(Context context) {
    return new ErrorParserImpl(context);
  }
}
