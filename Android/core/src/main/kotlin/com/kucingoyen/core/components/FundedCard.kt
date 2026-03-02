package com.kucingoyen.core.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kucingoyen.core.R
import com.kucingoyen.core.extensions.formatReadableDateTime
import com.kucingoyen.core.theme.BaseColor

@Composable
fun FundedCard(
    modifier: Modifier = Modifier,
    status: String = "Active",
    loan: String = "1000",
    expiredDate: String = "101230875233",
    cardImageRes: Int,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(0),
        border = BorderStroke(2.dp, BaseColor.JetBlack.Normal),
        colors = CardDefaults.cardColors(
            containerColor = BaseColor.White
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(id = cardImageRes),
                contentDescription = "Bank Card",
                modifier = Modifier
                    .width(90.dp)
                    .height(140.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CreditCard,
                        contentDescription = null,
                        tint = BaseColor.JetBlack.Normal
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = status,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BaseColor.JetBlack.Normal,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Funded: $loan CC",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color =  BaseColor.JetBlack.Normal,
                    fontFamily = FontFamily.Monospace
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "End: ${formatReadableDateTime(expiredDate)}",
                    fontSize = 12.sp,
                    color = BaseColor.JetBlack.Minus20,
                    fontFamily = FontFamily.Monospace
                )
            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Detail",
                tint = BaseColor.JetBlack.Minus20,
            )
        }
    }
}

@Preview
@Composable
private fun FundedCardPreview() {
    FundedCard(
        cardImageRes = R.drawable.img_create_loan,
        onClick = {
            // navigate ke detail
        }
    )
}
