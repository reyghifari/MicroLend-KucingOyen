package com.kucingoyen.dashboard.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kucingoyen.core.theme.BaseColor
import com.kucingoyen.dashboard.TextGray

@Composable
fun ActionButton(icon: ImageVector, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(BaseColor.White), // Button Color
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = label, tint = BaseColor.OceanBlue.Normal)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, color = TextGray, fontSize = 12.sp)
    }
}