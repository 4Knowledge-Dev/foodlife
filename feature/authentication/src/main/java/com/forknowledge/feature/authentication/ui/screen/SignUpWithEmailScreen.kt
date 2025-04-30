package com.forknowledge.feature.authentication.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.forknowledge.core.ui.R.drawable
import com.forknowledge.core.ui.theme.Green91C747
import com.forknowledge.core.ui.theme.GreyA7A6A6
import com.forknowledge.core.ui.theme.GreyB7BDC4
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.component.AppAlertDialog
import com.forknowledge.core.ui.theme.component.AppButton
import com.forknowledge.core.ui.theme.component.AppTextField
import com.forknowledge.core.ui.theme.component.LoadingIndicator
import com.forknowledge.core.ui.theme.openSansFamily
import com.forknowledge.feature.authentication.AUTHENTICATION_ROUTE
import com.forknowledge.feature.authentication.R
import com.forknowledge.feature.authentication.ui.AuthenticationViewModel

const val SIGN_UP_ROUTE = "authentication/signUpWithEmail"

fun NavController.navigateToSignUpWithEmail(navOptions: NavOptions? = null) =
    navigate(SIGN_UP_ROUTE, navOptions)

fun NavGraphBuilder.signUpWithEmailScreen(navController: NavController) {
    composable(SIGN_UP_ROUTE) { backStackEntry ->
        val navOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id)
        }
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(AUTHENTICATION_ROUTE)
        }
        SignUpWithEmailScreen(
            viewModel = hiltViewModel<AuthenticationViewModel>(parentEntry),
            onBackClicked = navController::popBackStack,
            onNavigateToLoginClicked = { navController.navigateToSignInWithEmail(navOptions) }
        )
    }
}

@Composable
internal fun SignUpWithEmailScreen(
    viewModel: AuthenticationViewModel,
    onBackClicked: () -> Unit,
    onNavigateToLoginClicked: () -> Unit,
) {
    val email by viewModel.email.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    val shouldShowEmailError by viewModel.shouldShowEmailError
    val shouldShowPasswordError by viewModel.shouldShowSignUpPasswordError
    val isRegisterButtonEnabled by viewModel.isRegisterButtonEnabled

    val isLoading by viewModel.shouldShowLoading
    val dialogError by viewModel.alertDialogError

    Box(contentAlignment = Alignment.Center) {
        SignUpSection(
            email = email,
            password = password,
            shouldShowEmailError = shouldShowEmailError,
            shouldShowPasswordError = shouldShowPasswordError,
            isButtonEnabled = isRegisterButtonEnabled,
            onEmailUpdated = viewModel::updateEmail,
            onPasswordUpdated = viewModel::updatePassword,
            onUserValidated = viewModel::validateUser,
            onBackClicked = {
                onBackClicked()
                viewModel.resetStates()
            },
            onNavigateToLogin = {
                onNavigateToLoginClicked()
                viewModel.resetStates()
            }
        )

        if (isLoading) {
            LoadingIndicator()
        }

        dialogError?.let { message ->
            AppAlertDialog(
                modifier = Modifier.align(Alignment.Center),
                title = stringResource(id = R.string.sign_up_with_email_dialog_error_title),
                content = message,
                confirmButton = stringResource(id = R.string.sign_up_with_email_dialog_confirmation_button_text),
                dismissButton = stringResource(id = R.string.sign_up_with_email_dialog_dismiss_button_text),
                onConfirmationButtonClicked = {},
                onDismissButtonClicked = {
                    onNavigateToLoginClicked()
                },
                onDismissDialogRequested = {}
            )
        }
    }
}

