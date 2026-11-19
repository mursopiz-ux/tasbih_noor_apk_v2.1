package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.ui.theme.NoorGold
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun IslamicGeometricDeco(
    modifier: Modifier = Modifier,
    color: Color = NoorGold.copy(alpha = 0.12f)
) {
    Canvas(modifier = modifier) {
        val centerX = size.width / 2f
        val centerY = size.height / 2f
        val radiusOuter = size.minDimension * 0.44f
        val radiusInner = radiusOuter * 0.55f

        val path = Path()
        val points = 8
        val totalSteps = points * 2

        for (i in 0 until totalSteps) {
            val radius = if (i % 2 == 0) radiusOuter else radiusInner
            val angle = (i * Math.PI / points) - (Math.PI / 2)
            val x = centerX + (radius * cos(angle)).toFloat()
            val y = centerY + (radius * sin(angle)).toFloat()
            if (i == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }
        path.close()

        drawPath(
            path = path,
            color = color,
            style = Stroke(width = 1.2.dp.toPx())
        )

        drawCircle(
            color = color.copy(alpha = color.alpha * 0.7f),
            radius = radiusInner * 0.5f,
            center = Offset(centerX, centerY),
            style = Stroke(width = 1.dp.toPx())
        )
    }
}
