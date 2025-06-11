package com.forknowledge.feature.onboarding.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.forknowledge.core.ui.R.drawable
import com.forknowledge.core.ui.theme.Green91C747
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.feature.onboarding.R

@Composable
fun WelcomeScreen(
    onNavigateToSurvey: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .drawWithContent {
                        val gradientHeightPx = size.height * 0.3F

                        drawContent()

                        drawRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, White),
                                startY = size.height - gradientHeightPx,
                                endY = size.height,
                            ),
                            topLeft = Offset(0f, size.height - gradientHeightPx),
                            size = Size(size.width, gradientHeightPx)
                        )
                    },
                painter = painterResource(id = R.drawable.img_welcome),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )

            AppText(
                modifier = Modifier
                    .align(alignment = Alignment.BottomCenter)
                    .fillMaxWidth(),
                text = stringResource(id = R.string.onboading_welcome_title),
                textStyle = Typography.headlineLarge,
                textAlign = TextAlign.Center
            )
        }

        AppText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 50.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
            text = stringResource(id = R.string.onboarding_welcome_text),
            textAlign = TextAlign.Center,
            textStyle = TextStyle(
                fontSize = 18.sp
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier
                .height(100.dp)
                .padding(bottom = 50.dp)
                .align(Alignment.CenterHorizontally),
            onClick = onNavigateToSurvey,
            colors = ButtonDefaults.buttonColors(
                containerColor = Green91C747
            )
        ) {
            Row {
                AppText(
                    text = stringResource(id = R.string.onboarding_welcome_button),
                    color = White,
                    textStyle = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Icon(
                    modifier = Modifier.padding(start = 10.dp),
                    painter = painterResource(id = drawable.ic_arrow_next),
                    contentDescription = null
                )
            }
        }
    }
}

@Preview(showSystemUi = false, showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen()
}
