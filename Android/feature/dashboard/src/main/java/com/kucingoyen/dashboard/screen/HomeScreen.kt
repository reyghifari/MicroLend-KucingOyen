package com.kucingoyen.dashboard.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kucingoyen.core.theme.BaseColor
import com.kucingoyen.dashboard.DashboardViewModel
import com.kucingoyen.dashboard.screen.component.AssetSection
import com.kucingoyen.dashboard.screen.component.TabsSection
import com.kucingoyen.dashboard.screen.component.TransactionItem
import com.kucingoyen.dashboard.screen.component.WalletCard
import com.kucingoyen.entity.model.Transaction

@Composable
fun HomeScreen(
    dashboardViewModel: DashboardViewModel,
    paddingValues: PaddingValues,
    onClickSend: () -> Unit = {},
    onClickDeposit: () -> Unit = {},
    onClickTransaction: (Transaction) -> Unit = {}
) {
    val listActivity by dashboardViewModel.listTransactionActivity.collectAsState()
    val balance by dashboardViewModel.balance.collectAsState()

    LazyColumn(
        modifier = Modifier
            .background(BaseColor.White)
            .padding(paddingValues)
            .fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
    ) {
        item {
            LoopProfileBar(
                dashboardViewModel.getPartyId()
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            WalletCard(
                dashboardViewModel,
                onClickSend = onClickSend,
                onClickDeposit = onClickDeposit
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            TabsSection("My Token")
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            AssetSection(
                assetName = "Canton",
                assetSymbol = "CC",
                balance = balance.CC.toString(),
            )
            Spacer(modifier = Modifier.height(8.dp))
            AssetSection(
                assetName = "Stablecoin",
                assetSymbol = "USDx",
                balance = "100.0",
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            TabsSection("Activity")
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(listActivity) { transaction ->
            TransactionItem(transaction = transaction) {
                onClickTransaction(it)
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}
@Composable
fun LoopProfileBar(
    address: String = "",
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Microlend",
            color = BaseColor.JetBlack.Normal,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )
        Spacer(modifier = Modifier.width(16.dp))
        Box(
            modifier = Modifier
                .border(
                    BorderStroke(1.dp, BaseColor.JetBlack.Normal),
                )
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatMaskedAddress(address),
                    color = Color.Gray,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy",
                    tint =BaseColor.JetBlack.Normal,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

fun formatMaskedAddress(address: String): String {
    if (address.length <= 12) return address

    val firstPart = "${address.substring(0, 3)}...${address.substring(6, 9)}"
    val secondPart = "${address.substring(address.length - 6, address.length - 3)}...${address.substring(address.length - 3)}"

    return "$firstPart::$secondPart"
}