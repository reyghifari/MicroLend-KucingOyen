package com.kucingoyen.dashboard.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kucingoyen.core.theme.BaseColor
import com.kucingoyen.dashboard.DashboardViewModel
import kotlinx.coroutines.launch

private object LoanScreenTheme {
    val TabSelectedColor = BaseColor.White
    val TabUnselectedColor = Color.Gray
    val TabBackgroundColor = Color.White
    val DividerColor = Color(0xFFE0E0E0)
    val BackgroundGray = Color(0xFFF5F5F5)
}

enum class LoanTab(val title: String) {
    Requests("Loan Requests"),
    Active("Active Loans"),
    Completed("Completed");

}

@Composable
fun LoanScreen(
    dashboardViewModel: DashboardViewModel,
    paddingValues: PaddingValues,
    onRequestLoan: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BaseColor.JetBlack.Minus80)
            .statusBarsPadding()
            .padding(paddingValues),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoanPagerScreen(dashboardViewModel){
            onRequestLoan()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class) // Depending on your Compose BOM version
@Composable
fun LoanPagerScreen(
    dashboardViewModel: DashboardViewModel,
    onRequestLoan: () -> Unit = {}
) {
    val tabs = LoanTab.entries
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = BaseColor.JetBlack.Normal,
            contentColor = BaseColor.White,
            divider = {
                HorizontalDivider(thickness = 1.dp, color = LoanScreenTheme.DividerColor)
            },
            indicator = { tabPositions ->
                if (pagerState.currentPage < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                        height = 3.dp,
                        color = LoanScreenTheme.TabSelectedColor
                    )
                }
            }
        ) {
            tabs.forEachIndexed { index, tab ->
                val isSelected = pagerState.currentPage == index

                Tab(
                    selected = isSelected,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            text = tab.title,
                            color = if (isSelected) LoanScreenTheme.TabSelectedColor else LoanScreenTheme.TabUnselectedColor,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontFamily = FontFamily.Monospace,
                        )
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { pageIndex ->
           when(pageIndex){
               0 -> RequestLoanScreen( dashboardViewModel, onRequestLoan)
               1 -> ActiveLoanScreen()
               2 -> CompletedLoanScreen()
           }
        }
        Spacer(modifier = Modifier.height(108.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun LoanPagerScreenPreview() {
    MaterialTheme {

    }
}