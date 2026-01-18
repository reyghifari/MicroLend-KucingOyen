package com.kucingoyen.auth.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.kucingoyen.core.R
import com.kucingoyen.core.theme.BaseColor
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navigateToLogin : () -> Unit){

    LaunchedEffect(key1 = true) {
        delay(2000)
        navigateToLogin()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BaseColor.White)
    ) {
        Column {
            Spacer(Modifier.weight(0.5f))
            Image(
                painter = painterResource(id = R.drawable.img_microlend_logo),
                contentDescription = "Splash Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.weight(0.5f))
        }
    }
}