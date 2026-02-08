package com.kucingoyen.dashboard.screen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kucingoyen.core.theme.BaseColor
import java.text.NumberFormat
import java.util.Locale

val BadgeGreen = BaseColor.Irish.Normal
val BadgeRed = Color(0xFFEF4444)
val BadgeYellow = BaseColor.ButterYellow.Normal

@Composable
fun LoanDetailsCard(
    modifier: Modifier = Modifier,
    loanAmount: Double = 0.0,
    interestRate: Double = 0.0,
    statusLoan: String = "",
    dueDate: String = "",
    collateral: Double = 0.0,
    onPay: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    val isCompleted = statusLoan.equals("COMPLETED", ignoreCase = true)
    val isRequest = statusLoan.equals("REQUEST", ignoreCase = true)

    val badgeColor = when {
        isCompleted -> Color(0xFF4CAF50)
        isRequest -> Color(0xFF2196F3)
        else -> Color(0xFFFFC107)
    }

    val formattedAmount = NumberFormat.getCurrencyInstance(Locale.US).format(loanAmount)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(0),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Loan Details",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = BaseColor.JetBlack.Normal,
                    fontFamily = FontFamily.Monospace,
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StatusBadge(text = statusLoan.uppercase(), backgroundColor = badgeColor)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = formattedAmount,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BaseColor.JetBlack.Normal,
                    fontFamily = FontFamily.Monospace,
                )
                Text(
                    text = "Interest Rate: ${interestRate}%",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 20.dp),
                color = Color.LightGray.copy(alpha = 0.5f)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    DetailItem(
                        icon = Icons.Default.DateRange,
                        label = "Due Date",
                        value = dueDate
                    )
                }

                VerticalDivider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 8.dp),
                    color = Color.LightGray.copy(alpha = 0.5f)
                )

                Box(
                    modifier = Modifier.weight(1.3f),
                    contentAlignment = Alignment.Center
                ) {
                    DetailItem(
                        icon = Icons.Default.AttachMoney,
                        label = "Collateral",
                        value = "$collateral CC"
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            when {
                isCompleted -> {
                    Text(
                        text = "Loan fully paid off",
                        fontSize = 14.sp,
                        color = Color(0xFF4CAF50), // Green
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                isRequest -> {
                    Button(
                        onClick = { onCancel() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEF4444),
                        ),
                        shape = RoundedCornerShape(0)
                    ) {
                        Text(
                            text = "CANCEL REQUEST",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = Color.White
                        )
                    }
                }
                else -> {
                    Button(
                        onClick = { onPay() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BaseColor.JetBlack.Normal,
                        ),
                        shape = RoundedCornerShape(0)
                    ) {
                        Text(
                            text = "PAY NOW",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun StatusBadge(text: String, backgroundColor: Color) {
    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(0)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun DetailItem(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Gray, // TextGray
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                color = BaseColor.JetBlack.Normal,
                fontFamily = FontFamily.Monospace,
            )
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF111827),
                fontFamily = FontFamily.Monospace,
            )
        }
    }
}

data class LoanModel(
    val id: String,
    val amount: Double,
    val interest: Double,
    val status: String, // "IN PROGRESS", "OVERDUE", "COMPLETED"
    val dueDate: String,
    val collateral: Double
)

// --- Preview ---
@Preview(showBackground = true, backgroundColor = 0xFFF3F4F6)
@Composable
fun LoanCardPreview() {
    Box(modifier = Modifier.padding(16.dp)) {
        LoanDetailsCard(
            loanAmount = 1000.0,
            interestRate = 10.0,
            statusLoan = "COMPLETED",
            dueDate = "2023-12-31",
            collateral = 1000.0,
            onPay = {},
            onCancel = {}
        )
    }
}