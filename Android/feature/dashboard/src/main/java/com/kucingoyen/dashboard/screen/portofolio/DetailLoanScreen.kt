package com.kucingoyen.dashboard.screen.portofolio

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kucingoyen.core.extensions.formatReadableDateTime
import com.kucingoyen.core.theme.BaseColor
import com.kucingoyen.dashboard.DashboardViewModel
import com.kucingoyen.dashboard.screen.component.NavbarMicroLend
import java.time.Duration
import java.time.Instant
import kotlin.math.roundToInt

private val GreenStatus = Color(0xFF2E7D32)
private val GreenStatusBg = Color(0xFF4CAF50)
private val GreenBannerBg = Color(0xFF388E3C)
private val CardBg = Color(0xFFF9F9F9)

@Composable
fun DetailLoanScreen(
    dashboardViewModel: DashboardViewModel,
    onBackClick: () -> Unit = {}
) {
    val loan by dashboardViewModel.selectedLoanItem.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val contractIdShort = if (loan.contractId.length > 12) {
        "#${loan.contractId.take(6)}...${loan.contractId.takeLast(4)}"
    } else {
        "#${loan.contractId}"
    }

    val interestPercent = "${(loan.interestRate * 100).roundToInt()}%"
    val requiredRepaymentText = if (loan.requiredRepayment == 0.0) {
        "Not calculated yet"
    } else {
        "$${loan.requiredRepayment}"
    }

    val timelineProgress = calculateLoanTimelineProgress(loan.startTime, loan.endTime)
    val durationDays = calculateLoanDurationDays(loan.startTime, loan.endTime)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BaseColor.Background)
    ) {
        // Top bar
        NavbarMicroLend(
            title = "My Loan",
            onBack = onBackClick,
            modifier = Modifier.background(BaseColor.White)
        )

        // Subtitle - Contract ID
        Text(
            text = contractIdShort,
            fontSize = 13.sp,
            color = BaseColor.JetBlack.Minus20,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier
                .fillMaxWidth()
                .background(BaseColor.White)
                .padding(start = 56.dp, bottom = 12.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Status Banner
            LoanStatusBanner(
                status = loan.status,
                daysRemaining = loan.daysRemaining
            )

            // Loan Summary Card
            LoanDetailSummaryCard(
                loanAmount = loan.loanAmount,
                collateralAmount = loan.collateralAmount,
                interestRate = interestPercent,
                requiredRepayment = requiredRepaymentText
            )

            // Participants Card
            LoanParticipantsCard(
                borrowerDisplayName = loan.borrowerDisplayName,
                borrowerAddress = loan.borrower,
                lenderDisplayName = loan.lenderDisplayName,
                lenderAddress = loan.lender,
                context = context
            )

            // Timeline Card
            LoanTimelineCard(
                startTime = loan.startTime,
                endTime = loan.endTime,
                durationDays = durationDays,
                progress = timelineProgress
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun LoanStatusBanner(
    status: String,
    daysRemaining: Int
) {
    val isActive = status.equals("Active", ignoreCase = true)
    val bannerColor = if (isActive) GreenStatusBg else BaseColor.Crismon.Normal
    val bannerDarkColor = if (isActive) GreenBannerBg else BaseColor.Crismon.Plus50

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = bannerColor)
    ) {
        Column {
            // Top status row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = status,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            // Bottom detail row
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(bannerDarkColor)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = if (isActive) "Loan is Active" else "Loan is $status",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "Ends in $daysRemaining days",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
private fun LoanDetailSummaryCard(
    loanAmount: Double,
    collateralAmount: Double,
    interestRate: String,
    requiredRepayment: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BaseColor.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Loan Summary",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                fontFamily = FontFamily.Monospace,
                color = BaseColor.JetBlack.Normal
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = BaseColor.JetBlack.Minus70)
            Spacer(modifier = Modifier.height(16.dp))

            // Row 1: Loan Amount + Collateral
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Loan Amount",
                        fontSize = 12.sp,
                        color = BaseColor.JetBlack.Minus20,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$${loanAmount.toInt()}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = BaseColor.JetBlack.Normal
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Collateral",
                        fontSize = 12.sp,
                        color = BaseColor.JetBlack.Minus20,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$${collateralAmount.toInt()}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = BaseColor.JetBlack.Normal
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = BaseColor.JetBlack.Minus70)
            Spacer(modifier = Modifier.height(16.dp))

            // Row 2: Interest Rate + Required Repayment
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Interest Rate",
                        fontSize = 12.sp,
                        color = BaseColor.JetBlack.Minus20,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = interestRate,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = BaseColor.JetBlack.Normal
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Required Repayment",
                        fontSize = 12.sp,
                        color = BaseColor.JetBlack.Minus20,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = requiredRepayment,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.Monospace,
                        color = BaseColor.JetBlack.Minus20
                    )
                }
            }
        }
    }
}

