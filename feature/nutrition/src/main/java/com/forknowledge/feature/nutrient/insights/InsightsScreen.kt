package com.forknowledge.feature.nutrient.insights

import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.feature.nutrient.R
import com.forknowledge.core.ui.R.drawable
import com.forknowledge.core.ui.theme.Black374957

@Composable
fun InsightsScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            InsightsTopBar(onNavigateBack = onNavigateBack)
        }
    ) { innerPadding ->

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsTopBar(
    onNavigateBack: () -> Unit
) {
    TopAppBar(
        title = {
            AppText(
                text = stringResource(R.string.nutrient_insights_header),
                textStyle = Typography.titleMedium
            )
        },
        navigationIcon = {
            Icon(
                modifier = Modifier.clickable { onNavigateBack() },
                painter = painterResource(drawable.ic_back),
                tint = Black374957,
                contentDescription = null
            )
        },
        actions = {
            Icon(
                modifier = Modifier.clickable { },
                painter = painterResource(drawable.ic_chart),
                tint = Black374957,
                contentDescription = null
            )
        }
    )
}