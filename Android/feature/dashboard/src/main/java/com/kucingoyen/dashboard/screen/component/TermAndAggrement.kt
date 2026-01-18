package com.kucingoyen.dashboard.screen.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kucingoyen.core.R
import com.kucingoyen.core.theme.BaseColor

@Composable
fun TermsAgreementNoticeTring(
    modifier: Modifier = Modifier,
    title: String = "By continuing, you agree to the Terms & Conditions.",
    spanText: String = "Terms & Conditions",
    spanStyle: TextStyle = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 18.sp).copy(color = BaseColor.OceanBlue.Normal),
    onClickText: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            contentDescription = "Icon Secure Check",
            modifier = Modifier
                .size(32.dp),
            painter = painterResource( R.drawable.ic_secure_check),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = buildAnnotatedString {
                val startIndex = title.indexOf(spanText)
                append(title)

                if (startIndex != -1) {
                    addStyle(
                        style = SpanStyle(
                            fontFamily = spanStyle.fontFamily,
                            fontWeight = spanStyle.fontWeight,
                            fontSize = spanStyle.fontSize,
                            letterSpacing = spanStyle.letterSpacing,
                            textDecoration = spanStyle.textDecoration,
                            color = spanStyle.color
                        ),
                        start = startIndex,
                        end = startIndex + spanText.length
                    )
                }
            },
            style = TextStyle(
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                lineHeight = 18.sp
            ).copy(color = BaseColor.JetBlack.Normal),
            modifier = Modifier
                .clickable(onClick = onClickText)
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewTermsAgreementNoticeTring() {
    TermsAgreementNoticeTring()
}
