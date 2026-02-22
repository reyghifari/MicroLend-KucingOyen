package com.kucingoyen.dashboard.screen.funding

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kucingoyen.core.R
import com.kucingoyen.core.components.BaseButtonSlider
import com.kucingoyen.core.components.InfoRow
import com.kucingoyen.core.components.bottomsheet.ErrorBottomSheet
import com.kucingoyen.core.components.bottomsheet.SuccessTransferSheet
import com.kucingoyen.core.theme.BaseColor
import com.kucingoyen.dashboard.DashboardViewModel
import com.kucingoyen.dashboard.screen.Divider
import com.kucingoyen.dashboard.screen.component.NavbarMicroLend
import com.kucingoyen.entity.model.CreateReviewResponse
import com.kucingoyen.entity.model.ReviewSummaryResponse

private val StarColor = Color(0xFFFFA726)

@Composable
fun DetailProvideFundScreen(
    dashboardViewModel: DashboardViewModel,
    onBackClick: () -> Unit
) {
    val selectedLoanRequest by dashboardViewModel.selectedLoanRequest.collectAsStateWithLifecycle()
    val bottomSheetSuccessFund by dashboardViewModel.bottomSheetSuccessFundLoan.collectAsStateWithLifecycle()
    val bottomSheetNotEnoughFund by dashboardViewModel.bottomSheetNotEnoughFund.collectAsStateWithLifecycle()
    val reviewSummary by dashboardViewModel.reviewSummary.collectAsStateWithLifecycle()

    LaunchedEffect(selectedLoanRequest.borrower) {
        if (selectedLoanRequest.borrower.isNotEmpty()) {
            dashboardViewModel.getReviewSummary(selectedLoanRequest.borrower)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BaseColor.White)
    ) {
        NavbarMicroLend(title = "Fund Loan", onBack = { onBackClick() })

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.img_send_money),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    tint = Color.Unspecified
                )
            }

            HorizontalDivider(thickness = 1.dp, color = BaseColor.Black)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {

                InfoRow(
                    label = "Borrower Address",
                    value = selectedLoanRequest.borrower
                )

                Spacer(modifier = Modifier.height(20.dp))

                InfoRow(
                    label = "Borrower Level",
                    value = selectedLoanRequest.borrowerLevel
                )

                Spacer(modifier = Modifier.height(16.dp))

                Divider()

                Spacer(modifier = Modifier.height(16.dp))

                InfoRow(
                    label = "Loan Amount",
                    value = "${selectedLoanRequest.loanAmount} CC",
                    fontSize = 22.sp,
                    isBold = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Divider()

                Spacer(modifier = Modifier.height(28.dp))

                InfoRow(
                    label = "Duration Loan",
                    value = "${selectedLoanRequest.durationDays} Days"
                )

                Spacer(modifier = Modifier.height(16.dp))

                InfoRow(
                    label = "Collateral Ratio",
                    value = "${selectedLoanRequest.collateralRatio}"
                )

                Spacer(modifier = Modifier.height(16.dp))

                Divider()

                Spacer(modifier = Modifier.height(16.dp))

                InfoRow(
                    label = "Collateral Amount",
                    value = "${selectedLoanRequest.collateralAmount} USDCx",
                    fontSize = 22.sp,
                    isBold = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Divider()

                Spacer(modifier = Modifier.height(24.dp))

                // Reviews Section
                ReviewsSection(reviewSummary = reviewSummary)

                Spacer(modifier = Modifier.height(24.dp))

                Divider()

                Spacer(modifier = Modifier.height(16.dp))

                BaseButtonSlider(
                    onConfirmed = {
                        dashboardViewModel.fillLoanRequestAsLender(
                            selectedLoanRequest.contractId,
                            selectedLoanRequest.loanAmount
                        )
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        if (bottomSheetSuccessFund) {
            SuccessTransferSheet(
                title = "Success",
                desc = "Success fund loan"
            ) {
                dashboardViewModel.updateBottomSuccessFundLoan(false)
            }
        }
        if (bottomSheetNotEnoughFund) {
            ErrorBottomSheet(
                title = "Failed",
                desc = "Not enough fund"
            ) {
                dashboardViewModel.updateBottomBarNotEnoughFund(false)
            }
        }
    }
}

@Composable
private fun ReviewsSection(reviewSummary: ReviewSummaryResponse) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Reviews",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            fontFamily = FontFamily.Monospace,
            color = BaseColor.JetBlack.Normal
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Average rating row
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            StarRatingBar(
                rating = reviewSummary.averageRating,
                starSize = 22
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = String.format("%.1f", reviewSummary.averageRating),
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                fontFamily = FontFamily.Monospace,
                color = BaseColor.JetBlack.Normal
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "out of 5",
                fontSize = 14.sp,
                fontFamily = FontFamily.Monospace,
                color = BaseColor.JetBlack.Minus20
            )
        }

        if (reviewSummary.reviews.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))

            reviewSummary.reviews.forEach { review ->
                HorizontalDivider(
                    color = BaseColor.JetBlack.Minus70,
                    thickness = 0.5.dp
                )
                Spacer(modifier = Modifier.height(14.dp))

                ReviewItem(review = review)

                Spacer(modifier = Modifier.height(14.dp))
            }
        } else {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "No reviews yet",
                fontSize = 14.sp,
                fontFamily = FontFamily.Monospace,
                color = BaseColor.JetBlack.Minus40
            )
        }
    }
}

@Composable
private fun ReviewItem(review: CreateReviewResponse) {
    val reviewerName = review.reviewerEmail
        .substringBefore("@")
        .replace(".", "_")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            StarRatingBar(
                rating = review.rating.toDouble(),
                starSize = 18
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = review.comment,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                fontFamily = FontFamily.Monospace,
                color = BaseColor.JetBlack.Normal
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "— $reviewerName",
                fontSize = 13.sp,
                fontFamily = FontFamily.Monospace,
                color = BaseColor.JetBlack.Minus20
            )
        }

        Text(
            text = "${review.rating}",
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            fontFamily = FontFamily.Monospace,
            color = BaseColor.JetBlack.Minus20
        )
    }
}

@Composable
private fun StarRatingBar(
    rating: Double,
    starSize: Int = 20
) {
    Row {
        for (i in 1..5) {
            val icon = when {
                i <= rating.toInt() -> Icons.Filled.Star
                i - 0.5 <= rating -> Icons.Filled.StarHalf
                else -> Icons.Outlined.StarOutline
            }
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = StarColor,
                modifier = Modifier.size(starSize.dp)
            )
        }
    }
}
