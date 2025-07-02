package com.forknowledge.feature.nutrient.statistics.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forknowledge.core.ui.R.drawable
import com.forknowledge.core.ui.theme.Black374957
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.component.AppText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutrientGroupTopBar(
    title: String,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    painter = painterResource(id = drawable.ic_back),
                    tint = Black374957,
                    contentDescription = null
                )
            }

            AppText(
                modifier = Modifier.padding(start = 24.dp),
                text = title,
                textStyle = Typography.titleSmall
            )
        }
    }
}

@Preview
@Composable
fun SelectNutrientTopBarPreview() {
    NutrientGroupTopBar(
        title = "Protein",
        onNavigateBack = {}
    )
}
