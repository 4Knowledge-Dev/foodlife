package com.forknowledge.core.ui.theme.component

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.forknowledge.core.ui.theme.GreyB7BDC4
import com.forknowledge.core.ui.theme.Typography
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.math.abs
import kotlin.math.max

const val WHEEL_SCROLL_DEBOUNCE = 300L

inline val Dp.px: Float
    @Composable get() = with(LocalDensity.current) { this@px.toPx() }

@OptIn(ExperimentalFoundationApi::class, FlowPreview::class)
@Composable
fun WheelColumn(
    modifier: Modifier = Modifier,
    value: Int,
    items: List<Int>,
    visibleItems: Int = 3,
    onCenterItemChanged: (value: Int) -> Unit,
) {

    val listState = rememberLazyListState()

    val centerIndex by remember {
        derivedStateOf { listState.firstVisibleItemIndex + visibleItems / 2 }
    }

    val flingBehavior = rememberSnapFlingBehavior(listState)

    LaunchedEffect(items) {
        listState.scrollToItem(items.indexOf(value) - visibleItems / 2)
    }

    LaunchedEffect(items) {
        snapshotFlow { items[centerIndex] }
            .debounce(WHEEL_SCROLL_DEBOUNCE)
            .onEach { newValue ->
                onCenterItemChanged(newValue)
            }
            .launchIn(this)
    }

    val itemHeight = 50.dp
    val wheelHeight = itemHeight * visibleItems

    Box(
        modifier = modifier.height(wheelHeight),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(itemHeight),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, GreyB7BDC4),
            color = Color.Transparent,
            content = {}
        )

        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior
        ) {
            itemsIndexed(items) { index, item ->
                val distanceFromCenter = index - centerIndex
                val alphaVal =
                    if (index < (visibleItems / 2) || index > items.size - (visibleItems / 2) - 1) {
                        0f
                    } else {
                        max(0.3f, 1f - abs(distanceFromCenter) * 0.5f)
                    }
                val angle = distanceFromCenter * 20f
                val textRotationX = when {
                    index == centerIndex -> 0f
                    else -> angle
                }

                AppText(
                    modifier = Modifier
                        .height(itemHeight)
                        .fillMaxWidth()
                        .graphicsLayer {
                            rotationX = textRotationX
                            alpha = alphaVal
                        },
                    text = item.toString(),
                    textAlign = TextAlign.Center,
                    textStyle = Typography.titleSmall
                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun WheelColumnPreview() {
    WheelColumn(
        value = 5,
        items = (1..10).toList(),
        onCenterItemChanged = {}
    )
}
