package com.drish.moneytracker.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.drish.moneytracker.ui.theme.Coffee
import com.drish.moneytracker.ui.theme.WarmBrown

@Composable
fun AnimatedBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier) {
        FloatingBlob(
            color = Coffee.copy(alpha = 0.08f),
            size = 300.dp,
            xOffset = (-50).dp,
            yOffset = (-80).dp,
            animationDuration = 6000
        )
        FloatingBlob(
            color = WarmBrown.copy(alpha = 0.10f),
            size = 250.dp,
            xOffset = 180.dp,
            yOffset = 100.dp,
            animationDuration = 8000
        )
        FloatingBlob(
            color = Coffee.copy(alpha = 0.06f),
            size = 200.dp,
            xOffset = 50.dp,
            yOffset = 400.dp,
            animationDuration = 7000
        )
        content()
    }
}

@Composable
private fun FloatingBlob(
    color: Color,
    size: Dp,
    xOffset: Dp,
    yOffset: Dp,
    animationDuration: Int
) {
    val infiniteTransition = rememberInfiniteTransition(label = "blob")
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(animationDuration, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offsetY"
    )
    val offsetX by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 15f,
        animationSpec = infiniteRepeatable(
            animation = tween(animationDuration + 1000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offsetX"
    )

    Box(
        modifier = Modifier
            .offset(x = xOffset + offsetX.dp, y = yOffset + offsetY.dp)
            .size(size)
            .blur(radius = 40.dp)
            .background(color = color, shape = CircleShape)
    )
}
