package com.forknowledge.core.ui.theme.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forknowledge.core.ui.R
import com.forknowledge.core.ui.theme.Green91C747
import com.forknowledge.core.ui.theme.GreyEBEBEB
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.state.FloatingAction

@Composable
fun AppFloatingActionButtonSelection(
    modifier: Modifier = Modifier,
    isExpanded: Boolean = false,
    actions: List<FloatingAction> = emptyList(),
    onClick: () -> Unit
) {
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 135f else 0f,
        animationSpec = tween(300)
    )

    Column(
        modifier = modifier
            .padding(
                bottom = 48.dp,
                end = 24.dp
            ),
        horizontalAlignment = Alignment.End
    ) {
        if (actions.isNotEmpty()) {
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 })
            ) {
                Column(
                    modifier = Modifier.padding(bottom = 50.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    actions.forEach { action ->
                        FloatingActionSection(
                            label = action.label,
                            icon = action.icon,
                            onClick = action.action
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            modifier = modifier.size(64.dp),
            onClick = onClick,
            shape = CircleShape,
            containerColor = Green91C747,
        ) {
            Icon(
                modifier = Modifier
                    .size(36.dp)
                    .rotate(rotationAngle),
                painter = painterResource(id = R.drawable.ic_add),
                tint = White,
                contentDescription = null
            )
        }
    }
}

@Composable
fun AppFloatingActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    FloatingActionButton(
        modifier = modifier
            .padding(
                bottom = 48.dp,
                end = 24.dp
            )
            .size(64.dp),
        onClick = onClick,
        shape = CircleShape,
        containerColor = Green91C747,
    ) {
        Icon(
            modifier = Modifier.size(36.dp),
            painter = painterResource(id = R.drawable.ic_add),
            tint = White,
            contentDescription = null
        )
    }
}

@Composable
fun FloatingActionSection(
    label: String,
    icon: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(top = 16.dp)
            .background(
                color = Color.Unspecified,
                shape = RoundedCornerShape(21.dp)
            )
            .clip(RoundedCornerShape(21.dp))
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AppText(
            modifier = Modifier
                .background(
                    color = GreyEBEBEB,
                    shape = RoundedCornerShape(21.dp)
                )
                .padding(8.dp),
            text = label,
            textStyle = Typography.bodyMedium
        )

        Image(
            modifier = Modifier
                .padding(start = 16.dp)
                .background(
                    color = GreyEBEBEB,
                    shape = CircleShape
                )
                .padding(4.dp)
                .size(32.dp),
            painter = painterResource(id = icon),
            contentDescription = null
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun AppFloatingButtonPreview() {
    AppFloatingActionButtonSelection(
        isExpanded = false,
        onClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun FloatingActionSectionPreview() {
    FloatingActionSection(
        label = "Breakfast",
        icon = R.drawable.img_breakfast,
        onClick = {}
    )
}
