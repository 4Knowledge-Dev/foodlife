package com.forknowledge.feature.authentication.ui;

import androidx.lifecycle.SavedStateHandle;
import com.forknowledge.core.domain.component.AuthenticationManager;
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
public final class AuthenticationViewModel_Factory implements Factory<AuthenticationViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<AuthenticationManager> authenticationManagerProvider;

  public AuthenticationViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<AuthenticationManager> authenticationManagerProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.authenticationManagerProvider = authenticationManagerProvider;
  }

  @Override
  public AuthenticationViewModel get() {
    return newInstance(savedStateHandleProvider.get(), authenticationManagerProvider.get());
  }

  public static AuthenticationViewModel_Factory create(
      javax.inject.Provider<SavedStateHandle> savedStateHandleProvider,
      javax.inject.Provider<AuthenticationManager> authenticationManagerProvider) {
    return new AuthenticationViewModel_Factory(Providers.asDaggerProvider(savedStateHandleProvider), Providers.asDaggerProvider(authenticationManagerProvider));
  }

  public static AuthenticationViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<AuthenticationManager> authenticationManagerProvider) {
    return new AuthenticationViewModel_Factory(savedStateHandleProvider, authenticationManagerProvider);
  }

  public static AuthenticationViewModel newInstance(SavedStateHandle savedStateHandle,
      AuthenticationManager authenticationManager) {
    return new AuthenticationViewModel(savedStateHandle, authenticationManager);
  }
}
