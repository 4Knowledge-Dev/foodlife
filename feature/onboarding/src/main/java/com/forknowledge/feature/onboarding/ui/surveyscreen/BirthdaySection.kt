package com.forknowledge.feature.onboarding.ui.surveyscreen

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.forknowledge.core.common.extension.convertMillisToDate
import com.forknowledge.core.ui.theme.component.AppOutlinedTextField
import com.forknowledge.core.ui.theme.component.DatePickerModal
import com.forknowledge.feature.onboarding.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthdaySection(
    modifier: Modifier = Modifier,
    selectedDate: Long?,
    onDateChanged: (Long) -> Unit
) {

    var showModal by remember { mutableStateOf(false) }

    AppOutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(selectedDate) {
                awaitEachGesture {
                    // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
                    // in the Initial pass to observe events before the text field consumes them
                    // in the Main pass.
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            },
        value = selectedDate?.convertMillisToDate() ?: "",
        label = stringResource(R.string.onboarding_survey_birthday_label),
        placeholder = stringResource(R.string.onboarding_survey_birthday_placeholder),
        trailingIcon = Icons.Default.DateRange,
    )

    if (showModal) {
        DatePickerModal(
            headline = stringResource(R.string.onboarding_survey_date_picker_headline),
            confirmText = stringResource(R.string.onboarding_survey_date_picker_button_confirm),
            date = selectedDate,
            onDateSelected = { date ->
                date?.let { onDateChanged(date) }
            },
            onDismiss = { showModal = false }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BirthdaySectionPreview() {
    BirthdaySection(
        selectedDate = 1231234,
        onDateChanged = {}
    )
}
