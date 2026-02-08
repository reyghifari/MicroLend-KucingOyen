package com.kucingoyen.dashboard.screen.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kucingoyen.core.R
import com.kucingoyen.core.theme.BaseColor

@Composable
fun AssetSection(
    assetName: String = "",
    assetSymbol: String = "",
    balance: String = "",
) {
    val imageRes = if (assetSymbol == "CC") R.drawable.ic_canton_logo else R.drawable.ic_usdx
    Card(
        shape = RoundedCornerShape(0),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = BaseColor.JetBlack.Normal,
            ),
        colors = CardDefaults.cardColors(containerColor = BaseColor.White)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(48.dp),
                painter = painterResource(imageRes),
                contentDescription = "Canton Logo"
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = assetName,
                    color = BaseColor.JetBlack.Normal,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = assetSymbol,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = balance,
                    color = BaseColor.JetBlack.Normal,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}