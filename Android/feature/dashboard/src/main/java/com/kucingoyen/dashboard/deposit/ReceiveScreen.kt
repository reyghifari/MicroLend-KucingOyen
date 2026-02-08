package com.kucingoyen.dashboard.deposit

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kucingoyen.core.R
import com.kucingoyen.core.components.bottomsheet.SuccessTransferSheet
import com.kucingoyen.core.theme.BaseColor
import com.kucingoyen.dashboard.DashboardViewModel

@Composable
fun ReceiveContent(
    dashboardViewModel: DashboardViewModel
) {
    val context = LocalContext.current

    val clipboardManager = LocalClipboardManager.current
    val walletAddress = dashboardViewModel.getEmailUser()
    val truncatedAddress = dashboardViewModel.getPartyId()

    val showSuccessRequestSheet by dashboardViewModel.showSuccessRequestSheet.collectAsStateWithLifecycle()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(R.drawable.ic_qr),
            contentDescription = "QR Code",
            modifier = Modifier
                .size(350.dp)
                .background(BaseColor.White, RoundedCornerShape(0))
                .border(2.dp, BaseColor.JetBlack.Minus80, RoundedCornerShape(0)),
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = walletAddress,
            fontSize = 18.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = BaseColor.JetBlack.Normal
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = truncatedAddress,
            fontSize = 14.sp,
            fontFamily = FontFamily.Monospace,
            color = BaseColor.JetBlack.Minus40,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // --- Copy Button ---
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable {
                        clipboardManager.setText(AnnotatedString(walletAddress))
                    }
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(BaseColor.JetBlack.Minus90, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy",
                        tint = BaseColor.JetBlack.Minus40,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Copy",
                    fontSize = 14.sp,
                    color = BaseColor.JetBlack.Minus40
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable {
                        dashboardViewModel.requestBalance("CC", 100)
                    }
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(BaseColor.JetBlack.Minus90, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AttachMoney,
                        contentDescription = "Copy",
                        tint = BaseColor.JetBlack.Minus40,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Request CC",
                    fontSize = 14.sp,
                    color = BaseColor.JetBlack.Minus40
                )
            }


            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable {
                        dashboardViewModel.requestBalance("USD", 10)
                    }
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(BaseColor.JetBlack.Minus90, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AttachMoney,
                        contentDescription = "Copy",
                        tint = BaseColor.JetBlack.Minus40,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Request USDx",
                    fontSize = 14.sp,
                    color = BaseColor.JetBlack.Minus40
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable {
                        val sendIntent = Intent(Intent.ACTION_SEND).apply {
                            putExtra(Intent.EXTRA_TEXT, truncatedAddress)
                            type = "text/plain"
                        }

                        val shareIntent = Intent.createChooser(sendIntent, "Share address via")
                        context.startActivity(shareIntent)
                    }
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(BaseColor.JetBlack.Minus90, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = BaseColor.JetBlack.Minus40,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Share",
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace,
                    color = BaseColor.JetBlack.Minus40
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NetworkIcon(Color(0xFFF0B90B))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "This address supports all assets on Canton Network",
            fontSize = 13.sp,
            color = BaseColor.JetBlack.Minus40,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (showSuccessRequestSheet){
            SuccessTransferSheet(
                title = "Success",
                desc = "Request completed successfully"
            ) {
                dashboardViewModel.updateShowSuccessRequestSheet(false)
            }
        }
    }
}

@Composable
fun NetworkIcon(color: Color) {
    Image(
        modifier = Modifier
            .size(48.dp),
        painter = painterResource(R.drawable.ic_canton_logo),
        contentDescription = "Canton Logo"
    )
}