package com.kucingoyen.dashboard.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kucingoyen.core.theme.BaseColor
import com.kucingoyen.dashboard.screen.component.LoanDetailsCard
import com.kucingoyen.dashboard.screen.component.LoanModel

@Composable
fun RequestLoanScreen(dashboardViewModel: DashboardViewModel, onRequestLoan: () -> Unit) {
    val loans = remember {
        listOf(
            LoanModel("1", 5000.0, 10.0, "REQUEST", "Jan 25, 2026", 5500.0),
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Button(
            onClick = { onRequestLoan() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BaseColor.JetBlack.Normal,
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Request New Loan",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = Color.White
            )
        }

        LazyColumn {
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
}