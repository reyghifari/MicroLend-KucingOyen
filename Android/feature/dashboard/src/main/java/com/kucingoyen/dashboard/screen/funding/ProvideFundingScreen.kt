package com.kucingoyen.dashboard.screen.funding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kucingoyen.core.components.LoanRequestItem
import com.kucingoyen.dashboard.DashboardViewModel
import com.kucingoyen.dashboard.screen.component.NavbarMicroLend

@Composable
fun ProvideFundingScreen(dashboardViewModel: DashboardViewModel, modifier: Modifier = Modifier, onClickDetail : () -> Unit) {
    val listLoanRequest by dashboardViewModel.listLoanRequest.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        dashboardViewModel.listLoanRequestAsLender()
    }

    Column {
        NavbarMicroLend(title = "List loan")
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(listLoanRequest) { loan ->
                LoanRequestItem(loan){
                    dashboardViewModel.setDetailLoanRequest(it)
                    onClickDetail()
                }
            }
        }
    }


}