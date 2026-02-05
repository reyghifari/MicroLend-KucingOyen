package com.kucingoyen.dashboard.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallReceived
import androidx.compose.material.icons.filled.CallMade
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kucingoyen.core.theme.BaseColor
import com.kucingoyen.entity.model.Transaction
import com.kucingoyen.entity.model.TransactionType

@Composable
fun TransactionItem(transaction: Transaction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFF5C6BC0)),
            contentAlignment = Alignment.Center
        ) {
             Icon(
                imageVector = if (transaction.type == TransactionType.RECEIVED) Icons.Default.CallReceived else Icons.Default.CallMade,
                contentDescription = null,
                tint = Color.White,
                 modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = if (transaction.type == TransactionType.RECEIVED) "Received" else "Sent",
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = if (transaction.type == TransactionType.RECEIVED) "From ${maskString(transaction.address)}" else "To ${maskString(transaction.address)}",
                color = Color.Gray,
                fontSize = 14.sp,
                fontFamily = FontFamily.Monospace
            )
        }

        // Amounts
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = (if (transaction.type == TransactionType.RECEIVED) "+" else "-") +"${transaction.tokenAmount} ${transaction.tokenSymbol}",
                color = if (transaction.type == TransactionType.RECEIVED) BaseColor.Irish.Normal else BaseColor.JetBlack.Normal,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

fun maskString(input: String, front: Int = 4, back: Int = 4, replacement: String = "...."): String {
    if (input.length <= (front + back)) return input

    val start = input.take(front)
    val end = input.takeLast(back)

    return "$start$replacement$end"
}

@Preview(showBackground = true)
@Composable
fun TransactionItemPreview() {
    Column {
        TransactionItem(
            Transaction(
                type = TransactionType.RECEIVED,
                address = "0xcba5...f1b7",
                tokenAmount = "0.00003",
                tokenSymbol = "ETH"
            )
        )
        TransactionItem(
            Transaction(
                type = TransactionType.SENT,
                address = "0xcba5...f1b7",
                tokenAmount = "0.0001",
                tokenSymbol = "BTC"
            )
        )
    }
}
