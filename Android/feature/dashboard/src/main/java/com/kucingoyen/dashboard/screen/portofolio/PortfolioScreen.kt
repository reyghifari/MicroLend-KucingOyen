package com.kucingoyen.dashboard.screen.portofolio

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kucingoyen.core.R
import com.kucingoyen.core.components.FundedCard
import com.kucingoyen.core.theme.BaseColor
import com.kucingoyen.dashboard.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioScreen(
    dashboardViewModel: DashboardViewModel,
    onClickRequestLoan : () -> Unit = {},
    onClickFundLoan : () -> Unit = {}
) {
    val tabs = listOf( "Borrower Portfolio", "Lender Portfolio")
    var selectedTabIndex by remember { mutableIntStateOf(1) }

    val listFunded by dashboardViewModel.listMyFunded.collectAsState()
    val listMyLoan by dashboardViewModel.listMyLoan.collectAsState()

    val isLenderTab = selectedTabIndex == 0
    val currentList = if (isLenderTab) listMyLoan else listFunded

    LaunchedEffect(Unit) {
        dashboardViewModel.myListFunded()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Portfolio",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White)
        ) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.White,
                contentColor = BaseColor.JetBlack.Normal,
                indicator = { tabPositions ->
                    if (selectedTabIndex < tabPositions.size) {
                        TabRowDefaults.Indicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = BaseColor.JetBlack.Normal,
                        )
                    }
                },
                divider = { HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f)) }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                color = if (selectedTabIndex == index) BaseColor.JetBlack.Normal else Color.Gray,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (currentList.isEmpty()) {
                    EmptyPortfolioState(
                        isLender = isLenderTab,
                        onButtonClick = {
                            if (isLenderTab){
                                onClickRequestLoan()
                            }else{
                                onClickFundLoan()
                            }
                        }
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(currentList) { loan ->
                            FundedCard(
                                cardImageRes = R.drawable.img_create_loan,
                                status = loan.status,
                                loan = loan.loanAmount.toString(),
                                expiredDate = loan.endTime,
                                onClick = {

                                }
                            )
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun EmptyPortfolioState(isLender: Boolean, onButtonClick : () -> Unit) {
    
    val message = if (isLender) "No Lending Activity" else "No Loan History"
    val buttonText = if (isLender) "Start Lending" else "Apply for Loan"
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(24.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_empty),
            contentDescription = "Empty State Illustration",
            modifier = Modifier.size(160.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = message,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontFamily = FontFamily.Monospace
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onButtonClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BaseColor.JetBlack.Normal
            ),
            shape = RoundedCornerShape(0)
        ) {
            Text(
                text = buttonText,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}