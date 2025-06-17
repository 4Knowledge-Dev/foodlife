package com.forknowledge.core.ui.theme.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forknowledge.core.ui.R.drawable
import com.forknowledge.core.ui.theme.Black374957
import com.forknowledge.core.ui.theme.Grey7F000000
import com.forknowledge.core.ui.theme.GreyF4F5F5
import com.forknowledge.core.ui.theme.Typography

@Composable
fun AppSearchBar(
    modifier: Modifier = Modifier,
    placeholder: String,
    onClicked: () -> Unit,
) {
    Row(
        modifier = modifier
            .height(55.dp)
            .background(
                color = GreyF4F5F5,
                shape = RoundedCornerShape(32.dp)
            )
            .clip(RoundedCornerShape(32.dp))
            .clickable { onClicked() }
            .padding(
                vertical = 16.dp,
                horizontal = 21.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(id = drawable.ic_search),
            tint = Black374957,
            contentDescription = null
        )

        AppText(
            modifier = Modifier.padding(start = 18.dp),
            text = placeholder,
            textStyle = Typography.bodyMedium,
            color = Grey7F000000
        )

        Spacer(modifier = Modifier.weight(1f))

        /*Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(id = drawable.ic_scan),
            tint = Black374957,
            contentDescription = null
        )*/
    }
}

@Preview
@Composable
fun AppSearchBarPreview() {
    AppSearchBar(
        placeholder = "Placeholder",
        onClicked = {}
    )
}