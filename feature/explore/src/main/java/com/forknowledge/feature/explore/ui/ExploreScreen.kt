package com.forknowledge.feature.explore.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forknowledge.core.data.datasource.ingredients
import com.forknowledge.core.ui.theme.Black101510
import com.forknowledge.core.ui.theme.Green86BF3E
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.component.AppSearchBar
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.core.ui.theme.component.FlowRowItem
import com.forknowledge.feature.explore.DietType
import com.forknowledge.feature.explore.R

@Composable
fun ExploreScreen(
    isAddMealPlanProcess: Boolean,
    onNavigateToSearch: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White)
                    .padding(
                        top = 16.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppSearchBar(
                    modifier = Modifier.weight(0.8f),
                    placeholder = stringResource(R.string.explore_search_bar_placeholder),
                    onClicked = { onNavigateToSearch() }
                )

                if (isAddMealPlanProcess) {
                    AppText(
                        modifier = Modifier
                            .weight(0.2f)
                            .clickable { onNavigateBack() },
                        text = stringResource(R.string.explore_cancel_text),
                        textStyle = Typography.labelMedium,
                        color = Green86BF3E,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            AppText(
                modifier = Modifier
                    .padding(
                        top = 24.dp,
                        start = 16.dp
                    ),
                text = stringResource(R.string.explore_diet_type_section_title),
                textStyle = Typography.titleSmall,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(
                        top = 8.dp,
                        start = 12.dp,
                        end = 12.dp
                    )
            ) {
                DietType.entries.forEach { diet ->
                    DietItem(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        diet = stringResource(diet.title),
                        image = diet.icon
                    )
                }
            }

            AppText(
                modifier = Modifier
                    .padding(
                        top = 24.dp,
                        start = 16.dp
                    ),
                text = stringResource(R.string.explore_ingredient_section_title),
                textStyle = Typography.titleSmall,
            )

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(
                        top = 8.dp,
                        start = 10.dp,
                        end = 16.dp
                    ),
                maxItemsInEachRow = 5,
                maxLines = 3
            ) {
                ingredients.forEach { ingredient ->
                    FlowRowItem(
                        item = ingredient,
                        onItemSelected = {}
                    )
                }
            }
        }
    }
}

@Composable
fun DietItem(
    modifier: Modifier = Modifier,
    diet: String,
    image: Int,
    onClicked: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .size(100.dp)
            .background(
                color = Color.Unspecified,
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
            .clickable {},
        contentAlignment = Alignment.BottomCenter
    ) {
        Image(
            painter = painterResource(id = image),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )

        AppText(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(color = Black101510.copy(alpha = 0.3f)),
            text = diet,
            textStyle = Typography.labelSmall,
            textAlign = TextAlign.Center,
            color = White
        )
    }
}

@Preview
@Composable
fun DietItemPreview() {
    DietItem(
        diet = "High Protein",
        image = R.drawable.img_diet_high_protein
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun ExploreScreenPreview() {
    ExploreScreen(
        isAddMealPlanProcess = true,
        onNavigateToSearch = {},
        onNavigateBack = {}
    )
}
