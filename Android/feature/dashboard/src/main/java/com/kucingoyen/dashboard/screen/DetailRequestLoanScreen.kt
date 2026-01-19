package com.kucingoyen.dashboard.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kucingoyen.core.theme.BaseColor
import com.kucingoyen.dashboard.DashboardViewModel
import com.kucingoyen.dashboard.screen.component.InputCard
import com.kucingoyen.dashboard.screen.component.LevelInterest
import com.kucingoyen.dashboard.screen.component.NavbarMicroLend
import com.kucingoyen.dashboard.screen.component.TermsAgreementNoticeTring

@Composable
fun DetailRequestLoanScreen(
    dashboardViewModel: DashboardViewModel,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = BaseColor.JetBlack.Minus90)
            .padding(WindowInsets.systemBars.asPaddingValues())
    ) {
        Column {
            NavbarMicroLend(title = "Create Loan")
            ContentRequestLoan(modifier.weight(1f),dashboardViewModel)
            StickyContentLoan(dashboardViewModel)
        }
    }
}

@Composable
fun ContentRequestLoan(modifier: Modifier = Modifier,
                       dashboardViewModel: DashboardViewModel,
) {
    val loanAmount by dashboardViewModel.loanAmount.collectAsStateWithLifecycle()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LevelInterest(level = dashboardViewModel.getLevelUser().toString())
        Text(
            text = "Loan Data",
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = BaseColor.JetBlack.Normal,
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .background(BaseColor.JetBlack.Normal)
                .padding(16.dp)
        ) {
            InputCard(
                nominalLoan = loanAmount,
                onInputChange = {
                    dashboardViewModel.updateLoanAmount(it)
                }
            )
        }
        TermsAgreementNoticeTring()
    }
}

@Composable
fun StickyContentLoan(dashboardViewModel: DashboardViewModel) {
    val totalCollateral by dashboardViewModel.totalCollateral.collectAsStateWithLifecycle()

    Column {
        HorizontalDivider(thickness = 2.dp, color = BaseColor.JetBlack.Minus80)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = BaseColor.JetBlack.Minus90)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 25.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        modifier = Modifier
                            .weight(1f, fill = false)
                            .wrapContentWidth(),
                        text = "Total Collateral",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = BaseColor.JetBlack.Minus20
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f, fill = false)
                            .wrapContentWidth(),
                        text = "$totalCollateral CC",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = BaseColor.JetBlack.Normal
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {},
                    modifier = Modifier
                        .height(48.dp)
                        .width(139.dp),
                    colors = ButtonColors(
                        containerColor = BaseColor.JetBlack.Normal,
                        contentColor = BaseColor.JetBlack.Normal,
                        disabledContainerColor = BaseColor.JetBlack.Normal,
                        disabledContentColor = BaseColor.JetBlack.Normal
                    )
                ){
                    Text(
                        text = "PAY",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = BaseColor.White
                    )
                }
            }
        }
    }
}
