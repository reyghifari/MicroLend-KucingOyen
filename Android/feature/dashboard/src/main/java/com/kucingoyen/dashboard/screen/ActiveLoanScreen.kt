package com.kucingoyen.dashboard.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kucingoyen.dashboard.screen.component.LoanDetailsCard
import com.kucingoyen.dashboard.screen.component.LoanModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveLoanScreen(modifier: Modifier = Modifier) {
    val loans = remember {
        listOf(
            LoanModel("1", 5000.0, 10.0, "IN PROGRESS", "Jan 25, 2026", 5500.0),
            LoanModel("2", 1200.0, 10.0, "IN PROGRESS", "Dec 10, 2025", 1320.0),
            LoanModel("3", 10000.0, 10.0,  "OVERDUE", "Jan 01, 2026", 11000.0),
            LoanModel("4", 350.0, 10.0,  "IN PROGRESS", "Feb 14, 2026", 385.0)
        )
    }

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(loans) { loan ->
            LoanDetailsCard(
                loanAmount = loan.amount,
                interestRate = loan.interest,
                statusLoan = loan.status,
                dueDate = loan.dueDate,
                collateral = loan.collateral,
                onPay = {

                }
            )
        }
    }
}