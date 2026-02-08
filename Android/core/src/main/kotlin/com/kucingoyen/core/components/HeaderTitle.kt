package com.kucingoyen.core.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kucingoyen.core.theme.BaseColor

@Composable
fun HeaderTitle(
    modifier: Modifier = Modifier,
    textHeader: String = "",
    backgroundHeader: Color = Color(0xFFF5F7F9),
    onClickIcon: () -> Unit = {},
) {

    SlantCard(
        modifier =
            modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        slantSide = SlantSide.Bottom,
        slantDirection = SlantDirection.Right,
        slantSize = 24.dp,
        backgroundColor = backgroundHeader,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = textHeader,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Normal,
                fontSize = 32.sp,
                color = BaseColor.JetBlack.Normal,
                modifier = Modifier .weight(1f)
            )

            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}