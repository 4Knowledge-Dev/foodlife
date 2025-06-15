package com.forknowledge.core.ui.theme.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forknowledge.core.ui.theme.Green91C747
import com.forknowledge.core.ui.theme.GreyB7BDC4

@Composable
fun FlowRowItem(
    item: String,
    isSelected: Boolean = false,
    onItemSelected: (item: String) -> Unit
) {
    AppText(
        modifier = Modifier
            .padding(
                start = 6.dp,
                top = 6.dp
            )
            .widthIn(min = 80.dp)
            .height(40.dp)
            .background(
                color = if (isSelected) {
                    Green91C747
                } else {
                    White
                },
                shape = RoundedCornerShape(32.dp)
            )
            .clip(RoundedCornerShape(32.dp))
            .border(
                width = 1.dp,
                color = GreyB7BDC4,
                shape = RoundedCornerShape(32.dp)
            )
            .clickable { onItemSelected(item) }
            .padding(
                //vertical = 8.dp,
                horizontal = 12.dp
            ),
        text = item.replaceFirst(
            item.first(),
            item.first().uppercaseChar(),
        ),
        color = if (isSelected) White else Black,
        textAlign = TextAlign.Center
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun FlowRowPreview() {
    FlowRowItem(
        item = "sugar",
        isSelected = true,
        onItemSelected = {}
    )
}
