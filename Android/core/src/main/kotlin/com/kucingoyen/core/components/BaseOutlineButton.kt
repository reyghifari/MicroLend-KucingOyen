package com.kucingoyen.core.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kucingoyen.core.extensions.skeletonLoading
import com.kucingoyen.core.theme.BaseColor
import com.kucingoyen.core.theme.BaseDp
import com.kucingoyen.core.theme.BaseFont

@Composable
fun BaseOutlineButton(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color? = null,
    textStyle: TextStyle = BaseFont.buttonText,
    iconStart: Int = 0,
    iconEnd: Int = 0,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    loadingHeight: Dp = BaseDp.heightButton,
    iconStartColor: Color? = null,
    borderColorEnable: Color? = null,
    isTransparent: Boolean = false,
    onClickButton: () -> Unit = {}
) {


    if (isLoading) {
        Box(
            modifier = modifier.skeletonLoading(
                isLoading = true,
                height = loadingHeight,
                shape = RoundedCornerShape(0)
            )
        )
    } else {
        OutlinedButton(
            onClick = onClickButton,
            shape = RoundedCornerShape(BaseDp.dp24),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (isTransparent) Color.Transparent else BaseColor.White,
                disabledContainerColor = BaseColor.ButtonGrayColorDisableApp,
                contentColor = if (isTransparent) BaseColor.White else BaseColor.ButtonGreenColorApp,
                disabledContentColor = BaseColor.White,
            ),
            border = BorderStroke(
                width = 1.dp, color = if (isTransparent) BaseColor.White
                else if (isEnabled) borderColorEnable ?: BaseColor.ButtonGreenColorApp
                else BaseColor.ButtonTextColorDisableApp
            ),
            enabled = isEnabled
        ) {
            if (iconStart != 0) {
                Image(
                    contentDescription = text,
                    modifier = Modifier.size(BaseDp.dp20),
                    painter = painterResource(iconStart),
                )
                Spacer(modifier = Modifier.width(BaseDp.dp14))
            }
            Text(
                text = text,
                style = textStyle,
                color = textColor ?: Color.Unspecified,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (iconEnd != 0) {
                Spacer(modifier = Modifier.width(BaseDp.dp14))
                Image(
                    contentDescription = text,
                    painter = painterResource(iconEnd),
                )
            }
        }
    }
}