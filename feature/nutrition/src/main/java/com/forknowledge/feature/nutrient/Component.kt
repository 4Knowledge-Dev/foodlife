package com.forknowledge.feature.nutrient

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.forknowledge.core.common.Result
import com.forknowledge.core.ui.theme.Black374957
import com.forknowledge.core.ui.theme.Grey808993
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.core.ui.theme.component.LoadingIndicator
import com.forknowledge.feature.model.Nutrient
import com.forknowledge.feature.model.Recipe
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlin.math.roundToInt
import com.forknowledge.core.ui.R.drawable

const val ANIMATION_DURATION = 1000L

@Composable
fun RecipeItem(
    result: Result<Unit>?,
    logRecipeId: Long?,
    recipe: Recipe,
    onLogRecipe: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = White)
            .clickable {}
            .padding(
                vertical = 12.dp,
                horizontal = 16.dp
            )
    ) {
        val (recipeImage, icon, recipeName, recipeAmount, loading) = createRefs()
        val endGuideLine = createGuidelineFromEnd(0.05F)

        Image(
            modifier = Modifier
                .size(60.dp)
                .background(
                    color = Color.Unspecified,
                    shape = CircleShape
                )
                .clip(CircleShape)
                .constrainAs(recipeImage) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                },
            painter = painterResource(drawable.img_sample),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )

        AppText(
            modifier = Modifier.constrainAs(recipeName) {
                top.linkTo(recipeImage.top, margin = 4.dp)
                bottom.linkTo(recipeAmount.top)
                start.linkTo(recipeImage.end, margin = 16.dp)
                end.linkTo(icon.start)
                horizontalBias = 0F
            },
            text = recipe.name,
            textStyle = Typography.labelMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        AppText(
            modifier = Modifier.constrainAs(recipeAmount) {
                top.linkTo(recipeName.bottom)
                bottom.linkTo(recipeImage.bottom, margin = 4.dp)
                start.linkTo(recipeImage.end, margin = 16.dp)
                end.linkTo(icon.start)
                horizontalBias = 0F
            },
            text = stringResource(
                R.string.nutrient_log_food_food_item_amount,
                recipe.healthScore,
                recipe.nutrients[0].amount.roundToInt()
            ),
            textStyle = Typography.bodySmall,
            color = Grey808993
        )

        val successComposition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.anim_success)
        )
        val failComposition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.anim_fail)
        )
        val progress by animateLottieCompositionAsState(failComposition)
        var isAnimatedVisible by remember { mutableStateOf(false) }

        LaunchedEffect(result) {
            snapshotFlow { result }
                .map { it !is Result.Loading }
                .distinctUntilChanged()
                .collect {
                    isAnimatedVisible = true
                    delay(ANIMATION_DURATION)
                    isAnimatedVisible = false
                }
        }

        if (result !is Result.Loading && !isAnimatedVisible) {
            Icon(
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onLogRecipe() }
                    .constrainAs(icon) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(endGuideLine)
                    },
                painter = painterResource(drawable.ic_add_solid),
                tint = Black374957,
                contentDescription = null
            )
        }

        if (recipe.id == logRecipeId) {
            when (result) {
                is Result.Loading -> {
                    LoadingIndicator(
                        modifier = Modifier
                            .constrainAs(loading) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                end.linkTo(endGuideLine)
                            },
                        size = 28.dp,
                        strokeWidth = 2.dp
                    )
                }

                is Result.Success -> {
                    if (isAnimatedVisible) {
                        LottieAnimation(
                            modifier = Modifier
                                .size(28.dp)
                                .constrainAs(loading) {
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                    end.linkTo(endGuideLine)
                                },
                            composition = successComposition,
                            progress = { progress },
                        )
                    }
                }

                is Result.Error -> {
                    if (isAnimatedVisible) {
                        LottieAnimation(
                            modifier = Modifier
                                .size(28.dp)
                                .constrainAs(loading) {
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                    end.linkTo(endGuideLine)
                                },
                            composition = failComposition,
                            progress = { progress },
                        )
                    }
                }

                else -> Unit
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun RecipeItemPreview() {
    RecipeItem(
        result = Result.Loading,
        logRecipeId = 1L,
        recipe = Recipe(
            name = "Spaghetti Bolognese",
            healthScore = 100,
            nutrients = listOf(
                Nutrient(
                    name = "Calories",
                    amount = 1200.0,
                    unit = "kcal",
                )
            )
        ),
        onLogRecipe = {}
    )
}
