package com.kucingoyen.core.components.bottomsheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.kucingoyen.core.components.HeaderTitle
import com.kucingoyen.core.theme.BaseColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseBottomSheet(
    bottomSheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    onDismiss: () -> Unit,
    titleHeader : String = "",
    containerColor: Color = BaseColor.JetBlack.Minus90,
    content: @Composable ColumnScope.() -> Unit
) {
    val scope = rememberCoroutineScope()
    ModalBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState,
        dragHandle = null,
        containerColor = BaseColor.White,
        shape = RectangleShape,
    ) {
        Column {
            HeaderTitle(
                textHeader = titleHeader,
                onClickIcon = {
                    scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                        if (!bottomSheetState.isVisible) {
                            onDismiss()
                        }
                    }
                },
            )
            Spacer(Modifier.height(16.dp))
            content()
        }
    }
}
