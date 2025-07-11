package com.forknowledge.core.ui.theme.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.forknowledge.core.ui.theme.Black05172C
import com.forknowledge.core.ui.theme.GreyB7BDC4

@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier,
    size: Dp = 60.dp,
    strokeWidth: Dp = 5.dp,
    color: Color = Black05172C
) {
    CircularProgressIndicator(
        modifier = modifier.size(size),
        color = color,
        strokeWidth = strokeWidth
    )
}

@Composable
fun LoadingIndicatorBox(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(100.dp)
            .background(
                color = GreyB7BDC4.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        LoadingIndicator()
    }
}

@Composable
fun LoadingIndicatorOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.1f))
            .clickable(enabled = false, onClick = {})
            .zIndex(1f),
        contentAlignment = Alignment.Center
    ) {
        LoadingIndicator()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun LoadingIndicatorPreview() {
    LoadingIndicatorBox()
}