@Composable
internal fun SignUpSection(
    email: String,
    password: String,
    shouldShowEmailError: Boolean,
    shouldShowPasswordError: Boolean,
    isButtonEnabled: Boolean,
    onEmailUpdated: (String) -> Unit,
    onPasswordUpdated: (String) -> Unit,
    onUserValidated: () -> Unit,
    onBackClicked: () -> Unit,
    onNavigateToLogin: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    var shouldShowPassword by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp)
            .imePadding()
    ) {

        Icon(
            modifier = Modifier
                .clickable { onBackClicked() }
                .padding(start = 16.dp),
            painter = painterResource(id = drawable.ic_back),
            contentDescription = null
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = stringResource(id = R.string.sign_up_with_email_welcome),
            style = TextStyle(
                fontFamily = openSansFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            ),
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 8.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
            text = stringResource(id = R.string.sign_up_with_email_title),
            style = Typography.titleSmall,
            textAlign = TextAlign.Center
        )

        AppTextField(
            modifier = Modifier
                .padding(
                    top = 36.dp,
                    start = 16.dp,
                    end = 16.dp
                )
                .focusRequester(focusRequester),
            value = email,
            placeholder = stringResource(id = R.string.sign_up_with_email_text_field_email_placeholder),
            supportingText = if (shouldShowEmailError) {
                stringResource(id = R.string.sign_in_with_email_supporting_text_validation_error)
            } else {
                ""
            },
            leadingIcon = drawable.ic_email,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            onValueChanged = { onEmailUpdated(it) }
        )

        AppTextField(
            modifier = Modifier
                .padding(
                    top = 12.dp,
                    start = 16.dp,
                    end = 16.dp
                )
                .clickable { focusRequester.requestFocus() },
            value = password,
            placeholder = stringResource(id = R.string.sign_up_with_email_text_field_password_placeholder),
            supportingText = if (shouldShowPasswordError) {
                stringResource(id = R.string.sign_up_with_email_password_validation_error_text)
            } else {
                ""
            },
            leadingIcon = drawable.ic_password_lock,
            trailingIcon = {
                if (shouldShowPassword) {
                    Icon(
                        modifier = Modifier.clickable { shouldShowPassword = false },
                        painter = painterResource(id = drawable.ic_visibility_off),
                        contentDescription = null,
                        tint = GreyA7A6A6
                    )
                } else {
                    Icon(
                        modifier = Modifier.clickable { shouldShowPassword = true },
                        painter = painterResource(id = drawable.ic_visibility_on),
                        contentDescription = null,
                        tint = GreyA7A6A6
                    )
                }
            },
            visualTransformation = if (shouldShowPassword) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }
            ),
            onValueChanged = { onPasswordUpdated(it) }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 32.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
            horizontalArrangement = Arrangement.Center
        ) {

            Text(
                text = stringResource(id = R.string.sign_up_with_email_already_have_account_text),
                style = Typography.bodyMedium
            )

            Text(
                modifier = Modifier
                    .padding(start = 6.dp)
                    .clickable { onNavigateToLogin() },
                text = stringResource(id = R.string.sign_in_with_email_button_login_text),
                style = Typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = Green91C747
            )
        }

        Spacer(modifier = Modifier.weight(1F))

        HorizontalDivider(
            modifier = Modifier.padding(top = 48.dp),
            color = GreyB7BDC4
        )

        AppButton(
            modifier = Modifier
                .padding(
                    top = 24.dp,
                    start = 16.dp,
                    end = 16.dp
                )
                .height(50.dp)
                .fillMaxWidth(),
            enabled = isButtonEnabled,
            buttonText = stringResource(R.string.sign_up_with_email_register_button_text),
            onClicked = onUserValidated
        )
    }
}

@Preview(showBackground = true)
@Composable
internal fun SignUpSectionPreview() {
    SignUpSection(
        email = "",
        password = "",
        shouldShowEmailError = true,
        shouldShowPasswordError = true,
        isButtonEnabled = true,
        onEmailUpdated = {},
        onPasswordUpdated = {},
        onUserValidated = {},
        onBackClicked = {},
        onNavigateToLogin = {},
    )
}
