package com.forknowledge.feature.recipe.screen.recipe

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil3.compose.AsyncImage
import com.forknowledge.core.ui.theme.Black374957
import com.forknowledge.core.ui.theme.Grey808993
import com.forknowledge.core.ui.theme.Grey8A949F
import com.forknowledge.core.ui.theme.GreyDADADA
import com.forknowledge.core.ui.theme.GreyEBEBEB
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.feature.model.Equipment
import com.forknowledge.feature.model.Ingredient
import com.forknowledge.feature.model.Step
import com.forknowledge.feature.recipe.R

@Composable
fun InstructionTabContent(
    sourceUrl: String,
    readyInMinutes: Int,
    prepTime: Int,
    cookTime: Int,
    steps: List<Step>
) {
    if (steps.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 24.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (readyInMinutes > 0) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_timer),
                        tint = Black374957,
                        contentDescription = null
                    )

                    AppText(
                        modifier = Modifier.padding(start = 8.dp),
                        text = stringResource(R.string.recipe_ready_in_minutes),
                        textStyle = Typography.bodyMedium,
                        color = Grey8A949F
                    )

                    AppText(
                        modifier = Modifier.padding(start = 4.dp),
                        text = pluralStringResource(
                            R.plurals.recipe_cook_time,
                            readyInMinutes,
                            readyInMinutes
                        )
                    )
                }
            }

            if (prepTime > 0 && cookTime > 0) {
                Row(
                    modifier = Modifier
                        .padding(vertical = 24.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_prep_time),
                            tint = Black374957,
                            contentDescription = null
                        )

                        AppText(
                            modifier = Modifier.padding(start = 8.dp),
                            text = stringResource(R.string.recipe_prep_time),
                            textStyle = Typography.bodyMedium,
                            color = Grey8A949F
                        )

                        AppText(
                            modifier = Modifier.padding(start = 4.dp),
                            text = pluralStringResource(
                                R.plurals.recipe_cook_time,
                                prepTime,
                                prepTime
                            )
                        )
                    }

                    VerticalDivider(
                        modifier = Modifier.height(25.dp),
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_cook_time),
                            tint = Black374957,
                            contentDescription = null
                        )

                        AppText(
                            modifier = Modifier.padding(start = 8.dp),
                            text = stringResource(R.string.recipe_cook_time),
                            textStyle = Typography.bodyMedium,
                            color = Grey8A949F
                        )

                        AppText(
                            modifier = Modifier.padding(start = 4.dp),
                            text = pluralStringResource(
                                R.plurals.recipe_cook_time,
                                cookTime,
                                cookTime
                            )
                        )
                    }
                }
            }

            StepSection(steps)
        }
    } else if (sourceUrl.isNotEmpty()) {
        AndroidView(
            factory = {
                WebView(it).apply {
                    webViewClient = WebViewClient()
                    loadUrl(sourceUrl)
                }
            }
        )
    } else {
        AppText(
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
            text = stringResource(R.string.recipe_instruction_no_instruction_label),
            textStyle = Typography.titleMedium,
            color = Grey808993,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun StepSection(
    steps: List<Step>
) {
    Column(
        modifier = Modifier
            .padding(top = 24.dp)
            .fillMaxWidth()
    ) {
        steps.forEachIndexed { index, step ->
            AppText(
                text = stringResource(R.string.recipe_instruction_step_number, step.stepNumber),
                textStyle = Typography.titleSmall,
            )

            AppText(
                modifier = Modifier.padding(top = 8.dp),
                text = step.description,
                textStyle = Typography.bodyLarge,
            )

            if (step.ingredients.isNotEmpty()) {
                AppText(
                    modifier = Modifier.padding(top = 8.dp),
                    text = stringResource(R.string.recipe_ingredients_tab_title),
                    textStyle = Typography.labelMedium,
                )

                FlowRow(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    for (ingredientIndex in step.ingredients.indices) {
                        val ingredient = step.ingredients[ingredientIndex]
                        Row(
                            modifier = Modifier
                                .background(
                                    color = GreyEBEBEB,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clip(RoundedCornerShape(16.dp))
                                .padding(
                                    start = 4.dp,
                                    end = 8.dp,
                                    top = 2.dp,
                                    bottom = 2.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .size(25.dp)
                                    .background(
                                        color = White,
                                        shape = CircleShape
                                    )
                                    .clip(CircleShape)
                                    .padding(2.dp),
                                model = ingredient.imageUrl,
                                placeholder = painterResource(R.drawable.img_ingredient_error),
                                error = painterResource(R.drawable.img_ingredient_error),
                                contentDescription = null
                            )

                            AppText(
                                modifier = Modifier.padding(start = 4.dp),
                                text = ingredient.ingredientName.replaceFirstChar { it.titlecase() },
                                textStyle = Typography.bodySmall,
                            )
                        }
                    }
                }
            }

            if (step.equipments.isNotEmpty()) {
                AppText(
                    modifier = Modifier.padding(top = 8.dp),
                    text = stringResource(R.string.recipe_instruction_equipment_header),
                    textStyle = Typography.labelMedium,
                )

                FlowRow(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    for (equipmentIndex in step.equipments.indices) {
                        val equipment = step.equipments[equipmentIndex]
                        Row(
                            modifier = Modifier
                                .background(
                                    color = GreyEBEBEB,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clip(RoundedCornerShape(16.dp))
                                .padding(
                                    start = 4.dp,
                                    end = 8.dp,
                                    top = 2.dp,
                                    bottom = 2.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .size(25.dp)
                                    .background(
                                        color = White,
                                        shape = CircleShape
                                    )
                                    .clip(CircleShape)
                                    .padding(2.dp),
                                model = equipment.imageUrl,
                                placeholder = painterResource(R.drawable.img_ingredient_error),
                                error = painterResource(R.drawable.img_ingredient_error),
                                //contentScale = ContentScale.Crop,
                                contentDescription = null
                            )

                            AppText(
                                modifier = Modifier.padding(start = 4.dp),
                                text = equipment.equipmentName.replaceFirstChar { it.titlecase() },
                                textStyle = Typography.bodySmall,
                            )
                        }
                    }
                }
            }

            if (index < steps.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 24.dp,
                            horizontal = 16.dp
                        ),
                    color = GreyDADADA,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StepSectionPreview() {
    StepSection(
        steps = listOf(
            Step(
                stepNumber = 1,
                description = "Preheat the oven to 350°F (175°C).",
                ingredients = List(4) {
                    Ingredient(
                        ingredientId = 1,
                        ingredientName = "baking soda",
                        imageUrl = "",
                    )
                },
                equipments = List(4) {
                    Equipment(
                        equipmentId = 1,
                        equipmentName = "Oven",
                        imageUrl = "",
                    )
                }
            )
        )
    )
}
