package com.kucingoyen.dashboard.screen.funding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kucingoyen.core.R
import com.kucingoyen.core.components.BaseButtonSlider
import com.kucingoyen.core.components.InfoRow
import com.kucingoyen.core.theme.BaseColor
import com.kucingoyen.dashboard.screen.Divider
import com.kucingoyen.dashboard.screen.component.NavbarMicroLend

@Composable
fun DetailProvideFundScreen(
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BaseColor.White)
    ) {
        NavbarMicroLend(title = "Fund Loan", onBack = {onBackClick()})
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.img_send_money),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                tint = Color.Unspecified
            )
        }

        HorizontalDivider(thickness = 1.dp, color = BaseColor.Black)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {

            InfoRow(
                label = "Borrower Address",
                value = "Alice::162385921321"
            )

            Spacer(modifier = Modifier.height(20.dp))

            InfoRow(
                label = "Borrower Level",
                value = "1",
            )

            Spacer(modifier = Modifier.height(16.dp))

            Divider()

            Spacer(modifier = Modifier.height(16.dp))

            InfoRow(
                label = "Loan Amount",
                value = "5,000 CC",
                fontSize = 22.sp,
                isBold = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Divider()

            Spacer(modifier = Modifier.height(28.dp))

            InfoRow(
                label = "Duration Loan",
                value = "30 Days"
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoRow(
                label = "Interest Rate",
                value = "10%(Base Rate) + 5%(Level 1)"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Divider()

            Spacer(modifier = Modifier.height(16.dp))

            InfoRow(
                label = "Collateral Amount",
                value = "1000 USDCx",
                fontSize = 22.sp,
                isBold = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Divider()

            Spacer(modifier = Modifier.height(16.dp))

            BaseButtonSlider(
                onConfirmed = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailProvideFundScreenPreview() {
    DetailProvideFundScreen {

    }
}
