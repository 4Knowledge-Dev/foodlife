package com.forknowledge.feature.nutrient

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
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
import com.forknowledge.core.ui.theme.Black374957
import com.forknowledge.core.ui.theme.Grey808993
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.feature.model.Nutrient
import com.forknowledge.feature.model.Recipe

@Composable
fun RecipeItem(recipe: Recipe) {
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
        val (recipeImage, icon, recipeName, recipeAmount) = createRefs()

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
            painter = painterResource(R.drawable.img_sample),
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
                recipe.nutrients[0]
            ),
            textStyle = Typography.bodySmall,
            color = Grey808993
        )

        IconButton(
            modifier = Modifier
                .constrainAs(icon) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end, margin = 16.dp)
                },
            onClick = { }
        ) {
            Icon(
                modifier = Modifier.size(28.dp),
                painter = painterResource(R.drawable.ic_add_solid),
                tint = Black374957,
                contentDescription = null
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun RecipeItemPreview() {
    RecipeItem(
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
        )
    )
}
