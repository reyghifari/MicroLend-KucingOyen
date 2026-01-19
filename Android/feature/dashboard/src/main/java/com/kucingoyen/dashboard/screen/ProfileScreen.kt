package com.kucingoyen.dashboard.screen

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kucingoyen.core.extensions.copyToClipboard
import com.kucingoyen.core.theme.BaseColor
import com.kucingoyen.dashboard.DashboardViewModel

val TextGray = Color(0xFF6B7280)

@Composable
fun ProfileScreen(dashboardViewModel: DashboardViewModel) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BaseColor.JetBlack.Minus80)
            .padding(16.dp)
            .statusBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        LevelCard(dashboardViewModel.getLevelUser())

        LevelStepper(currentLevel = dashboardViewModel.getLevelUser(), totalLevels = 5)

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "Account Details",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Monospace,
                color = Color.Black
            )

            AccountDetailItem(
                icon = Icons.Default.Email,
                label = "Email",
                value = dashboardViewModel.getEmailUser()
            )

            AccountDetailItem(
                icon = Icons.Default.Wallet,
                label = "Party Id",
                value = dashboardViewModel.getPartyId(),
                showCopyButton = true,
                onClickCopy = {
                    context.copyToClipboard(text = it)
                }
            )
        }
    }
}

@Composable
fun LevelCard(
    level : Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(BaseColor.OceanBlue.Normal, BaseColor.JetBlack.Normal)
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "LEVEL $level",
                color = Color.White.copy(alpha = 0.9f),
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.sp,
                fontFamily = FontFamily.Monospace,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${level.getInterest()} Interest Rate",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                fontSize = 28.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Starter Boost Active",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                fontFamily = FontFamily.Monospace,
            )
        }
    }
}

@Composable
fun LevelStepper(currentLevel: Int, totalLevels: Int) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                thickness = 4.dp,
                color = Color.LightGray.copy(alpha = 0.5f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 1..totalLevels) {
                    val isActive = i <= currentLevel
                    val isCurrent = i == currentLevel

                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(if (isActive) BaseColor.JetBlack.Normal else Color.LightGray.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = i.toString(),
                            color = if (isActive) Color.White else TextGray,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "1 (10%)",
                color = BaseColor.JetBlack.Normal,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
            Text(
                text = "5 (5%)",
                color = TextGray,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun AccountDetailItem(
    icon: ImageVector,
    label: String,
    value: String,
    showCopyButton: Boolean = false,
    onClickCopy : (String) -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEEEEEE)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Text Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = FontFamily.Monospace,
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextGray,
                    fontFamily = FontFamily.Monospace,
                )
            }

            if (showCopyButton) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEEEEEE))
                        .clickable {
                            onClickCopy(value)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy",
                        tint = Color.Black,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

fun Int.getInterest() : String {
    return when(this){
        1 -> return "10%"
        2 -> return "9%"
        3 -> return "8%"
        4 -> return "7%"
        5 -> return "5%"
        else -> ""
    }
}