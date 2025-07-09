package com.forknowledge.core.ui.theme.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.forknowledge.core.ui.R.drawable
import com.forknowledge.core.ui.theme.GreyA7A6A6
import com.forknowledge.core.ui.theme.GreyEBEBEB
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.openSansFamily

@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    placeholder: String = "",
    supportingText: String = "",
    textStyle: TextStyle = Typography.bodyMedium,
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
            TextFieldPlaceholder(
                placeholder = placeholder,
                textStyle = textStyle
            )
        },
        value = value,
        singleLine = true,
        textStyle = textStyle,
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
    label: String? = null,
    placeholder: String? = null,
    textStyle: TextStyle = Typography.bodyMedium,
    readOnly: Boolean = false,
    trailingIcon: ImageVector? = null,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    onValueChanged: (String) -> Unit = {},
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        label = label?.let { label ->
            {
                TextFieldPlaceholder(
                    placeholder = label,
                    textStyle = textStyle
                )
            }
        },
        placeholder = placeholder?.let { placeholder ->
            {
                TextFieldPlaceholder(
                    placeholder = placeholder,
                    textStyle = textStyle
                )
            }
        },
        textStyle = textStyle,
        trailingIcon = trailingIcon?.let { icon ->
            {
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = "Select date"
                )
            }
        },
        readOnly = readOnly,
        singleLine = singleLine,
        maxLines = maxLines,
        onValueChange = { onValueChanged(it) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBasicTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    label: String? = null,
    placeholder: String? = null,
    textStyle: TextStyle = Typography.bodyMedium,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onValueChanged: (String) -> Unit
) {
    val interaction = remember { MutableInteractionSource() }
    BasicTextField(
        modifier = modifier,
        value = value,
        textStyle = textStyle,
        singleLine = singleLine,
        maxLines = if (singleLine) 1 else maxLines,
        minLines = if (singleLine) 1 else minLines,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        decorationBox = { innerTextField ->
            OutlinedTextFieldDefaults.DecorationBox(
                value = value,
                innerTextField = innerTextField,
                enabled = true,
                singleLine = singleLine,
                visualTransformation = VisualTransformation.None,
                interactionSource = interaction,
                placeholder = placeholder?.let { placeholder ->
                    {
                        TextFieldPlaceholder(
                            placeholder = placeholder,
                            textStyle = textStyle
                        )
                    }
                },
                label = label?.let { label ->
                    {
                        TextFieldPlaceholder(
                            placeholder = label,
                            textStyle = textStyle
                        )
                    }
                },
                contentPadding = PaddingValues(
                    vertical = 8.dp,
                    horizontal = 12.dp
                )
            )
        },
        onValueChange = { onValueChanged(it) },
    )
}

@Composable
fun TextFieldPlaceholder(
    modifier: Modifier = Modifier,
    placeholder: String,
    textStyle: TextStyle
) {
    AppText(
        modifier = modifier,
        text = placeholder,
        textStyle = textStyle,
        color = GreyA7A6A6
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

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
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

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun AppOutlinedTextFieldPreview() {
    AppOutlinedTextField(
        value = "",
        label = "DOB",
        placeholder = "MM/DD/YYYY",
        trailingIcon = Icons.Default.DateRange
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun AppBasicTextFieldPreview() {
    AppBasicTextField(
        value = "",
        placeholder = "Placeholder",
        onValueChanged = {}
    )
}
