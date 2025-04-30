package com.forknowledge.feature.authentication.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.forknowledge.core.ui.R.drawable
import com.forknowledge.core.ui.theme.openSansFamily

@Composable
fun HeaderSection(
    modifier: Modifier = Modifier,
    title: String,
    onBackClick: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(
            modifier = modifier.size(24.dp),
            onClick = { onBackClick() }) {
            Icon(
                painter = painterResource(id = drawable.ic_back),
                contentDescription = ""
            )
        }

        Text(
            modifier = Modifier.padding(start = 24.dp),
            text = title,
            fontFamily = openSansFamily,
            fontSize = 28.sp,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderSectionPreview() {
    HeaderSection(title = "Login") {}
}
