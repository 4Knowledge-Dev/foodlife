package com.forknowledge.core.ui.theme.component

import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.openSansFamily

@Composable
fun AppText(
    modifier: Modifier = Modifier,
    text: String = "",
    color: Color = Color.Black,
    textAlign: TextAlign = TextAlign.Start,
    textStyle: TextStyle = Typography.bodyMedium,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {

    Text(
        modifier = modifier.wrapContentHeight(),
        fontFamily = openSansFamily,
        text = text,
        color = color,
        textAlign = textAlign,
        style = textStyle,
        maxLines = maxLines,
        overflow = overflow
    )
}
