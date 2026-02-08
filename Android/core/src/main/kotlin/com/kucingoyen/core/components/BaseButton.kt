package com.kucingoyen.core.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.kucingoyen.core.extensions.skeletonLoading
import com.kucingoyen.core.theme.BaseColor
import com.kucingoyen.core.theme.BaseDp
import com.kucingoyen.core.theme.BaseFont

@Composable
fun BaseButton(
    text: String,
    modifier: Modifier = Modifier,
    iconStart: Int = 0,
    iconEnd: Int = 0,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    softWrap: Boolean = true,
    loadingHeight: Dp = BaseDp.heightButton,
    containerButtonColor: Color = BaseColor.ButtonGreenColorApp,
    textColor: Color? = null,
    textStyle: TextStyle = BaseFont.buttonText.copy(color = textColor ?: BaseColor .White),
    buttonContentPadding: PaddingValues = ButtonDefaults.ContentPadding,
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
        Button(
            onClick = onClickButton,
            shape = RoundedCornerShape(0),
            colors = ButtonDefaults.buttonColors(
                containerColor = containerButtonColor,
                disabledContainerColor = BaseColor.ButtonGrayColorDisableApp,
                contentColor = BaseColor.White,
                disabledContentColor = BaseColor.White,
            ),
            enabled = isEnabled,
            contentPadding = buttonContentPadding
        ) {
            if (iconStart != 0) {
                Image(
                    contentDescription = text,
                    modifier = Modifier
                        .size(BaseDp.dp20),
                    painter = painterResource(iconStart),
                )
                Spacer(modifier = Modifier.width(BaseDp.dp14))
            }
            Text(
                text = text,
                style = textStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                softWrap = softWrap,
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
