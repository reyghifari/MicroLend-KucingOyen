package com.kucingoyen.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
	primary = BaseColor.Primary,
	secondary = BaseColor.Secondary,
	tertiary = BaseColor.Tertiary,
    background = BaseColor.White,
    surface = BaseColor.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
)

@Composable
fun BaseTheme(
	content: @Composable () -> Unit
) {
	MaterialTheme(
		colorScheme = LightColorScheme,
		shapes = Shapes,
		content = content
	)
}

