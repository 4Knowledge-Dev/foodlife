package com.forknowledge.core.ui.theme.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.forknowledge.core.ui.R.drawable
import com.forknowledge.core.ui.theme.GreyA7A6A6
import com.forknowledge.core.ui.theme.GreyEBEBEB
import com.forknowledge.core.ui.theme.openSansFamily

@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    placeholder: String = "",
    supportingText: String = "",
    @DrawableRes leadingIcon: Int? = null,
    onLeadingIconClick: () -> Unit = {},
    trailingIcon: @Composable (() -> Unit)? = null,
    corner: Dp = 14.dp,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onValueChanged: (String) -> Unit = {},
) {
    TextField(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(corner),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = GreyEBEBEB,
            unfocusedContainerColor = GreyEBEBEB,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = Color.Black
        ),
        supportingText = if (supportingText.isNotEmpty()) {
            @Composable {
                TextFieldErrorLabel(
                    modifier = Modifier.fillMaxWidth(),
                    error = supportingText
                )
            }
        } else {
            null
        },
        leadingIcon = leadingIcon?.let { icon ->
            {
                Icon(
                    modifier = Modifier.clickable { onLeadingIconClick() },
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    tint = GreyA7A6A6
                )
            }
        },
        trailingIcon = trailingIcon,
        placeholder = {
            TextFieldPlaceholder(placeholder = placeholder)
        },
        value = value,
        singleLine = true,
        textStyle = TextStyle(
            fontFamily = openSansFamily
        ),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        onValueChange = { onValueChanged(it) },
    )
}

@Composable
fun AppOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    label: String = "",
    placeholder: String = "",
    readOnly: Boolean = true,
    trailingIcon: ImageVector? = null,
    onValueChanged: (String) -> Unit = {},
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        label = { TextFieldPlaceholder(placeholder = label) },
        placeholder = { TextFieldPlaceholder(placeholder = placeholder) },
        trailingIcon = trailingIcon?.let { icon ->
            {
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = "Select date"
                )
            }
        },
        readOnly = readOnly,
        onValueChange = { onValueChanged(it) },
    )
}

@Composable
fun TextFieldPlaceholder(
    modifier: Modifier = Modifier,
    placeholder: String,
) {
    Text(
        modifier = modifier,
        text = placeholder,
        fontFamily = openSansFamily,
        fontSize = 16.sp,
        color = GreyA7A6A6,
        textAlign = TextAlign.Center
    )
}

@Composable
fun TextFieldErrorLabel(
    modifier: Modifier = Modifier,
    error: String,
) {
    Text(
        modifier = modifier,
        text = error,
        fontFamily = openSansFamily,
        fontSize = 14.sp,
        color = Color.Red
    )
}

@Preview(showBackground = true)
@Composable
fun TextFieldErrorLabelPreview() {
    TextFieldErrorLabel(
        error = "8–30 characters, 1 uppercase &amp; 1 lowercase &amp; 1 digit"
    )
}

@Preview
@Composable
fun AppTextFieldPreview() {
    AppTextField(
        value = "Food Life",
        leadingIcon = drawable.ic_password_lock,
        trailingIcon = {
            Icon(
                painter = painterResource(id = drawable.ic_visibility_on),
                contentDescription = null,
                tint = GreyA7A6A6
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun AppOutlinedTextFieldPreview() {
    AppOutlinedTextField(
        value = "",
        label = "DOB",
        placeholder = "MM/DD/YYYY",
        trailingIcon = Icons.Default.DateRange
    )
}
