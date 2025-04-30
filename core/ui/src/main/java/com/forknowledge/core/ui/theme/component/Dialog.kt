package com.forknowledge.core.ui.theme.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.buttonTextDialog

@Composable
fun AppAlertDialog(
    confirmButton: String,
    onConfirmationButtonClicked: () -> Unit,
    onDismissDialogRequested: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int? = null,
    title: String? = null,
    content: String? = null,
    dismissButton: String? = null,
    containerColor: Color = Color.White,
    onDismissButtonClicked: () -> Unit = {},
) {
    AlertDialog(
        modifier = modifier,
        icon = icon?.let {
            @Composable {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null
                )
            }
        },
        title = title?.let {
            @Composable {
                Text(
                    text = it,
                    style = Typography.titleSmall
                )
            }
        },
        text = content?.let {
            @Composable {
                Text(
                    text = it,
                    style = Typography.bodyMedium
                )
            }
        },
        confirmButton = {
            Text(
                modifier = Modifier.clickable { onConfirmationButtonClicked() },
                text = confirmButton,
                style = buttonTextDialog
            )
        },
        dismissButton = dismissButton?.let {
            @Composable {
                Text(
                    modifier = Modifier.clickable { onDismissButtonClicked() },
                    text = it,
                    style = buttonTextDialog
                )
            }
        },
        containerColor = containerColor,
        onDismissRequest = onDismissDialogRequested
    )
}

@Composable
fun AppSnackBar() {

}

@Preview(showBackground = true)
@Composable
fun AppAlertDialogPreview() {
    AppAlertDialog(
        title = "Title",
        content = "This is description",
        confirmButton = "OK",
        dismissButton = "Cancel",
        onConfirmationButtonClicked = {},
        onDismissDialogRequested = {}
    )
}
