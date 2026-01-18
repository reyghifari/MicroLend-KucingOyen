package com.kucingoyen.microlend

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.navigation.compose.rememberNavController
import com.kucingoyen.core.components.error.ErrorGeneralHost
import com.kucingoyen.core.theme.BaseTheme
import com.kucingoyen.core.theme.FixedFontScaleContent
import com.kucingoyen.core.utils.loading.LoadingHost
import com.kucingoyen.navigation.BaseNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val focusManager = LocalFocusManager.current
            FixedFontScaleContent {
                BaseTheme {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .pointerInput(Unit) {
                                detectTapGestures {
                                    focusManager.clearFocus()
                                }
                            }) {
                        MainContent()
                    }
                }
            }
        }
    }
}

@Composable
fun MainContent(){
    val navController = rememberNavController()
    LoadingHost()
    ErrorGeneralHost(navController)
    BaseNavGraph(navController)
}

