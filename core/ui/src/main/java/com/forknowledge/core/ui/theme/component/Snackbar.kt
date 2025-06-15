package com.forknowledge.core.ui.theme.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forknowledge.core.ui.theme.Black212121
import com.forknowledge.core.ui.theme.GreenA1CE50
import com.forknowledge.core.ui.theme.RedF44336
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.state.SnackBarState

@Composable
fun AppSnackBar(
    modifier: Modifier = Modifier,
    message: String,
    state: SnackBarState = SnackBarState.NONE,
    actionLabel: String? = null,
    onAction: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(45.dp)
            .padding(horizontal = 16.dp)
            .background(
                color = when (state) {
                    SnackBarState.SUCCESS -> GreenA1CE50
                    SnackBarState.FAILURE -> RedF44336
                    SnackBarState.NONE -> Black212121
                },
                shape = RoundedCornerShape(4.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AppText(
            modifier = Modifier.weight(0.7f),
            text = message,
            textStyle = Typography.bodyMedium,
            color = White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        actionLabel?.let { label ->
            TextButton(
                modifier = Modifier.weight(0.3f),
                onClick = onAction
            ) {
                AppText(
                    text = label,
                    textStyle = Typography.labelMedium,
                    color = White
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun AppSnackBarPreview() {
    AppSnackBar(
        message = "Something went wrong ".repeat(3),
        actionLabel = "Done"
    )
}
