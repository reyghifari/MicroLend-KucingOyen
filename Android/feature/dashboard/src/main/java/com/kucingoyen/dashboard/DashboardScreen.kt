package com.kucingoyen.dashboard

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kucingoyen.core.theme.BaseColor
import com.kucingoyen.dashboard.screen.HomeScreen
import com.kucingoyen.dashboard.screen.ProfileScreen
import com.kucingoyen.dashboard.screen.component.WalletBottomBar
import com.kucingoyen.dashboard.screen.portofolio.PortfolioScreen
import com.kucingoyen.entity.model.Transaction

val TextGray = Color(0xFFAAAAAA)

@Composable
fun DashboardScreen(
    dashboardViewModel: DashboardViewModel = hiltViewModel(),
    requestLoan: () -> Unit = {},
    onClickSend : () -> Unit = {},
    onClickDeposit : () -> Unit = {},
    onClickTransaction: (Transaction) -> Unit = {}
) {
    val selectedBar by dashboardViewModel.bottomBarSelected.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = BaseColor.White,
        modifier = Modifier.statusBarsPadding(),
        bottomBar = { WalletBottomBar(dashboardViewModel) }
    ) { paddingValues ->
        when(selectedBar){
            0 -> {
                HomeScreen(
                    dashboardViewModel, paddingValues, onClickSend = onClickSend,
                    onClickDeposit = onClickDeposit,
                    onClickTransaction = onClickTransaction
                )
            }
            1 -> {
                PortfolioScreen()
            }
            2 -> {
                ProfileScreen(dashboardViewModel, paddingValues)
            }
        }
    }
}