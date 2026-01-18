package com.kucingoyen.core.utils.loading

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.kucingoyen.core.components.LoadingBase

@Composable
fun LoadingHost() {
    var show by remember { mutableStateOf<Boolean>(false) }

    LaunchedEffect(Unit) {
        LoadingAction.init { isShow ->
            show = isShow
        }
    }

    LoadingBase(show)
}
