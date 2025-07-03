package com.forknowledge.feature.planner.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil3.compose.AsyncImage
import com.forknowledge.core.ui.R.drawable
import com.forknowledge.core.ui.theme.Black063336
import com.forknowledge.core.ui.theme.Black374957
import com.forknowledge.core.ui.theme.Grey808993
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.core.ui.theme.component.LoadingIndicator
import com.forknowledge.feature.model.MealRecipe
import com.forknowledge.feature.planner.R
import com.forknowledge.feature.planner.SheetAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealItem(
    recipe: MealRecipe,
    showLoading: Boolean,
    onRecipeClick: (Int) -> Unit,
    onDeleteRecipe: () -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    if (showBottomSheet) {
        ModalBottomSheet(
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.background,
            onDismissRequest = { showBottomSheet = false }
        ) {
            ActionBottomSheet(
                actions = listOf(
                    SheetAction(
                        label = stringResource(R.string.meal_planner_bottom_sheet_delete_recipe_label),
                        icon = painterResource(drawable.ic_delete),
                        action = {
                            showBottomSheet = false
                            onDeleteRecipe()
                        }
                    )
                )
            )
        }
    }

    ConstraintLayout(
        modifier = Modifier
            .padding(top = 12.dp)
            .fillMaxWidth()
            .graphicsLayer {
                shape = RoundedCornerShape(8.dp)
                shadowElevation = 3.dp.toPx()
                spotShadowColor = Black063336
                clip = true
            }
            .background(
                color = White,
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.background)
            .clickable { onRecipeClick(recipe.recipeId) }

    ) {
        val (recipeImage, recipeName, cookTime, servings, actionIcon) = createRefs()

        AsyncImage(
            modifier = Modifier
                .size(
                    width = 130.dp,
                    height = 110.dp
                )
                .background(
                    color = Color.Unspecified,
                    shape = RoundedCornerShape(8.dp)
                )
                .clip(RoundedCornerShape(8.dp))
                .constrainAs(recipeImage) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                },
            model = recipe.imageUrl,
            placeholder = painterResource(drawable.img_vector_loading),
            error = painterResource(drawable.img_vector_loading),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )

        AppText(
            modifier = Modifier
                .padding(top = 4.dp)
                .widthIn(max = 190.dp)
                .constrainAs(recipeName) {
                    top.linkTo(recipeImage.top, margin = 4.dp)
                    start.linkTo(recipeImage.end, margin = 16.dp)
                    end.linkTo(parent.end)
                    horizontalBias = 0F
                },
            text = recipe.name,
            textStyle = Typography.labelMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        AppText(
            modifier = Modifier
                .constrainAs(cookTime) {
                    top.linkTo(recipeName.bottom)
                    bottom.linkTo(recipeImage.bottom, margin = 4.dp)
                    start.linkTo(recipeImage.end, margin = 16.dp)
                    end.linkTo(actionIcon.start)
                    horizontalBias = 0F
                },
            text = pluralStringResource(
                R.plurals.meal_planner_recipe_cook_time_text,
                recipe.cookTime,
                recipe.cookTime
            ),
            textStyle = Typography.bodySmall,
            color = Grey808993
        )

        AppText(
            modifier = Modifier
                .widthIn(max = 230.dp)
                .constrainAs(servings) {
                    top.linkTo(recipeName.bottom)
                    bottom.linkTo(recipeImage.bottom, margin = 4.dp)
                    start.linkTo(cookTime.end, margin = 24.dp)
                    end.linkTo(actionIcon.start)
                    horizontalBias = 0F
                },
            text = pluralStringResource(
                R.plurals.meal_planner_recipe_serving_text,
                recipe.servings,
                recipe.servings
            ),
            textStyle = Typography.bodySmall,
            color = Grey808993
        )

        if (showLoading) {
            LoadingIndicator(
                modifier = Modifier
                    .size(28.dp)
                    .clickable { showBottomSheet = true }
                    .constrainAs(actionIcon) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end, margin = 12.dp)
                    },
                strokeWidth = 3.dp
            )
        } else {
            IconButton(
                modifier = Modifier
                    .size(28.dp)
                    .constrainAs(actionIcon) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end, margin = 12.dp)
                    },
                onClick = { showBottomSheet = true }
            ) {
                Icon(
                    painter = painterResource(drawable.ic_options),
                    tint = Black374957,
                    contentDescription = null
                )
            }
        }
    }
}

@Preview()
@Composable
fun MealItemPreview() {
    MealItem(
        recipe = MealRecipe(
            mealId = 1,
            recipeId = 1,
            imageUrl = "",
            name = "Spaghetti Bolognese ".repeat(3),
            cookTime = 30,
            servings = 4
        ),
        showLoading = true,
        onRecipeClick = {},
        onDeleteRecipe = {}
    )
}
