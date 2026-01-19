package com.kucingoyen.dashboard.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kucingoyen.core.theme.BaseColor
import com.kucingoyen.dashboard.DashboardViewModel
import com.kucingoyen.dashboard.screen.component.TabsSection
import com.kucingoyen.dashboard.screen.component.WalletCard

@Composable
fun HomeScreen(dashboardViewModel: DashboardViewModel, paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .background(BaseColor.JetBlack.Minus80)
            .padding(paddingValues)
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        WalletCard()
        Spacer(modifier = Modifier.height(24.dp))
        TabsSection()
        Spacer(modifier = Modifier.height(40.dp))

        Box(
            modifier = Modifier.fillMaxWidth().weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No Activity found...",
                color = BaseColor.JetBlack.Normal,
                fontSize = 16.sp
            )
        }
    }
}