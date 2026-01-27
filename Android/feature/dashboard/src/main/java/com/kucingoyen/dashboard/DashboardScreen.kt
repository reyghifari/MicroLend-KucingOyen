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
import com.kucingoyen.dashboard.screen.LoanScreen
import com.kucingoyen.dashboard.screen.ProfileScreen
import com.kucingoyen.dashboard.screen.component.WalletBottomBar
import com.kucingoyen.dashboard.screen.component.WalletTopBar

val TextGray = Color(0xFFAAAAAA)

@Composable
fun DashboardScreen(
    dashboardViewModel: DashboardViewModel = hiltViewModel(),
    requestLoan: () -> Unit = {},
    onClickSend : () -> Unit = {}
) {
    val selectedBar by dashboardViewModel.bottomBarSelected.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = BaseColor.White,
        topBar = { if (selectedBar == 0) WalletTopBar(dashboardViewModel) },
        modifier = Modifier.statusBarsPadding(),
        bottomBar = { WalletBottomBar(dashboardViewModel) }
    ) { paddingValues ->
        when(selectedBar){
            0 -> {
                HomeScreen(dashboardViewModel, paddingValues){
                    onClickSend()
                }
            }
            1 -> {
                LoanScreen(dashboardViewModel){
                    requestLoan()
                }
            }
            2 -> {
                ProfileScreen(dashboardViewModel)
            }
        }
    }
}