@Composable
private fun LoanParticipantsCard(
    borrowerDisplayName: String,
    borrowerAddress: String,
    lenderDisplayName: String,
    lenderAddress: String,
    context: Context
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BaseColor.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Participants",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                fontFamily = FontFamily.Monospace,
                color = BaseColor.JetBlack.Normal
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = BaseColor.JetBlack.Minus70)
            Spacer(modifier = Modifier.height(16.dp))

            // Borrower
            LoanParticipantRow(
                role = "Borrower",
                displayName = borrowerDisplayName,
                address = borrowerAddress,
                dotColor = GreenStatus,
                onCopy = { copyLoanToClipboard(context, borrowerAddress) }
            )

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = BaseColor.JetBlack.Minus70)
            Spacer(modifier = Modifier.height(20.dp))

            // Lender
            LoanParticipantRow(
                role = "Lender",
                displayName = lenderDisplayName,
                address = lenderAddress,
                dotColor = GreenStatusBg,
                onCopy = { copyLoanToClipboard(context, lenderAddress) }
            )
        }
    }
}

@Composable
private fun LoanParticipantRow(
    role: String,
    displayName: String,
    address: String,
    dotColor: Color,
    onCopy: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.Top
        ) {
            // Avatar circle
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(dotColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = dotColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = role,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Monospace,
                    color = BaseColor.JetBlack.Normal
                )
                Text(
                    text = displayName,
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace,
                    color = BaseColor.JetBlack.Minus20
                )
                Text(
                    text = truncateLoanAddress(address),
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    color = BaseColor.JetBlack.Minus40,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        IconButton(
            onClick = onCopy,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ContentCopy,
                contentDescription = "Copy address",
                tint = BaseColor.JetBlack.Minus40,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun LoanTimelineCard(
    startTime: String,
    endTime: String,
    durationDays: Long,
    progress: Float
) {
    val progressPercent = (progress * 100).roundToInt()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BaseColor.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Timeline",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                fontFamily = FontFamily.Monospace,
                color = BaseColor.JetBlack.Normal
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = BaseColor.JetBlack.Minus70)
            Spacer(modifier = Modifier.height(16.dp))

            // Start Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Start Date",
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Medium,
                    color = BaseColor.JetBlack.Normal
                )
                Text(
                    text = if (startTime.isNotEmpty()) formatReadableDateTime(startTime) else "-",
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace,
                    color = BaseColor.JetBlack.Minus20
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // End Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "End Date",
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Medium,
                    color = BaseColor.JetBlack.Normal
                )
                Text(
                    text = if (endTime.isNotEmpty()) formatReadableDateTime(endTime) else "-",
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace,
                    color = BaseColor.JetBlack.Minus20
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Duration
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Duration",
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Medium,
                    color = BaseColor.JetBlack.Normal
                )
                Text(
                    text = "$durationDays Days",
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace,
                    color = BaseColor.JetBlack.Minus20
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Progress bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = GreenStatusBg,
                    trackColor = BaseColor.JetBlack.Minus70,
                    strokeCap = StrokeCap.Round,
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "$progressPercent%",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Monospace,
                    color = BaseColor.JetBlack.Normal
                )
            }
        }
    }
}

// Helper functions

private fun truncateLoanAddress(address: String): String {
    return if (address.length > 20) {
        "${address.take(18)}..."
    } else {
        address
    }
}

private fun copyLoanToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
    clipboard?.setPrimaryClip(ClipData.newPlainText("Address", text))
    Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show()
}

private fun calculateLoanTimelineProgress(startTime: String, endTime: String): Float {
    if (startTime.isEmpty() || endTime.isEmpty()) return 0f
    return try {
        val start = Instant.parse(startTime)
        val end = Instant.parse(endTime)
        val now = Instant.now()
        val total = Duration.between(start, end).toMillis().toFloat()
        if (total <= 0) return 0f
        val elapsed = Duration.between(start, now).toMillis().toFloat()
        (elapsed / total).coerceIn(0f, 1f)
    } catch (e: Exception) {
        0f
    }
}

private fun calculateLoanDurationDays(startTime: String, endTime: String): Long {
    if (startTime.isEmpty() || endTime.isEmpty()) return 0
    return try {
        val start = Instant.parse(startTime)
        val end = Instant.parse(endTime)
        Duration.between(start, end).toDays()
    } catch (e: Exception) {
        0
    }
}
