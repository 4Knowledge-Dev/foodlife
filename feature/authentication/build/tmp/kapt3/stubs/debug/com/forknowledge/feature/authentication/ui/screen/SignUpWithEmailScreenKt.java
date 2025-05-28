package com.forknowledge.feature.authentication.ui.screen;

@kotlin.Metadata(mv = {2, 1, 0}, k = 2, xi = 48, d1 = {"\u0000:\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\u001a\u0016\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\n\b\u0002\u0010\u0003\u001a\u0004\u0018\u00010\u0004\u001a,\u0010\u0005\u001a\u00020\u00012\u0006\u0010\u0006\u001a\u00020\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00010\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\tH\u0001\u001a\u0082\u0001\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u00102\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00010\u00142\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00010\u00142\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00010\t2\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00010\t2\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00010\tH\u0001\u001a\b\u0010\u0018\u001a\u00020\u0001H\u0001\u00a8\u0006\u0019"}, d2 = {"navigateToSignUpWithEmail", "", "Landroidx/navigation/NavController;", "navOptions", "Landroidx/navigation/NavOptions;", "SignUpWithEmailScreen", "viewModel", "Lcom/forknowledge/feature/authentication/ui/AuthenticationViewModel;", "onBackClicked", "Lkotlin/Function0;", "onNavigateToLoginClicked", "SignUpSection", "email", "", "password", "shouldShowEmailError", "", "shouldShowPasswordError", "isButtonEnabled", "onEmailUpdated", "Lkotlin/Function1;", "onPasswordUpdated", "onUserValidated", "onNavigateToLogin", "SignUpSectionPreview", "authentication_debug"})
public final class SignUpWithEmailScreenKt {
    
    public static final void navigateToSignUpWithEmail(@org.jetbrains.annotations.NotNull()
    androidx.navigation.NavController $this$navigateToSignUpWithEmail, @org.jetbrains.annotations.Nullable()
    androidx.navigation.NavOptions navOptions) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void SignUpWithEmailScreen(@org.jetbrains.annotations.NotNull()
    com.forknowledge.feature.authentication.ui.AuthenticationViewModel viewModel, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBackClicked, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onNavigateToLoginClicked) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void SignUpSection(@org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String password, boolean shouldShowEmailError, boolean shouldShowPasswordError, boolean isButtonEnabled, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onEmailUpdated, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onPasswordUpdated, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onUserValidated, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBackClicked, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onNavigateToLogin) {
    }
    
    @androidx.compose.ui.tooling.preview.Preview(showBackground = true)
    @androidx.compose.runtime.Composable()
    public static final void SignUpSectionPreview() {
    }
}