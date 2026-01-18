package com.kucingoyen.dashboard.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kucingoyen.core.theme.BaseColor

@Composable
fun InputCard(
    nominalLoan: String,
    onInputChange: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .background(BaseColor.White, shape = RoundedCornerShape(12.dp))
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Nominal Loan",
                color = BaseColor.JetBlack.Normal,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                BasicTextField(
                    value = nominalLoan,
                    onValueChange = { input ->
                       onInputChange(input)
                    },
                    textStyle = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = BaseColor.JetBlack.Minus20,
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    cursorBrush = SolidColor(Color.Black),
                    decorationBox = { innerTextField ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (nominalLoan.isEmpty()) {
                                Text(
                                    text = "0",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BaseColor.JetBlack.Minus20,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            innerTextField()
                        }
                    },
                    modifier = Modifier.width(IntrinsicSize.Min)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "$",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = if (nominalLoan.isEmpty())BaseColor.JetBlack.Minus20 else BaseColor.JetBlack.Normal
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGoldInput() {
    var weight by remember { mutableStateOf("10") }

    Box(modifier = Modifier.padding(16.dp).background(Color.White)) {
        InputCard(
            nominalLoan = weight,
            onInputChange = { weight = it }
        )
    }
}