package com.kucingoyen.core.components

import android.graphics.fonts.FontFamily
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.kucingoyen.core.R
import com.kucingoyen.core.theme.BaseColor
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

// Constants
private object SliderDefaults {
    val Height = 56.dp
    val HorizontalPadding = 8.dp
    val DotTextureWidth = 200.dp
    val DotSize = 1.dp
    val DotGap = 3.dp
    const val CONFIRM_THRESHOLD = 0.95f
    const val ANIMATION_DURATION = 500
    const val DOT_MAX_ALPHA = 0.25f
}

@Composable
fun BaseButtonSlider(
    modifier: Modifier = Modifier,
    idleText: String = "Slide to Fund Loan",
    confirmedText: String = "Let's go!",
    thumbSize: Dp = 40.dp,
    onConfirmed: (Boolean) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val offsetX = remember { Animatable(0f) }
    var isConfirm by remember { mutableStateOf(false) }

    Blinking(isConfirm)

    BoxWithConstraints(
        modifier =
            modifier
                .height(SliderDefaults.Height)
                .fillMaxWidth()
                .border(width = 2.dp, color = BaseColor.JetBlack.Normal)
                .clipToBounds(),
    ) {
        val boxScope = this
        val measurements =
            remember(boxScope.constraints.maxWidth, thumbSize) {
                SliderMeasurements(
                    widthPx = boxScope.constraints.maxWidth.toFloat(),
                    thumbPx = with(density) { thumbSize.toPx() },
                    horizontalPaddingPx = with(density) { SliderDefaults.HorizontalPadding.toPx() },
                    dotTextureWidthPx = with(density) { SliderDefaults.DotTextureWidth.toPx() },
                )
            }

        val isOnPoint by remember {
            derivedStateOf {
                offsetX.value > measurements.maxOffset * SliderDefaults.CONFIRM_THRESHOLD
            }
        }

        val bgOffset by animateFloatAsState(
            targetValue = offsetX.value,
            animationSpec =
                tween(
                    durationMillis = SliderDefaults.ANIMATION_DURATION,
                    easing = FastOutSlowInEasing,
                ),
            label = "bgOffset",
        )

        // Background layers
        ConfirmedBackground(
            widthPx = measurements.widthPx,
            bgOffset = bgOffset,
            text = confirmedText,
        )

        IdleBackground(
            offsetX = offsetX.value,
            bgOffset = bgOffset,
            text = idleText,
        )

        DotTextureOverlay(
            isOnPoint = isOnPoint,
            offsetX = offsetX.value,
            measurements = measurements,
        )

        ThumbHandle(
            modifier = Modifier.align(Alignment.CenterStart),
            offsetX = offsetX.value,
            maxOffset = measurements.maxOffset,
            thumbSize = thumbSize,
            isOnPoint = isOnPoint,
            onDrag = { dragAmount ->
                scope.launch {
                    offsetX.snapTo(
                        (offsetX.value + dragAmount).coerceIn(0f, measurements.maxOffset),
                    )
                }
            },
            onDragEnd = {
                scope.launch {
                    if (offsetX.value > measurements.maxOffset * SliderDefaults.CONFIRM_THRESHOLD) {
                        offsetX.animateTo(measurements.maxOffset)
                        onConfirmed(isOnPoint)
                        isConfirm = true
                    } else {
                        offsetX.animateTo(0f)
                        isConfirm = false
                    }
                }
            },
        )
    }
}

// Data class to hold measurements
private data class SliderMeasurements(
    val widthPx: Float,
    val thumbPx: Float,
    val horizontalPaddingPx: Float,
    val dotTextureWidthPx: Float,
) {
    val maxOffset: Float = widthPx - thumbPx - (horizontalPaddingPx * 2)
}

@Composable
private fun ConfirmedBackground(
    widthPx: Float,
    bgOffset: Float,
    text: String,
) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .offset { IntOffset((-widthPx + bgOffset).roundToInt(), 0) }
                .background(BaseColor.JetBlack.Normal),
        contentAlignment = Alignment.CenterEnd,
    ) {
        Text(
            text = text,
            color = BaseColor.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(end = 100.dp),
            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
        )
    }
}

@Composable
private fun Blinking(isConfirm: Boolean) {
    AnimatedContent(isConfirm) { isConfirm ->
        if (isConfirm) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp, end = 4.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_blink),
                    contentDescription = "Check",
                )
            }
        }
    }
}

@Composable
private fun IdleBackground(
    offsetX: Float,
    bgOffset: Float,
    text: String,
) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .offset { IntOffset(offsetX.coerceIn(0f, bgOffset).roundToInt(), 0) }
                .background(Color(0xFFFF3E17)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = BaseColor.White,
            fontWeight = FontWeight.Bold,
            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
        )
    }
}

@Composable
private fun DotTextureOverlay(
    isOnPoint: Boolean,
    offsetX: Float,
    measurements: SliderMeasurements,
) {
    Box(
        modifier =
            Modifier
                .width(SliderDefaults.DotTextureWidth)
                .fillMaxHeight()
                .offset {
                    val xOffset =
                        if (isOnPoint) {
                            offsetX.roundToInt()
                        } else {
                            (offsetX - measurements.dotTextureWidthPx + 16 + measurements.thumbPx + measurements.horizontalPaddingPx).roundToInt()
                        }
                    IntOffset(xOffset, 0)
                }
                .background(
                    brush =
                        Brush.horizontalGradient(
                            colors = listOf(Color.Transparent, BaseColor.White),
                        ),
                ),
    ) {
        DotTexture()
    }
}

@Composable
private fun ThumbHandle(
    modifier: Modifier = Modifier,
    offsetX: Float,
    maxOffset: Float,
    thumbSize: Dp,
    isOnPoint: Boolean,
    onDrag: (Float) -> Unit,
    onDragEnd: () -> Unit,
) {
    Box(
        modifier =
            modifier
                .padding(start = SliderDefaults.HorizontalPadding)
                .offset { IntOffset(offsetX.coerceIn(0f, maxOffset).roundToInt(), 0) }
                .size(thumbSize)
                .clip(CircleShape)
                .background( BaseColor.JetBlack.Minus20)
                .pointerInput(maxOffset) {
                    detectDragGestures(
                        onDrag = { change, drag ->
                            change.consume()
                            onDrag(drag.x)
                        },
                        onDragEnd = onDragEnd,
                    )
                },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = if (isOnPoint) Icons.Default.Check else Icons.Default.ChevronRight,
            contentDescription = if (isOnPoint) "Confirmed" else "Slide",
            tint = BaseColor.White,
        )
    }
}

@Composable
private fun DotTexture() {
    val density = LocalDensity.current
    val dotSizePx = with(density) { SliderDefaults.DotSize.toPx() }
    val gapPx = with(density) { SliderDefaults.DotGap.toPx() }

    Canvas(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(end = 1.dp),
    ) {
        val columns = (size.width / gapPx).toInt()
        val rows = (size.height / gapPx).toInt()

        for (x in 0..columns) {
            val progressX = x.toFloat() / columns
            val alpha = SliderDefaults.DOT_MAX_ALPHA * progressX

            for (y in 0..rows) {
                drawCircle(
                    color = BaseColor.JetBlack.Minus20.copy(alpha = alpha),
                    radius = dotSizePx,
                    center = Offset(x * gapPx, y * gapPx),
                )
            }
        }
    }
}

@Preview(name = "SliderButton", showBackground = false)
@Composable
private fun SliderButton() {
    Column {
        BaseButtonSlider (
            onConfirmed = {},
        )
    }
}
