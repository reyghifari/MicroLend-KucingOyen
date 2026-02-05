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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kucingoyen.core.theme.BaseColor
import com.kucingoyen.dashboard.DashboardViewModel
import com.kucingoyen.dashboard.screen.component.TabsSection
import com.kucingoyen.dashboard.screen.component.TransactionItem
import com.kucingoyen.dashboard.screen.component.WalletCard

@Composable
fun HomeScreen(dashboardViewModel: DashboardViewModel, paddingValues: PaddingValues, onClickSend : () -> Unit = {},  onClickDeposit : () -> Unit = {}) {
    val listActivity by dashboardViewModel.listTransactionActivity.collectAsState()
    Column(
        modifier = Modifier
            .background(BaseColor.JetBlack.Minus80)
            .padding(paddingValues)
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        WalletCard(
            dashboardViewModel,
            onClickSend = onClickSend,
            onClickDeposit = onClickDeposit
        )
        Spacer(modifier = Modifier.height(24.dp))
        TabsSection()
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn {
                items(listActivity) { transaction ->
                    TransactionItem(transaction = transaction)
                }
            }
        }
    }
}