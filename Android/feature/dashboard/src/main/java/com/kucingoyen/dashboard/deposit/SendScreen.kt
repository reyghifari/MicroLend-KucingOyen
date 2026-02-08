package com.kucingoyen.dashboard.deposit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kucingoyen.core.components.bottomsheet.SuccessTransferSheet
import com.kucingoyen.core.theme.BaseColor
import com.kucingoyen.dashboard.DashboardViewModel


@Composable
fun SendContent(
    dashboardViewModel: DashboardViewModel
) {
    val balance by dashboardViewModel.balance.collectAsStateWithLifecycle()
    val showSuccessTransferSheet by dashboardViewModel.showSuccessTransferSheet.collectAsStateWithLifecycle()

    var recipientAddress by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    val userBalance = balance.CC.toString()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.Start
    ) {

        Text(
            text = "Available Balance",
            fontSize = 14.sp,
            fontFamily = FontFamily.Monospace,
            color = BaseColor.JetBlack.Minus40,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = "$${balance.CC} CC",
            fontSize = 28.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = BaseColor.JetBlack.Normal,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Text(
            text = "Recipient Address",
            fontSize = 14.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.SemiBold,
            color = BaseColor.JetBlack.Normal,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = recipientAddress,
                onValueChange = { recipientAddress = it },
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        text = "Enter address or scan QR",
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Monospace,
                        color = BaseColor.JetBlack.Minus40
                    )
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BaseColor.JetBlack.Normal,
                    unfocusedBorderColor = BaseColor.JetBlack.Minus70,
                    focusedContainerColor = BaseColor.White,
                    unfocusedContainerColor = BaseColor.White
                ),
                shape = RoundedCornerShape(0),
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace
                )
            )

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(BaseColor.JetBlack.Normal, RoundedCornerShape(0))
                    .clickable { /* Implement QR scan */ },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Scan QR",
                    tint = BaseColor.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Amount Section
        Text(
            text = "Amount",
            fontSize = 14.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.SemiBold,
            color = BaseColor.JetBlack.Normal,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = amount,
            onValueChange = { newValue ->
                // Only allow numbers and decimal point
                if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                    amount = newValue
                }
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = "0.00",
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Monospace,
                    color = BaseColor.JetBlack.Minus40
                )
            },
            leadingIcon = {
                Text(
                    text = "$",
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = BaseColor.JetBlack.Normal,
                    modifier = Modifier.padding(start = 8.dp)
                )
            },
            trailingIcon = {
                TextButton(
                    onClick = { amount = userBalance },
                    modifier = Modifier.padding(end = 4.dp)
                ) {
                    Text(
                        text = "MAX",
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = BaseColor.JetBlack.Normal
                    )
                }
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BaseColor.JetBlack.Normal,
                unfocusedBorderColor = BaseColor.JetBlack.Minus70,
                focusedContainerColor = BaseColor.White,
                unfocusedContainerColor = BaseColor.White
            ),
            shape = RoundedCornerShape(0),
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = 18.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Amount Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            QuickAmountButton("$10", onClick = { amount = "10" })
            QuickAmountButton("$50", onClick = { amount = "50" })
            QuickAmountButton("$100", onClick = { amount = "100" })
            QuickAmountButton("$500", onClick = { amount = "500" })
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Network Fee Info
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(BaseColor.JetBlack.Minus90, RoundedCornerShape(0))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Network Fee",
                fontSize = 14.sp,
                fontFamily = FontFamily.Monospace,
                color = BaseColor.JetBlack.Minus40
            )
            Text(
                text = "$0.50",
                fontSize = 14.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold,
                color = BaseColor.JetBlack.Normal
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Total Amount
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Total",
                fontSize = 16.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = BaseColor.JetBlack.Normal
            )
            Text(
                text = "$${calculateTotal(amount)}",
                fontSize = 16.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = BaseColor.JetBlack.Normal
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Send Button
        Button(
            onClick = { dashboardViewModel.transfer(amount, recipientAddress) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = recipientAddress.isNotEmpty() && amount.isNotEmpty() && amount.toDoubleOrNull() != null && amount.toDouble() > 0,
            colors = ButtonDefaults.buttonColors(
                containerColor = BaseColor.JetBlack.Normal,
                disabledContainerColor = BaseColor.JetBlack.Minus70
            ),
            shape = RoundedCornerShape(0)
        ) {
            Text(
                text = "Send",
                fontSize = 18.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = BaseColor.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Network Support Text
        Text(
            text = "Transaction will be processed on Canton Network",
            fontSize = 12.sp,
            color = BaseColor.JetBlack.Minus40,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        if (showSuccessTransferSheet){
            SuccessTransferSheet(
                title = "Success",
                desc = "Transfer completed successfully"
            ) {
                dashboardViewModel.updateShowSuccessTransferSheet(false)
            }
        }
    }
}

@Composable
private fun RowScope.QuickAmountButton(
    label: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .height(40.dp)
            .background(BaseColor.JetBlack.Minus90, RoundedCornerShape(0))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.SemiBold,
            color = BaseColor.JetBlack.Normal
        )
    }
}


private fun calculateTotal(amount: String): String {
    val amountValue = amount.toDoubleOrNull() ?: 0.0
    val fee = 0.50
    val total = amountValue + fee
    return String.format("%.2f", total)
}
