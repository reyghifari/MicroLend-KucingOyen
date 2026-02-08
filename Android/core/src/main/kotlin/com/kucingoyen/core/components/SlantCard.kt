package com.kucingoyen.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kucingoyen.core.theme.BaseColor

@Composable
fun SlantCard(
	modifier: Modifier = Modifier,
	slantSide: SlantSide = SlantSide.Bottom,
	slantDirection: SlantDirection = SlantDirection.Right,
	slantSize: Dp = VelloSlantCardDefaults.SlantSize,
	backgroundColor: Color = VelloSlantCardDefaults.backgroundColor(),
	content: (@Composable BoxScope.() -> Unit)? = null,
) {
	val slantShape =
		rememberSlantShape(
			slantSide = slantSide,
			slantDirection = slantDirection,
			slantSize = slantSize,
		)

	Box(
		modifier =
			modifier
				.clip(slantShape)
				.background(backgroundColor),
	) {
		content?.let { view ->
			view()
		}
	}
}

enum class SlantSide {
	Top,
	Bottom,
}

enum class SlantDirection {
	Left,
	Right,
}

object VelloSlantCardDefaults {
	val SlantSize = 32.dp

	@Composable
	fun backgroundColor() = BaseColor.JetBlack.Normal
}

@Composable
private fun rememberSlantShape(
	slantSide: SlantSide,
	slantDirection: SlantDirection,
	slantSize: Dp,
): Shape {
	val density = LocalDensity.current

	return GenericShape { size, _ ->
		val slantPx = with(density) { slantSize.toPx() }

		when (slantSide) {
			SlantSide.Bottom -> {
				drawBottomSlant(size.width, size.height, slantPx, slantDirection)
			}

			SlantSide.Top -> {
				drawTopSlant(size.width, size.height, slantPx, slantDirection)
			}
		}

		close()
	}
}

private fun Path.drawBottomSlant(
	width: Float,
	height: Float,
	slantPx: Float,
	direction: SlantDirection,
) {
	moveTo(0f, 0f)
	lineTo(width, 0f)

	when (direction) {
		SlantDirection.Right -> {
			lineTo(width, height - slantPx)
			lineTo(0f, height)
		}

		SlantDirection.Left -> {
			lineTo(width, height)
			lineTo(0f, height - slantPx)
		}
	}
}

private fun Path.drawTopSlant(
	width: Float,
	height: Float,
	slantPx: Float,
	direction: SlantDirection,
) {
	when (direction) {
		SlantDirection.Right -> {
			moveTo(0f, slantPx)
			lineTo(width, 0f)
		}

		SlantDirection.Left -> {
			moveTo(0f, 0f)
			lineTo(width, slantPx)
		}
	}

	lineTo(width, height)
	lineTo(0f, height)
}
