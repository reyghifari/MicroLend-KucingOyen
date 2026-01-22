package com.kucingoyen.dashboard.deposit

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kucingoyen.core.theme.BaseColor
import com.kucingoyen.dashboard.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestBalanceScreen(
    dashboardViewModel: DashboardViewModel,
    onNavigateBack: () -> Unit = {},
) {
    var selectedTab by remember { mutableStateOf(1) }

    Scaffold(
        containerColor = BaseColor.White,
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = BaseColor.JetBlack.Normal
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BaseColor.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(BaseColor.JetBlack.Minus90, RoundedCornerShape(12.dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(4.dp)
                        .background(
                            if (selectedTab == 0)  BaseColor.JetBlack.Normal else Color.Transparent,
                            RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            selectedTab = 0
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Send",
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = if (selectedTab == 0) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (selectedTab == 0) BaseColor.White else BaseColor.JetBlack.Normal
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(4.dp)
                        .background(
                            if (selectedTab == 1) BaseColor.JetBlack.Normal else Color.Transparent,
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { selectedTab = 1 },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Receive",
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = if (selectedTab == 1) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (selectedTab == 1) BaseColor.White else BaseColor.JetBlack.Normal
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            when (selectedTab) {
                0 -> {
                    SendContent(dashboardViewModel)
                }

                1 -> {
                    ReceiveContent(dashboardViewModel)
                }
            }
        }
    }
}
@Composable
private fun SendContent(
    dashboardViewModel: DashboardViewModel
) {
    var recipientAddress by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    val userBalance = "106"

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
            text = "$$userBalance USD",
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
                shape = RoundedCornerShape(12.dp),
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace
                )
            )

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(BaseColor.JetBlack.Normal, RoundedCornerShape(12.dp))
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
            shape = RoundedCornerShape(12.dp),
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
                .background(BaseColor.JetBlack.Minus90, RoundedCornerShape(12.dp))
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
            onClick = { /* Implement send logic */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = recipientAddress.isNotEmpty() && amount.isNotEmpty() && amount.toDoubleOrNull() != null && amount.toDouble() > 0,
            colors = ButtonDefaults.buttonColors(
                containerColor = BaseColor.JetBlack.Normal,
                disabledContainerColor = BaseColor.JetBlack.Minus70
            ),
            shape = RoundedCornerShape(12.dp)
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
            .background(BaseColor.JetBlack.Minus90, RoundedCornerShape(8.dp))
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

@Composable
private fun ReceiveContent(
    dashboardViewModel: DashboardViewModel
){
    val clipboardManager = LocalClipboardManager.current
    val walletAddress = dashboardViewModel.getEmailUser()
    val truncatedAddress = dashboardViewModel.getPartyId()

    Box(
        modifier = Modifier
            .size(350.dp)
            .background(BaseColor.White, RoundedCornerShape(16.dp))
            .border(2.dp, BaseColor.JetBlack.Minus80, RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            QRCodePlaceholder()

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .border(4.dp, Color.White, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BaseColor.JetBlack.Minus80)
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(32.dp))

    Text(
        text = walletAddress,
        fontSize = 18.sp,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
        color = BaseColor.JetBlack.Normal
    )

    Spacer(modifier = Modifier.height(4.dp))

    Text(
        text = truncatedAddress,
        fontSize = 14.sp,
        fontFamily = FontFamily.Monospace,
        color = BaseColor.JetBlack.Minus40
    )

    Spacer(modifier = Modifier.height(24.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Copy Button
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clickable {
                    clipboardManager.setText(AnnotatedString(walletAddress))
                }
                .padding(horizontal = 24.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(BaseColor.JetBlack.Minus90, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy",
                    tint = BaseColor.JetBlack.Minus40,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Copy",
                fontSize = 14.sp,
                color = BaseColor.JetBlack.Minus40
            )
        }

        Spacer(modifier = Modifier.width(48.dp))

        // Share Button
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clickable { /* Implement share functionality */ }
                .padding(horizontal = 24.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(BaseColor.JetBlack.Minus90, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = BaseColor.JetBlack.Minus40,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Share",
                fontSize = 14.sp,
                fontFamily = FontFamily.Monospace,
                color = BaseColor.JetBlack.Minus40
            )
        }
    }

    Spacer(modifier = Modifier.height(32.dp))

    // Network Icons Row
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NetworkIcon(Color(0xFFF0B90B)) // BSC
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = "This address supports all assets on Canton Network",
        fontSize = 13.sp,
        color = BaseColor.JetBlack.Minus40,
        textAlign = TextAlign.Center,
        fontFamily = FontFamily.Monospace,
        modifier = Modifier.padding(horizontal = 32.dp)
    )

    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
fun NetworkIcon(color: Color) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(color)
    )
}

@Composable
fun QRCodePlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BaseColor.White),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "QR Code\nPlaceholder",
            fontSize = 14.sp,
            color = BaseColor.JetBlack.Minus40,
            textAlign = TextAlign.Center
        )
    }
}
