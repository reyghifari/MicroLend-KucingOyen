package com.kucingoyen.dashboard.screen.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kucingoyen.core.R
import com.kucingoyen.core.theme.BaseColor
import com.kucingoyen.dashboard.DashboardViewModel

@Composable
fun WalletCard(
    dashboardViewModel: DashboardViewModel,
    onClickSend: () -> Unit = {},
    onClickDeposit: () -> Unit = {},
    onClickRequest: () -> Unit = {},
    onClickGifting: () -> Unit = {}
) {
    val balance by dashboardViewModel.balance.collectAsStateWithLifecycle()
    val totalBalance by dashboardViewModel.getTotalBalance.collectAsStateWithLifecycle()

    val brandColor = Color(0xFFFF3E17)

    Card(
        shape = RoundedCornerShape(0),
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .border(1.dp, BaseColor.JetBlack.Normal),
        colors = CardDefaults.cardColors(containerColor = brandColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.Wallet,
                    contentDescription = "Wallet Icon",
                    tint = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.size(70.dp)
                )

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Total Balance",
                        fontSize = 18.sp,
                        color = BaseColor.White,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                    Text(
                        text = "$ $totalBalance",
                        fontSize = 18.sp,
                        color = Color.White.copy(alpha = 0.85f),
                        fontWeight = FontWeight.Medium,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                WalletActionButton("Request Loan", R.drawable.ic_request_money, onClickRequest)
                WalletActionButton("Provide Funding", R.drawable.ic_give_money, onClickGifting)
                WalletActionButton("Send", R.drawable.ic_send, onClickSend)
                WalletActionButton("Deposit", R.drawable.ic_deposit, onClickDeposit)
            }
        }
    }
}

@Composable
fun WalletActionButton(
    label: String,
    iconRes: Int,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.clickable { onClick() }
    ) {
        Surface(
            shape = RoundedCornerShape(0),
            color = Color.White,
            modifier = Modifier.size(60.dp),
            shadowElevation = 2.dp
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.border(1.dp, BaseColor.JetBlack.Normal).padding(12.dp)
            ) {
                Image(
                    modifier = Modifier
                        .size(48.dp),
                    painter = painterResource(iconRes),
                    contentDescription = "Canton Logo"
                )
            }
        }

        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}