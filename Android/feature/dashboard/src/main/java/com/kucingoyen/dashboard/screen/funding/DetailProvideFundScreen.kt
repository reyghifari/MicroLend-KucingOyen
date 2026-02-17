package com.kucingoyen.dashboard.screen.funding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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

@Composable
fun DetailProvideFundScreen(
    dashboardViewModel: DashboardViewModel,
    onBackClick: () -> Unit
) {
    val selectedLoanRequest by dashboardViewModel.selectedLoanRequest.collectAsStateWithLifecycle()
    val bottomSheetSuccessFund by dashboardViewModel.bottomSheetSuccessFundLoan.collectAsStateWithLifecycle()
    val bottomSheetNotEnoughFund by dashboardViewModel.bottomSheetNotEnoughFund.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BaseColor.White)
    ) {
        NavbarMicroLend(title = "Fund Loan", onBack = {onBackClick()})
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

            Spacer(modifier = Modifier.height(16.dp))

            BaseButtonSlider(
                onConfirmed = {
                    dashboardViewModel.fillLoanRequestAsLender(
                        selectedLoanRequest.contractId,
                        selectedLoanRequest.loanAmount
                    )
                }
            )
        }

        if (bottomSheetSuccessFund){
            SuccessTransferSheet(
                title = "Success",
                desc = "Success fund loan"
            ) {
                dashboardViewModel.updateBottomSuccessFundLoan(false)
            }
        }
        if (bottomSheetNotEnoughFund){
            ErrorBottomSheet(
                title = "Failed",
                desc = "Not enough fund"
            ) {
                dashboardViewModel.updateBottomBarNotEnoughFund(false)
            }
        }
    }
}
