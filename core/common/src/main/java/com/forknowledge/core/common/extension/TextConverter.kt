package com.forknowledge.core.common.extension

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextDecoration

fun String.htmlToPlainText(): String {
    return AnnotatedString.fromHtml(
        this.trimIndent(),
        TextLinkStyles(
            style = SpanStyle(
                textDecoration = TextDecoration.Underline,
                fontStyle = FontStyle.Italic,
                color = Color.Blue
            )
        )
    ).text
}

fun Float.toFormattedNumber(): String {
    return if (this % 1 == 0f) {
        this.toInt().toString()
    } else {
        this.toString()
    }
}
