package com.forknowledge.feature.recipe.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.forknowledge.core.common.extension.htmlToPlainText
import com.forknowledge.core.common.extension.toFormattedNumber
import com.forknowledge.core.ui.R.drawable
import com.forknowledge.core.ui.theme.Black374957
import com.forknowledge.core.ui.theme.GreenA1CE50
import com.forknowledge.core.ui.theme.Grey8A949F
import com.forknowledge.core.ui.theme.GreyDADADA
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.core.ui.theme.component.ExpandableText
import com.forknowledge.feature.model.Ingredient
import com.forknowledge.feature.model.Measure
import com.forknowledge.feature.recipe.type.MeasureType
import com.forknowledge.feature.recipe.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientTabContent(
    summary: String,
    originalServings: Int,
    ingredients: List<Ingredient>
) {
    val sheetState = rememberModalBottomSheetState()
    var isBottomSheetVisible by remember { mutableStateOf(false) }
    var measureType by remember { mutableStateOf(MeasureType.ORIGINAL) }
    var servings by remember { mutableIntStateOf(originalServings) }

    if (isBottomSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { isBottomSheetVisible = false },
            sheetState = sheetState,
            containerColor = White,
            content = {
                MeasureBottomSheet(
                    currentMeasure = measureType,
                    onMeasureChanged = {
                        measureType = it
                        isBottomSheetVisible = false
                    }
                )
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        ExpandableText(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth(),
            text = summary.htmlToPlainText(),
            textStyle = Typography.bodyMedium,
            color = Grey8A949F
        )

        Row(
            modifier = Modifier.padding(
                top = 16.dp,
                bottom = 8.dp
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                enabled = servings > 1,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Black374957
                ),
                onClick = { servings-- }
            ) {
                Icon(
                    painter = painterResource(id = drawable.ic_reduce_solid),
                    contentDescription = null
                )
            }

            AppText(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = pluralStringResource(
                    R.plurals.recipe_ingredients_servings,
                    servings,
                    servings
                ),
                color = Grey8A949F,
                textStyle = Typography.bodyMedium
            )

            IconButton(
                onClick = { servings++ }
            ) {
                Icon(
                    painter = painterResource(id = drawable.ic_add_solid),
                    tint = Black374957,
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            AppText(
                modifier = Modifier
                    .background(
                        color = White,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        width = 1.dp,
                        color = GreyDADADA,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable { isBottomSheetVisible = true }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                text = measureType.title,
                textStyle = Typography.labelSmall
            )
        }

        ingredients.forEach { ingredient ->
            val measures = when (measureType) {
                MeasureType.ORIGINAL -> {
                    val amountPerServing = ingredient.originalMeasures.amount / originalServings
                    ingredient.originalMeasures.copy(amount = amountPerServing * servings)
                }

                MeasureType.METRIC -> {
                    val amountPerServing = ingredient.metricMeasures.amount / originalServings
                    ingredient.metricMeasures.copy(amount = amountPerServing * servings)
                }

                MeasureType.US -> {
                    val amountPerServing = ingredient.usMeasures.amount / originalServings
                    ingredient.usMeasures.copy(amount = amountPerServing * servings)
                }
            }
            val formattedAmount = measures.amount.toFormattedNumber()
            val ingredientAmount = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(fontWeight = FontWeight.Bold)
                ) {
                    append("$formattedAmount ${measures.unit} ")
                }
                append(ingredient.ingredientName)
            }

            Row(
                modifier = Modifier.padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                AsyncImage(
                    modifier = Modifier.size(50.dp),
                    model = ingredient.imageUrl,
                    placeholder = painterResource(id = R.drawable.img_ingredient_error),
                    error = painterResource(id = R.drawable.img_ingredient_error),
                    contentDescription = null
                )

                Text(
                    modifier = Modifier.padding(start = 24.dp),
                    text = ingredientAmount
                )
            }
        }
    }
}

@Composable
fun MeasureBottomSheet(
    currentMeasure: MeasureType,
    onMeasureChanged: (MeasureType) -> Unit
) {
    val radioOptions = MeasureType.entries
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(currentMeasure) }

    Column(
        modifier = Modifier
            .selectableGroup()
            .fillMaxWidth()
            .padding(bottom = 32.dp)
    ) {
        radioOptions.forEach { measure ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .selectable(
                        selected = (measure == selectedOption),
                        onClick = {
                            onOptionSelected(measure)
                            onMeasureChanged(measure)
                        },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 32.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AppText(
                    text = measure.title,
                    textStyle = Typography.titleSmall
                )

                RadioButton(
                    selected = (measure == selectedOption),
                    colors = RadioButtonDefaults.colors(
                        selectedColor = GreenA1CE50,
                        unselectedColor = Grey8A949F
                    ),
                    onClick = null
                )
            }
        }
    }
}

@Preview
@Composable
fun IngredientTabContentPreview() {
    IngredientTabContent(
        summary = "Watching your figure? This gluten free, dairy free, pescatarian, and ketogenic recipe has <b>505 calories</b>, <b>27g of protein</b>, and <b>40g of fat</b> per serving. For <b>$3.15 per serving</b>, this recipe <b>covers 21%</b> of your daily requirements of vitamins and minerals.",
        originalServings = 4,
        ingredients = listOf(
            Ingredient(
                ingredientId = 1,
                ingredientName = " Tomato",
                imageUrl = "",
                originalMeasures = Measure(amount = 2f, unit = "pieces"),
                usMeasures = Measure(amount = 2f, unit = "pieces"),
                metricMeasures = Measure(amount = 2f, unit = "pieces"),
            ),
            Ingredient(
                ingredientId = 2,
                ingredientName = " Tomato",
                imageUrl = "",
                originalMeasures = Measure(amount = 2f, unit = "pieces"),
                usMeasures = Measure(amount = 2f, unit = "pieces"),
                metricMeasures = Measure(amount = 2f, unit = "pieces"),
            ),
        )
    )
}

@Preview(showBackground = true)
@Composable
fun MeasureBottomSheetPreview() {
    MeasureBottomSheet(
        currentMeasure = MeasureType.ORIGINAL,
        onMeasureChanged = {}
    )
}
