package com.forknowledge.feature.authentication.ui.screen.signInWithEmail

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.forknowledge.core.data.datatype.LoginResultType
import com.forknowledge.core.ui.R.drawable
import com.forknowledge.core.ui.theme.Green91C747
import com.forknowledge.core.ui.theme.GreyB7BDC4
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.component.AppButton
import com.forknowledge.core.ui.theme.component.AppTextField
import com.forknowledge.feature.authentication.R
import com.forknowledge.feature.authentication.SignInWithEmailRoute

fun NavController.navigateToSignInWithEmail(navOptions: NavOptions? = null) =
    navigate(SignInWithEmailRoute, navOptions)

@Composable
internal fun SignInWithEmailScreen(
    viewModel: AuthenticationViewModel,
    onBackClicked: () -> Unit,
    onNavigateToRegisterClicked: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val context = LocalContext.current

    val email by viewModel.email.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    val shouldShowEmailError by viewModel.shouldShowEmailError
    val isEmailValidated by viewModel.isEmailValidated
    val isButtonEnabled by viewModel.isLoginButtonEnabled
    val signInState by viewModel.signInState.collectAsStateWithLifecycle()

    when (signInState) {
        LoginResultType.SUCCESS_NEW_USER -> onNavigateToOnboarding()
        LoginResultType.SUCCESS_OLD_USER -> onNavigateToHome()
        LoginResultType.FAIL -> {
            Toast.makeText(
                context,
                stringResource(R.string.authentication_sign_in_fail),
                Toast.LENGTH_SHORT
            ).show()
        }
        else -> { /* Do nothing */ }
    }

    if (isEmailValidated) {
        viewModel.signInWithEmail(email)
    }

    SignInSection(
        email = email,
        password = password,
        shouldShowEmailError = shouldShowEmailError,
        isButtonEnabled = isButtonEnabled,
        onEmailUpdated = { viewModel.updateEmail(it) },
        onPasswordUpdated = { viewModel.updatePassword(it) },
        onLoginClicked = { viewModel.onValidate(it) },
        onBackClicked = {
            onBackClicked()
            viewModel.resetStates()
        },
        onRegisterClicked = {
            onNavigateToRegisterClicked()
            viewModel.resetStates()
        },
    )
}

@Composable
internal fun SignInSection(
    email: String,
    password: String,
    shouldShowEmailError: Boolean,
    isButtonEnabled: Boolean,
    onEmailUpdated: (String) -> Unit,
    onPasswordUpdated: (String) -> Unit,
    onLoginClicked: (String) -> Unit,
    onBackClicked: () -> Unit,
    onRegisterClicked: () -> Unit,
) {

    var isPasswordVisible by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 24.dp)
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
            text = stringResource(id = R.string.sign_in_with_email_login_header),
            style = Typography.titleSmall,
            textAlign = TextAlign.Center
        )

        AppTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 36.dp,
                    start = 16.dp,
                    end = 16.dp
                )
                .focusRequester(focusRequester),
            value = email,
            placeholder = stringResource(R.string.sign_in_with_email_input_email_placeholder),
            supportingText = if (shouldShowEmailError) {
                stringResource(id = R.string.sign_in_with_email_supporting_text_validation_error)
            } else {
                ""
            },
            onValueChanged = { onEmailUpdated(it) }
        )

        AppTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 24.dp,
                    start = 16.dp,
                    end = 16.dp
                )
                .clickable { focusRequester.requestFocus() },
            value = password,
            placeholder = stringResource(R.string.sign_in_with_email_enter_password_input_password_placeholder),
            trailingIcon = {
                if (isPasswordVisible) {
                    Icon(
                        modifier = Modifier.clickable { isPasswordVisible = false },
                        painter = painterResource(id = drawable.ic_visibility_off),
                        contentDescription = null
                    )
                } else {
                    Icon(
                        modifier = Modifier.clickable { isPasswordVisible = true },
                        painter = painterResource(id = drawable.ic_visibility_on),
                        contentDescription = null
                    )
                }
            },
            visualTransformation = if (isPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            onValueChanged = { onPasswordUpdated(it) },
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {

            Text(
                modifier = Modifier.padding(top = 21.dp),
                text = stringResource(id = R.string.sign_in_with_email_login_do_not_have_account),
                style = Typography.bodyMedium
            )

            Text(
                modifier = Modifier
                    .padding(top = 21.dp, start = 6.dp)
                    .clickable { onRegisterClicked() },
                text = stringResource(id = R.string.sign_up_with_email_register_button_text),
                style = Typography.bodyMedium,
                color = Green91C747,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.weight(1F))

        HorizontalDivider(
            modifier = Modifier.padding(bottom = 24.dp),
            color = GreyB7BDC4
        )

        AppButton(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .height(50.dp)
                .fillMaxWidth(),
            enabled = isButtonEnabled,
            buttonText = stringResource(R.string.sign_in_with_email_button_login_text),
            onClicked = { onLoginClicked(email) }
        )
    }
}

@Preview(showBackground = true)
@Composable
internal fun SignInSectionPreview() {
    SignInSection(
        email = "",
        password = "",
        shouldShowEmailError = true,
        isButtonEnabled = true,
        onEmailUpdated = {},
        onPasswordUpdated = {},
        onLoginClicked = {},
        onBackClicked = {},
        onRegisterClicked = {}
    )
}
