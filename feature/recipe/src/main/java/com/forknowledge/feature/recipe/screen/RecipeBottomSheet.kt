package com.forknowledge.feature.recipe.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forknowledge.core.common.extension.isUrlFormat
import com.forknowledge.core.ui.theme.Green91C747
import com.forknowledge.core.ui.theme.RedFF4950
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.component.AppBasicTextField
import com.forknowledge.core.ui.theme.component.AppButton
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.feature.recipe.R

const val SOURCE_NAME_MAX_LENGTH = 100
const val SOURCE_URL_MAX_LENGTH = 100

@Composable
fun AddSourceBottomSheet(
    sourceName: String,
    url: String,
    onSave: (sourceName: String, url: String) -> Unit
) {
    var sourceName by remember { mutableStateOf(sourceName) }
    var url by remember { mutableStateOf(url) }
    var isUrlValid by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 24.dp,
                horizontal = 16.dp
            )
    ) {
        AppText(
            text = stringResource(R.string.create_recipe_bottom_sheet_add_source_title),
            textStyle = Typography.labelLarge
        )

        AppBasicTextField(
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth(),
            value = sourceName,
            placeholder = stringResource(R.string.create_recipe_bottom_sheet_add_source_placeholder),
            textStyle = Typography.bodyMedium,
            singleLine = true,
            onValueChanged = { name ->
                if (name.length <= SOURCE_NAME_MAX_LENGTH) {
                    sourceName = name
                }
            }
        )

        AppBasicTextField(
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth(),
            value = url,
            placeholder = stringResource(R.string.create_recipe_bottom_sheet_url_placeholder),
            textStyle = Typography.bodyMedium,
            singleLine = true,
            onValueChanged = { sourceUrl ->
                if (sourceUrl.length <= SOURCE_URL_MAX_LENGTH) {
                    url = sourceUrl
                }
            }
        )

        if (!isUrlValid) {
            AppText(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                text = stringResource(R.string.create_recipe_bottom_sheet_url_error),
                textStyle = Typography.bodySmall,
                color = RedFF4950,
                textAlign = TextAlign.End
            )
        }

        AppButton(
            modifier = Modifier
                .padding(top = 40.dp)
                .fillMaxWidth()
                .height(36.dp),
            buttonText = stringResource(R.string.create_recipe_button_save),
            buttonColor = Green91C747,
            textStyle = Typography.labelMedium,
            onClicked = {
                if (url.isUrlFormat()) {
                    onSave(sourceName, url)
                } else {
                    isUrlValid = false
                }
            }
        )
    }
}

@Composable
fun AddMethodBottomSheet(
    method: String,
    onSave: (String) -> Unit
) {
    var method by remember { mutableStateOf(method) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 24.dp,
                horizontal = 16.dp
            )
    ) {
        AppText(
            text = stringResource(R.string.create_recipe_bottom_sheet_add_method_title),
            textStyle = Typography.labelLarge
        )

        AppText(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            text = stringResource(R.string.create_recipe_bottom_sheet_add_method_instruction),
            textStyle = Typography.bodyMedium
        )

        AppBasicTextField(
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth(),
            value = method,
            placeholder = stringResource(R.string.create_recipe_bottom_sheet_add_method_placeholder),
            textStyle = Typography.bodyMedium,
            onValueChanged = { method = it }
        )

        AppButton(
            modifier = Modifier
                .padding(top = 40.dp)
                .fillMaxWidth()
                .height(36.dp),
            buttonText = stringResource(R.string.create_recipe_button_generate),
            buttonColor = Green91C747,
            textStyle = Typography.labelMedium,
            onClicked = { onSave(method) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddSourceBottomSheetPreview() {
    AddSourceBottomSheet(
        sourceName = "",
        url = "",
        onSave = { _, _ -> }
    )
}

@Preview(showBackground = true)
@Composable
fun AddMethodBottomSheetPreview() {
    AddMethodBottomSheet(
        method = "",
        onSave = { }
    )
}