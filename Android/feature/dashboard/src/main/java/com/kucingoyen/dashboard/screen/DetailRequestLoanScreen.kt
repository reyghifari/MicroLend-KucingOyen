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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kucingoyen.core.R
import com.kucingoyen.core.components.bottomsheet.BaseBottomSheet
import com.kucingoyen.core.components.bottomsheet.ErrorBottomSheet
import com.kucingoyen.core.components.bottomsheet.SuccessTransferSheet
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
    onBackClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = BaseColor.JetBlack.Minus90)
            .padding(WindowInsets.systemBars.asPaddingValues())
    ) {
        Column {
            NavbarMicroLend(title = "Create Loan", onBack = { onBackClick() })
            ContentRequestLoan(modifier.weight(1f),dashboardViewModel, onBackClick = onBackClick)
            StickyContentLoan(dashboardViewModel)
        }
    }
}

@Composable
fun ContentRequestLoan(modifier: Modifier = Modifier,
                       dashboardViewModel: DashboardViewModel,
                       onBackClick: () -> Unit
) {
    val loanAmount by dashboardViewModel.loanAmount.collectAsStateWithLifecycle()
    val bottomSheetLevelInfo by dashboardViewModel.bottomSheetLevelInfo.collectAsStateWithLifecycle()
    val bottomSheetSuccessLoan by dashboardViewModel.bottomSheetSuccessRequestLoan.collectAsStateWithLifecycle()
    val bottomSheetNotEnoughCollateral by dashboardViewModel.bottomSheetNotEnoughCollateral.collectAsStateWithLifecycle()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.img_create_loan),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                tint = Color.Unspecified
            )
        }

        HorizontalDivider(thickness = 1.dp, color = BaseColor.Black)

        LevelInterest(level = dashboardViewModel.getLevelUser().toString()){
            dashboardViewModel.updateBottomBarLevelInfo(true)
        }

        Text(
            text = "Loan Data",
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = BaseColor.JetBlack.Normal,
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(0))
                .background(BaseColor.JetBlack.Normal)
                .padding(2.dp)
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
    if (bottomSheetLevelInfo){
        ShowBottomLevelInfo(onDismiss = {
            dashboardViewModel.updateBottomBarLevelInfo(false)
        })
    }
    if (bottomSheetSuccessLoan){
        SuccessTransferSheet(
            title = "Success",
            desc = "Success Request loan"
        ) {
            dashboardViewModel.updateBottomSuccessRequestLoan(false)
            onBackClick()
        }
    }
    if (bottomSheetNotEnoughCollateral){
        ErrorBottomSheet(
            title = "Failed",
            desc = "Not enough collateral"
        ) {
            dashboardViewModel.updateBottomBarNotEnoughCollateral(false)
        }
    }
}

@Composable
fun StickyContentLoan(dashboardViewModel: DashboardViewModel) {
    val totalCollateral by dashboardViewModel.totalCollateral.collectAsStateWithLifecycle()
    val loanAmount by dashboardViewModel.loanAmount.collectAsStateWithLifecycle()

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
                        text = "$totalCollateral USDCx",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = BaseColor.JetBlack.Normal
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        dashboardViewModel.createLoanRequestAsBorrower(loanAmount)
                    },
                    modifier = Modifier
                        .height(48.dp)
                        .width(139.dp),
                    enabled = totalCollateral.isNotEmpty(),
                    colors = ButtonColors(
                        containerColor = BaseColor.JetBlack.Normal,
                        contentColor = BaseColor.JetBlack.Normal,
                        disabledContainerColor = BaseColor.JetBlack.Minus60,
                        disabledContentColor = BaseColor.JetBlack.Minus60
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ShowBottomLevelInfo(onDismiss : () -> Unit){
    BaseBottomSheet(
        titleHeader = "Level Info",
        onDismiss = {
            onDismiss()
        }
    ){
        LevelList()
    }
}

@Composable
fun LevelList() {
    val levels = listOf(
        LevelData(1, "Active", 1, "Base Rate: 110%", "Level interest: 5%"),
        LevelData(2, "Reliable", 1500, "Base Rate: 110%", "Level interest: 4%"),
        LevelData(3, "Consistent", 3500, "Base Rate: 110%", "Level interest: 3%"),
        LevelData(4, "Trusted", 7500, "Base Rate: 110%", "Level interest: 2%"),
        LevelData(5, "Prime", 10000, "Base Rate: 110%", "Level interest: 1%")
    )

    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(0))
                .background(Color(0xFFF3F4F6))
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
