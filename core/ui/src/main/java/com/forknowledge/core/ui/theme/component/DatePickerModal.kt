package com.forknowledge.core.ui.theme.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forknowledge.core.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    modifier: Modifier = Modifier,
    title: String? = null,
    headline: String? = null,
    confirmText: String = "",
    dismissText: String = "",
    date: Long? = null,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {

    val datePickerState = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Picker,
        initialSelectedDateMillis = date,
    )

    DatePickerDialog(
        modifier = modifier,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text(confirmText)
            }

        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(dismissText)
            }
        },
        onDismissRequest = onDismiss,
    ) {
        DatePicker(
            modifier = Modifier.padding(top = 24.dp),
            title = title?.let {
                {
                    AppText(
                        modifier = Modifier.padding(start = 24.dp),
                        text = it,
                        textStyle = Typography.titleSmall
                    )
                }
            },
            headline = headline?.let {
                {
                    AppText(
                        modifier = Modifier.padding(
                            start = 24.dp,
                            bottom = 24.dp
                        ),
                        text = it,
                        textStyle = Typography.bodyLarge
                    )
                }
            },
            showModeToggle = false,
            state = datePickerState
        )
    }
}

@Preview
@Composable
fun ModalDateInputPreview() {
    DatePickerModal(
        title = "Birthday",
        headline = "Enter your birthday",
        confirmText = "OK",
        dismissText = "Cancel",
        onDateSelected = {},
        onDismiss = {}
    )
}
