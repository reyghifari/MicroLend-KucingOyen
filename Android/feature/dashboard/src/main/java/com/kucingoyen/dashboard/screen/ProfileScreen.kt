package com.kucingoyen.dashboard.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
val Orange = Color(0xFFFF5722)

@Composable
fun ProfileScreen(dashboardViewModel: DashboardViewModel, paddingValues: PaddingValues) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BaseColor.White)
            .statusBarsPadding()
            .verticalScroll(scrollState)
            .padding(paddingValues)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        
        Text(
            text = "Profile",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = BaseColor.JetBlack.Normal
        )

        LevelCard(dashboardViewModel.getLevelUser())

        LockedRewardsList()

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
fun LevelCard(level: Int) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = Color(0xFFE8EAF6)
            ) {
                 Icon(
                    imageVector = Icons.Default.WaterDrop, // Placeholder for level icon
                    contentDescription = null,
                    tint = Color(0xFF3F51B5),
                    modifier = Modifier.padding(10.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Level $level",
                    color = TextGray,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                )
                Text(
                    text = "Active",
                    color = BaseColor.JetBlack.Normal,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Monospace,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LinearProgressIndicator(
            progress = { 0.1f },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = Orange,
            trackColor = Color(0xFFF3F4F6),
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.WaterDrop,
                    contentDescription = null,
                    tint = Orange,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "1 points",
                    color = BaseColor.JetBlack.Normal,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace,
                )
            }
            Text(
                text = "1499 to level up",
                color = TextGray,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
            )
        }
    }
}

data class LevelData(
    val level: Int,
    val name: String,
    val requiredPoints: Int,
    val perksTitle: String,
    val perksDescription: String,
    val isLocked: Boolean = true
)

@Composable
fun LockedRewardsList() {
    val levels = listOf(
        LevelData(2, "Reliable", 1500, "Collateral ratio: 180%", "Loan limits: 200$"),
        LevelData(3, "Consistent", 3500, "Collateral ratio: 160%", "Loan limits: 500$"),
        LevelData(4, "Trusted", 7500, "Collateral ratio: 140%", "Loan limits: 1000$"),
        LevelData(5, "Prime", 10000, "Collateral ratio: 130%", "Loan limits: 5000$")
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Locked rewards",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = BaseColor.JetBlack.Normal
            )
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = Color(0xFFE0E7FF)
            ) {
                Text(
                    text = "${levels.size}",
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3F51B5),
                    fontFamily = FontFamily.Monospace,
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF3F4F6)) // Light gray container background
        ) {
             levels.forEachIndexed { index, levelData ->
                LockedLevelItem(levelData)
                if (index < levels.size - 1) {
                    HorizontalDivider(color = Color.White, thickness = 1.dp)
                }
            }
        }
    }
}

@Composable
fun LockedLevelItem(data: LevelData) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
             Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFE8EAF6)
            ) {
                 Icon(
                    imageVector = Icons.Default.WaterDrop, // Placeholder
                    contentDescription = null,
                    tint = Color(0xFF5C6BC0),
                    modifier = Modifier.padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Level ${data.level} • ${data.name}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = BaseColor.JetBlack.Normal
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = TextGray,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                     Text(
                        text = "${data.requiredPoints} points",
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        color = TextGray
                    )
                }
            }
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = BaseColor.JetBlack.Normal
            )
        }

        AnimatedVisibility(visible = expanded) {
             Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                 Surface(
                    modifier = Modifier.size(40.dp),
                    shape = CircleShape,
                    color = Color(0xFFE0E0E0)
                ) {
                      Icon(
                        imageVector = Icons.Default.Wallet, // Placeholder
                        contentDescription = null,
                        tint = TextGray,
                        modifier = Modifier.padding(10.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = data.perksTitle,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.Monospace,
                        color = BaseColor.JetBlack.Normal
                    )
                     Text(
                        text = data.perksDescription,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        color = TextGray,
                        lineHeight = 16.sp
                    )
                }
            }
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
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
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