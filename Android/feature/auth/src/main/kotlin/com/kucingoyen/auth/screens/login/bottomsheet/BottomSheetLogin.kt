package com.kucingoyen.auth.screens.login.bottomsheet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kucingoyen.auth.screens.AuthViewModel
import com.kucingoyen.core.R
import com.kucingoyen.core.components.bottomsheet.BaseBottomSheet
import com.kucingoyen.core.theme.BaseColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetLogin(
    authViewModel: AuthViewModel,
    onGoogleLogin: () -> Unit
) {
    val showLoginSheet by authViewModel.showSheetLogin.collectAsStateWithLifecycle()
    val context = LocalContext.current
    if (showLoginSheet){
        BaseBottomSheet(
            bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            onDismiss = {
                authViewModel.updateShowSheetLogin(false)
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Hello, Welcome back",
                    fontSize = 18.sp,
                    color = BaseColor.MicroLend.TextPurple,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedButton(
                    onClick = {
                        authViewModel.loginWithGoogle(context, onSuccess = {
                            onGoogleLogin()
                        })},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(0),
                    border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_google_logo),
                        contentDescription = "Google Logo",
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "Sign in with Google",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black.copy(alpha = 0.8f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}