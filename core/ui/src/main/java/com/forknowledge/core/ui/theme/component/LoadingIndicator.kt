package com.forknowledge.core.ui.theme.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forknowledge.core.ui.theme.Black05172C

@Composable
fun LoadingIndicator() {
    CircularProgressIndicator(
        modifier = Modifier.size(60.dp),
        color = Black05172C,
        strokeWidth = 5.dp
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun LoadingIndicatorPreview() {
    LoadingIndicator()
}
