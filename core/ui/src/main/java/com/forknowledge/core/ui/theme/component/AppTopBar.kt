package com.forknowledge.core.ui.theme.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forknowledge.core.ui.R.drawable
import com.forknowledge.core.ui.theme.Black374957
import com.forknowledge.core.ui.theme.Typography

/**
 * [title] the title of TopAppBar.
 * [isCenterAligned] whether title is center aligned or not.
 * [scrollBehavior] the scroll behavior of TopAppBar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    title: String,
    isCenterAligned: Boolean = true,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onNavigateBack: () -> Unit
) {
    if (isCenterAligned) {
        CenterAlignedTopAppBar(
            modifier = modifier.fillMaxWidth(),
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                scrolledContainerColor = MaterialTheme.colorScheme.background,
            ),
            title = {
                AppText(
                    text = title,
                    textStyle = Typography.labelLarge,
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = { onNavigateBack() }
                ) {
                    Icon(
                        painter = painterResource(drawable.ic_back),
                        tint = Black374957,
                        contentDescription = null
                    )
                }
            },
            scrollBehavior = scrollBehavior
        )
    } else {
        TopAppBar(
            modifier = modifier.fillMaxWidth(),
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                scrolledContainerColor = MaterialTheme.colorScheme.background,
            ),
            title = {
                AppText(
                    modifier = Modifier.padding(start = 16.dp),
                    text = title,
                    textStyle = Typography.labelLarge,
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = { onNavigateBack() }
                ) {
                    Icon(
                        painter = painterResource(drawable.ic_back),
                        tint = Black374957,
                        contentDescription = null
                    )
                }
            },
            scrollBehavior = scrollBehavior
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun AppTopBarPreview() {
    AppTopBar(
        title = "Top bar",
        onNavigateBack = {}
    )
}
