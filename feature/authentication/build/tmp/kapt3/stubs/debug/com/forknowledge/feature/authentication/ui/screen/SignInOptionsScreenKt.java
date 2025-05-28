package com.forknowledge.feature.authentication.ui.screen;

@kotlin.Metadata(mv = {2, 1, 0}, k = 2, xi = 48, d1 = {"\u00004\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\u001a\u001e\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0007\u001aH\u0010\u0006\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0001\u001aC\u0010\r\u001a\u00020\u00012\b\b\u0003\u0010\u000e\u001a\u00020\u000f2\b\b\u0001\u0010\u0010\u001a\u00020\u000f2\b\b\u0001\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0007\u00a2\u0006\u0004\b\u0016\u0010\u0017\u001a\b\u0010\u0018\u001a\u00020\u0001H\u0007\u001a\b\u0010\u0019\u001a\u00020\u0001H\u0007\u001a\b\u0010\u001a\u001a\u00020\u0001H\u0007\u001a\b\u0010\u001b\u001a\u00020\u0001H\u0007\u00a8\u0006\u001c"}, d2 = {"SignInOptionScreen", "", "viewModel", "Lcom/forknowledge/feature/authentication/ui/AuthenticationViewModel;", "signInWithEmailClicked", "Lkotlin/Function0;", "SignInUiSection", "context", "Landroid/app/Activity;", "onSignInWithAvailableCredentials", "onSignInWithEmailClicked", "onSignInWithGoogleClicked", "onSignInWithFacebookClicked", "SignInOptionButton", "buttonTextColor", "Landroidx/compose/ui/graphics/Color;", "buttonBackground", "buttonIcon", "", "buttonText", "", "onClicked", "SignInOptionButton-dgg9oW8", "(JJILjava/lang/String;Lkotlin/jvm/functions/Function0;)V", "TermAndPolicyText", "SignInWithEmailButtonPreview", "SignInWithGoogleButtonPreview", "SignInUiSectionPreview", "authentication_debug"})
public final class SignInOptionsScreenKt {
    
    @androidx.annotation.OptIn(markerClass = {androidx.media3.common.util.UnstableApi.class})
    @androidx.compose.runtime.Composable()
    public static final void SignInOptionScreen(@org.jetbrains.annotations.NotNull()
    com.forknowledge.feature.authentication.ui.AuthenticationViewModel viewModel, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> signInWithEmailClicked) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void SignInUiSection(@org.jetbrains.annotations.NotNull()
    android.app.Activity context, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSignInWithAvailableCredentials, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSignInWithEmailClicked, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSignInWithGoogleClicked, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSignInWithFacebookClicked) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.ui.text.ExperimentalTextApi.class})
    @androidx.compose.runtime.Composable()
    public static final void TermAndPolicyText() {
    }
    
    @androidx.compose.ui.tooling.preview.Preview(name = "Sign in with Email")
    @androidx.compose.runtime.Composable()
    public static final void SignInWithEmailButtonPreview() {
    }
    
    @androidx.compose.ui.tooling.preview.Preview(name = "Sign in with Google")
    @androidx.compose.runtime.Composable()
    public static final void SignInWithGoogleButtonPreview() {
    }
    
    @androidx.compose.ui.tooling.preview.Preview(showBackground = true)
    @androidx.compose.runtime.Composable()
    public static final void SignInUiSectionPreview() {
    }
}