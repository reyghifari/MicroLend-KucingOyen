package com.kucingoyen.dashboard.screen

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallMade
import androidx.compose.material.icons.filled.CallReceived
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kucingoyen.core.theme.BaseColor
import com.kucingoyen.dashboard.screen.component.NavbarMicroLend
import com.kucingoyen.dashboard.screen.component.maskString
import com.kucingoyen.entity.model.Transaction
import com.kucingoyen.entity.model.TransactionType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TransactionDetailScreen(
    transaction: Transaction,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            NavbarMicroLend(
                title = "",
                onBack = onBack
            )
        },
        containerColor = BaseColor.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Icon
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF5C6BC0)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (transaction.type == TransactionType.RECEIVED) Icons.Default.CallReceived else Icons.Default.CallMade,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Title
            Text(
                text = "${if (transaction.type == TransactionType.RECEIVED) "Received" else "Sent"} ${transaction.tokenSymbol}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = BaseColor.JetBlack.Normal
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Date
            Text(
                text = formatDate(transaction.timestamp),
                fontSize = 14.sp,
                color = Color.Gray,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Details List
            DetailRow(
                label = "Status",
                content = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.size(4.dp))
                        Text(
                            text = "Complete",
                            color = Color(0xFF4CAF50),
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            )
            
            Divider()

            DetailRow(
                label = if (transaction.type == TransactionType.RECEIVED) "From" else "To",
                value = maskString(transaction.address, front = 6, back = 4)
            )

            Divider()

            DetailRow(
                label = "Amount",
                value = "${transaction.tokenAmount} ${transaction.tokenSymbol}"
            )
            
            Divider()

            // Price and Hash ignored as per request
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    DetailRow(label = label, content = {
        Text(
            text = value,
            fontSize = 16.sp,
            color = BaseColor.JetBlack.Normal,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Medium
        )
    })
}

@Composable
fun DetailRow(label: String, content: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            color = BaseColor.JetBlack.Normal,
            fontFamily = FontFamily.Monospace
        )
        content()
    }
}

@Composable
fun Divider() {
    HorizontalDivider(
        modifier = Modifier.fillMaxWidth(),
        thickness = 1.dp,
        color = Color.LightGray.copy(alpha = 0.5f)
    )
}

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM d, yyyy h:mm a", Locale.US)
    return sdf.format(Date(timestamp))
}
