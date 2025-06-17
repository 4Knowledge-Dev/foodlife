package com.forknowledge.feature.onboarding.ui.surveyscreen

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forknowledge.core.data.datasource.ingredients
import com.forknowledge.core.ui.theme.component.FlowRowItem

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExcludeSection(
    excludes: List<String>,
    onIngredientSelected: (exclude: String) -> Unit,
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
    ) {
        ingredients.forEach { ingredient ->
            FlowRowItem(
                item = ingredient,
                isSelected = excludes.contains(ingredient),
                onItemSelected = { onIngredientSelected(it) }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun ExcludeSectionPreview() {
    ExcludeSection(
        excludes = listOf("caffeine", "sugar", "fish"),
        onIngredientSelected = {}
    )
}
