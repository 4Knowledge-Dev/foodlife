package com.forknowledge.feature.onboarding.ui.surveyscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forknowledge.core.ui.theme.Green91C747
import com.forknowledge.core.ui.theme.GreyB7BDC4
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.feature.onboarding.ui.excludedIngredients

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
        excludedIngredients.forEach { ingredient ->
            IngredientItem(
                ingredient = ingredient,
                isSelected = excludes.contains(ingredient),
                onIngredientSelected = onIngredientSelected
            )
        }
    }
}

@Composable
fun IngredientItem(
    ingredient: String,
    isSelected: Boolean,
    onIngredientSelected: (exclude: String) -> Unit
) {
    AppText(
        modifier = Modifier
            .padding(
                start = 6.dp,
                top = 6.dp
            )
            .widthIn(min = 80.dp)
            .background(
                color = if (isSelected) {
                    Green91C747
                } else {
                    White
                },
                shape = RoundedCornerShape(32.dp)
            )
            .clip(RoundedCornerShape(32.dp))
            .border(
                width = 1.dp,
                color = GreyB7BDC4,
                shape = RoundedCornerShape(32.dp)
            )
            .clickable { onIngredientSelected(ingredient) }
            .padding(12.dp),
        text = ingredient.replaceFirst(
            ingredient.first(),
            ingredient.first().uppercaseChar(),
        ),
        color = if (isSelected) {
            White
        } else {
            Black
        },
        textAlign = TextAlign.Center
    )
}

@Preview(showBackground = true)
@Composable
fun ExcludeSectionPreview() {
    ExcludeSection(
        excludes = listOf("caffeine", "sugar", "fish"),
        onIngredientSelected = {}
    )
}
