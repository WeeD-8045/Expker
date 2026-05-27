package com.example.expker.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DonutChart(
    data: List<Pair<Color, Float>>,
    totalAmount: String,
    month: String,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(220.dp).padding(16.dp)) {
            var startAngle = -90f
            data.forEach { (color, percentage) ->
                if (percentage > 0) {
                    val sweepAngle = (percentage * 360f) - 4f // Small gap between segments
                    drawArc(
                        color = color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        style = Stroke(width = 32f, cap = StrokeCap.Round)
                    )
                    startAngle += sweepAngle + 4f
                }
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$$totalAmount",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold
            )
            if (month.isNotEmpty()) {
                Text(
                    text = month,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Text(
                    text = "Total Spent",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun SimpleLineChart(
    incomeData: List<Float>,
    expenseData: List<Float>,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val maxVal = (incomeData + expenseData).maxOrNull()?.let { it * 1.2f } ?: 1f
        
        // Draw grid lines
        val gridCount = 4
        for (i in 0..gridCount) {
            val y = height - (i * height / gridCount)
            drawLine(
                color = surfaceVariant,
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 2f
            )
        }

        val incomePath = androidx.compose.ui.graphics.Path()
        val expensePath = androidx.compose.ui.graphics.Path()
        
        incomeData.forEachIndexed { index, value ->
            val x = index * (width / (incomeData.size - 1))
            val y = height - (value / maxVal * height)
            if (index == 0) incomePath.moveTo(x, y) else incomePath.lineTo(x, y)
        }
        
        expenseData.forEachIndexed { index, value ->
            val x = index * (width / (expenseData.size - 1))
            val y = height - (value / maxVal * height)
            if (index == 0) expensePath.moveTo(x, y) else expensePath.lineTo(x, y)
        }
        
        drawPath(
            path = incomePath,
            color = secondaryColor,
            style = Stroke(width = 6f, cap = StrokeCap.Round)
        )
        
        drawPath(
            path = expensePath,
            color = primaryColor,
            style = Stroke(width = 6f, cap = StrokeCap.Round)
        )

        // Draw data points for expenses
        expenseData.forEachIndexed { index, value ->
            val x = index * (width / (expenseData.size - 1))
            val y = height - (value / maxVal * height)
            drawCircle(
                color = primaryColor,
                radius = 6f,
                center = Offset(x, y)
            )
            drawCircle(
                color = Color.White,
                radius = 3f,
                center = Offset(x, y)
            )
        }
    }
}
