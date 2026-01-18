package com.kucingoyen.dashboard.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kucingoyen.core.theme.BaseColor
import com.kucingoyen.dashboard.screen.DashboardViewModel

@Composable
fun WalletTopBar(
    dashboardViewModel: DashboardViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BaseColor.JetBlack.Minus70)
            .padding(top = 16.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "raihan",
                color = BaseColor.JetBlack.Normal,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = "(nightly::12...0af6c48aca9)",
                color = BaseColor.JetBlack.Minus20,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}