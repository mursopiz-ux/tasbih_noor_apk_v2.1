package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.NoorGold
import com.example.ui.theme.NoorGoldBright
import com.example.ui.theme.NoorGoldGlow
import com.example.ui.theme.NoorSoftGold
import kotlin.math.min

@Composable
fun NoorLightCounter(
    count: Int,
    target: Int,
    pulseTrigger: Long,
    animationIntensity: String,
    modifier: Modifier = Modifier
) {
    val progress = if (target > 0) {
        (count.toFloat() / target.toFloat()).coerceIn(0f, 1f)
    } else {
        0f
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = if (animationIntensity == "MINIMAL") {
            tween(durationMillis = 50)
        } else {
            tween(durationMillis = 280, easing = FastOutSlowInEasing)
        },
        label = "noor_progress_anim"
    )

    val pulseAnim = remember { Animatable(0f) }
    LaunchedEffect(pulseTrigger) {
        if (pulseTrigger > 0 && animationIntensity != "MINIMAL") {
            pulseAnim.snapTo(if (animationIntensity == "FULL") 1f else 0.5f)
            pulseAnim.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing)
            )
        }
    }

    val isTargetReached = count >= target && target > 0
    val percentage = if (target > 0) ((count.toFloat() / target.toFloat()) * 100).toInt().coerceAtMost(100) else 0

    Box(
        modifier = modifier
            .size(252.dp)
            .testTag("noor_light_counter_box"),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(252.dp)) {
            val strokeWidth = 10.dp.toPx()
            val diameter = min(size.width, size.height) - strokeWidth * 2
            val radius = diameter / 2f
            val center = Offset(size.width / 2f, size.height / 2f)

            drawCircle(
                color = NoorGold.copy(alpha = 0.14f),
                radius = radius,
                center = center,
                style = Stroke(width = strokeWidth * 0.7f)
            )

            val glowAlpha = (0.16f + pulseAnim.value * 0.35f + (if (isTargetReached) 0.25f else 0f)).coerceIn(0f, 0.7f)
            drawCircle(
                color = NoorGoldGlow.copy(alpha = glowAlpha),
                radius = radius + strokeWidth * 0.8f,
                center = center,
                style = Stroke(width = strokeWidth * 1.5f)
            )

            drawCircle(
                color = NoorGold.copy(alpha = 0.07f),
                radius = radius - strokeWidth * 1.2f,
                center = center,
                style = Stroke(width = 1.dp.toPx())
            )

            val sweepAngle = animatedProgress * 360f
            if (sweepAngle > 0f) {
                val goldGradient = Brush.sweepGradient(
                    listOf(
                        NoorGold,
                        NoorSoftGold,
                        NoorGoldBright,
                        NoorGold
                    ),
                    center = center
                )

                drawArc(
                    brush = Brush.radialGradient(
                        colors = listOf(NoorGoldBright.copy(alpha = 0.4f), Color.Transparent),
                        center = center,
                        radius = radius + strokeWidth
                    ),
                    startAngle = -90f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = strokeWidth * 1.8f, cap = StrokeCap.Round)
                )

                drawArc(
                    brush = goldGradient,
                    startAngle = -90f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "TARGET: $target",
                style = MaterialTheme.typography.labelMedium,
                color = NoorGold.copy(alpha = 0.85f),
                letterSpacing = 1.2.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "$count",
                fontFamily = CinzelFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 56.sp,
                lineHeight = 60.sp,
                color = if (isTargetReached) NoorGoldBright else MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.testTag("counter_numeric_display")
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "$percentage%",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (isTargetReached) NoorGold else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
