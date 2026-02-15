package com.kucingoyen.dashboard.screen.funding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kucingoyen.core.components.LoanRequestItem
import com.kucingoyen.dashboard.screen.component.NavbarMicroLend
import com.kucingoyen.entity.model.LoanRequest

@Composable
fun ProvideFundingScreen(modifier: Modifier = Modifier, onClickDetail : () -> Unit) {
    val a = listOf(LoanRequest.get(), LoanRequest.get(), LoanRequest.get())
    Column {
        NavbarMicroLend(title = "List loan")
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(a) { loan ->
                LoanRequestItem(loan){
                    onClickDetail()
                }
            }
        }
    }


}


@Preview(showBackground = true)
@Composable
private fun ProvideFundingScreenPreview() {
    ProvideFundingScreen {}
}
