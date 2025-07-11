package com.forknowledge.core.ui.theme.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.forknowledge.core.ui.R
import com.forknowledge.core.ui.theme.Black05172C
import com.forknowledge.core.ui.theme.Green91C747
import com.forknowledge.core.ui.theme.buttonTextStyle
import com.forknowledge.core.ui.theme.openSansFamily

@Composable
fun AppButton(
    modifier: Modifier = Modifier,
    buttonColor: Color = Black05172C,
    borderStroke: BorderStroke? = null,
    textStyle: TextStyle = buttonTextStyle,
    enabled: Boolean = true,
    buttonText: String,
    textColor: Color = White,
    icon: Int? = null,
    iconTint: Color = White,
    isNextButton: Boolean = true,
    onClicked: () -> Unit
) {
    Button(
        modifier = modifier.clip(RoundedCornerShape(50.dp)),
        colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
        border = borderStroke,
        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 10.dp),
        enabled = enabled,
        onClick = onClicked
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (isNextButton) {
                Text(
                    text = buttonText,
                    color = textColor,
                    style = textStyle,
                    textAlign = TextAlign.Center,
                )

                icon?.let { icon ->
                    Icon(
                        modifier = Modifier.padding(start = 10.dp),
                        painter = painterResource(id = icon),
                        tint = iconTint,
                        contentDescription = null
                    )
                }
            } else {
                icon?.let { icon ->
                    Icon(
                        painter = painterResource(id = icon),
                        tint = iconTint,
                        contentDescription = null
                    )
                }

                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = buttonText,
                    color = textColor,
                    style = textStyle,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
fun AppButtonLoading(
    modifier: Modifier = Modifier,
    buttonColor: Color = Black05172C
) {
    Button(
        modifier = modifier.clip(RoundedCornerShape(50.dp)),
        colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
        onClick = { /* Do no implement */ }
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(20.dp),
            color = White,
            strokeWidth = 2.dp
        )
    }
}

@Composable
fun AppButtonSmall(
    modifier: Modifier = Modifier,
    text: String,
    @DrawableRes trailingIcon: Int? = null,
    onClicked: () -> Unit
) {
    Button(
        modifier = modifier
            .height(100.dp)
            .padding(bottom = 50.dp),
        onClick = onClicked,
        colors = ButtonDefaults.buttonColors(
            containerColor = Green91C747
        )
    ) {
        Row {
            AppText(
                text = text,
                color = White,
                textStyle = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )

            trailingIcon?.let { icon ->
                Icon(
                    modifier = Modifier.padding(start = 10.dp),
                    painter = painterResource(id = icon),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun AppButtonSmallLoading(
    modifier: Modifier = Modifier,
    text: String
) {
    Button(
        modifier = modifier
            .height(100.dp)
            .padding(bottom = 50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Green91C747
        ),
        onClick = { /* Do no implement */ }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AppText(
                text = text,
                color = White,
                textStyle = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )

            CircularProgressIndicator(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(20.dp),
                color = White,
                strokeWidth = 2.dp
            )
        }
    }
}

@Preview
@Composable
fun AppButtonPreview() {
    AppButton(
        modifier = Modifier.fillMaxWidth(),
        buttonText = "Done",
        textStyle = TextStyle(
            fontFamily = openSansFamily,
            fontWeight = FontWeight.SemiBold
        ),
        icon = R.drawable.ic_arrow_next,
        onClicked = { }
    )
}

@Preview
@Composable
fun AppButtonLoadingPreview() {
    AppButtonLoading(
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth()
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun AppButtonSmallPreview() {
    AppButtonSmall(
        text = "Done",
        trailingIcon = R.drawable.ic_arrow_next,
        onClicked = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun AppButtonSmallLoadingPreview() {
    AppButtonSmallLoading(
        text = "Done"
    )
}
