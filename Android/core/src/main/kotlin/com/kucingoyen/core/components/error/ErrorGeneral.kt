package com.kucingoyen.core.components.error

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.kucingoyen.core.components.bottomsheet.BottomSheetRowButton
import com.kucingoyen.entity.model.ErrorModelData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorGeneralHost(navController: NavController) {

    var show by remember { mutableStateOf(false) }
    var errorModel by remember { mutableStateOf<ErrorModelData?>(null) }
    var handleErrorFn: ((ErrorModelData) -> Unit)? by remember { mutableStateOf(null) }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf(errorModel?.messageError.toString()) }
    var buttonTitleOne by remember { mutableStateOf("") }


    val buttonTitleTwo by remember { mutableStateOf("") }
    val onClickButtonOne: () -> Unit
    val onClickButtonTwo: () -> Unit
    val onDismiss: () -> Unit
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(Unit) {
        ErrorGeneralAction.init(
            showHandler = { isShow, error, handleError ->
                show = isShow
                errorModel = error
                handleErrorFn = handleError
            },
        )
    }

    when (errorModel?.code) {
        else -> {
            title = errorModel?.title ?: ""
            buttonTitleOne = errorModel?.buttonTitle ?: ""
            description =  errorModel?.messageError ?: ""
            onClickButtonOne = {
                show = false
                errorModel?.let { handleErrorFn?.invoke(it) }
            }
            onClickButtonTwo = {}
            onDismiss = {
                show = false
                errorModel?.let { handleErrorFn?.invoke(it) }
            }
        }
    }

    if (show) {
        BottomSheetRowButton(
            useImage = true,
            image = 0,
            title = title,
            description = description,
            buttonTitleOne = buttonTitleOne,
            buttonTitleTwo = buttonTitleTwo,
            onClickButtonOne = { onClickButtonOne.invoke() },
            onClickButtonTwo = { onClickButtonTwo.invoke() },
            onDismiss = { onDismiss.invoke() },
            bottomSheetState = sheetState
        )
    }
}
