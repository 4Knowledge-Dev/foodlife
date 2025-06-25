package com.forknowledge.core.ui.theme.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forknowledge.core.ui.theme.Grey808993
import com.forknowledge.core.ui.theme.GreyB7BDC4
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.openSansFamily

@Composable
fun AppText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = Color.Black,
    textAlign: TextAlign = TextAlign.Start,
    textStyle: TextStyle = Typography.bodyMedium,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    onTextLayout: (TextLayoutResult) -> Unit = {}
) {

    Text(
        modifier = modifier.wrapContentHeight(),
        fontFamily = openSansFamily,
        text = text,
        color = color,
        textAlign = textAlign,
        style = textStyle,
        maxLines = maxLines,
        overflow = overflow,
        onTextLayout = onTextLayout
    )
}

@Composable
fun AppTextButton(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = Color.Black,
    textAlign: TextAlign = TextAlign.Center,
    textStyle: TextStyle = Typography.bodyMedium,
    onClick: () -> Unit
) {
    TextButton(
        modifier = modifier,
        onClick = onClick
    ) {
        AppText(
            text = text,
            color = color,
            textAlign = textAlign,
            textStyle = textStyle,
            maxLines = 1
        )
    }
}

@Composable
fun ExpandableText(
    modifier: Modifier = Modifier,
    text: String,
    collapsedMaxLines: Int = 2,
    textStyle: TextStyle = Typography.bodyMedium,
    color: Color = Color.Black,
    textButtonMore: String = "More",
    textButtonLess: String = "Less"
) {
    var isExpanded by remember { mutableStateOf(false) }
    var hasOverFlow by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        AppText(
            text = text,
            color = color,
            textStyle = textStyle,
            maxLines = if (isExpanded) Int.MAX_VALUE else collapsedMaxLines,
            overflow = if (isExpanded) TextOverflow.Visible else TextOverflow.Ellipsis,
            onTextLayout = { textLayoutResult ->
                hasOverFlow = textLayoutResult.hasVisualOverflow
            }
        )

        if (isExpanded || hasOverFlow) {
            AppText(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clickable { isExpanded = !isExpanded },
                text = if (isExpanded) textButtonLess else textButtonMore,
                textStyle = textStyle.copy(
                    fontWeight = FontWeight.W600,
                    textDecoration = TextDecoration.Underline
                ),
                color = Grey808993
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun AppTextButtonPreview() {
    AppTextButton(
        text = "Hello World",
        onClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun ExpandableTextPreview() {
    ExpandableText(
        text = "When Google revealed the first version of their Android OS over a decade ago, they adopted Java as the main language for Android application development But why Java? As one of the oldest object-oriented languages, Java is easy to learn and it works well on the Dalvik virtual machine, which was inspired by Java Virtual Machine (JVM), making it portable for almost any device and operating system. So, when Google began building the Android system, Java was one of the most suitable languages."
    )
}
