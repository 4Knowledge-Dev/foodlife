package com.forknowledge.core.common.extension

import android.util.Patterns
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextDecoration

/**
 * Convert HTML text to plain text.
 * @return the plain text.
 */
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

/**
 * Format a float number to integer type if fractional part is 0.
 * For example: 1.0 -> 1.
 * return the formatted number.
 */
fun Float.toFormattedNumber(): String {
    return if (this % 1 == 0f) {
        this.toInt().toString()
    } else {
        this.toString()
    }
}

/**
 * Check if a string is a valid URL format.
 * @return true if the string is a valid URL format, false otherwise.
 */
fun String.isUrlFormat(): Boolean {
    return Patterns.WEB_URL.matcher(this).matches()
}
