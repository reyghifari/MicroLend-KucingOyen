package com.kucingoyen.auth.screens.login.bottomsheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kucingoyen.auth.screens.AuthViewModel
import com.kucingoyen.core.components.CustomTextField
import com.kucingoyen.core.components.bottomsheet.BaseBottomSheet
import com.kucingoyen.core.theme.BaseColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetLogin(authViewModel: AuthViewModel, onLogin : () -> Unit) {
    val showLoginSheet by authViewModel.showSheetLogin.collectAsStateWithLifecycle()

    val InputBackground = Color(0xFFF3F4F6)
    val ButtonDisabledColor = Color(0xFFC8C8C8)
    val TextLabelColor = Color(0xFF0D3E38)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

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
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Hello, Welcome back",
                    fontSize = 18.sp,
                    color = BaseColor.MicroLend.TextPurple,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )

                Spacer(modifier = Modifier.height(16.dp))
                CustomTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email",
                    placeholder = "Masukkan Email",
                    containerColor = InputBackground,
                    labelColor = TextLabelColor
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 2. Input Password
                CustomTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Password",
                    placeholder = "Masukkan password",
                    containerColor = InputBackground,
                    labelColor = TextLabelColor,
                    isPassword = true,
                    isPasswordVisible = passwordVisible,
                    onVisibilityChange = { passwordVisible = !passwordVisible },
                    iconColor = BaseColor.Irish.Minus70
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { onLogin() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(20),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonDisabledColor,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Text(
                        text = "Sign In",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}