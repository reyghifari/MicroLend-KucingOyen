package com.kucingoyen.core.components.bottomsheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kucingoyen.core.components.BaseButton
import com.kucingoyen.core.components.BaseOutlineButton
import com.kucingoyen.core.theme.BaseColor
import com.kucingoyen.core.theme.BaseDp
import com.kucingoyen.core.theme.BaseFont


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetRowButton(
    bottomSheetState: SheetState,
    useImage: Boolean = false,
    image: Int = 0,
    title: String = "",
    description: String = "",
    annotatedDescription: AnnotatedString? = null,
    buttonTitleOne: String = "",
    buttonTitleTwo: String = "",
    onClickButtonOne: () -> Unit,
    onClickButtonTwo: () -> Unit,
    onDismiss: () -> Unit,
) {
    BaseBottomSheet(
        bottomSheetState,
        onDismiss,
        titleHeader = title
    ) {
        Column(
            modifier = Modifier
                .padding(start = BaseDp.dp16, end = BaseDp.dp16, bottom = BaseDp.dp16)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = title,
                modifier = Modifier.fillMaxWidth().height(200.dp).padding(16.dp)
            )
        }
    }
}