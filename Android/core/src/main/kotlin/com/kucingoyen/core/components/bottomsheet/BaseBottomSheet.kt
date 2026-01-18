package com.kucingoyen.core.components.bottomsheet

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.kucingoyen.core.theme.BaseColor
import com.kucingoyen.core.theme.BaseDp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseBottomSheet(
    bottomSheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    onDismiss: () -> Unit,
    containerColor: Color = BaseColor.JetBlack.Minus90,
    content: @Composable ColumnScope.() -> Unit
) {
    ModalBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState,
        shape = RoundedCornerShape(topStart = BaseDp.dp32, topEnd = BaseDp.dp32),
        containerColor = containerColor,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                width = BaseDp.dp48,
                height = BaseDp.dp6,
                color = BaseColor.JetBlack.Minus70,
            )
        }
    ) {
        content()
    }
}
