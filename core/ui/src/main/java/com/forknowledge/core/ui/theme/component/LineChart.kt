package com.forknowledge.core.ui.theme.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ChartDataPoint(
    val xProgress: Float, // Normalized x position (0.0 to 1.0)
    val value: Float,     // The actual Y value (e.g., weight)
    val labelDate: String,
    val labelValue: String,
    val pointColor: Color,
    val additionalLabel: String? = null // e.g., "Current you", "Goal Date"
)

@Composable
fun WeightProgressLineChart(
    modifier: Modifier = Modifier,
    dataPoints: List<ChartDataPoint>,
    lineThickness: Dp = 4.dp,
    pointRadius: Dp = 8.dp,
    chartHeight: Dp = 260.dp,
    gradientColors: List<Color> = listOf(Color.Red, Color.Yellow, Color.Green),
    animationDurationMillis: Int = 1500
) {
    val density = LocalDensity.current
    val textMeasurer = rememberTextMeasurer()

    val animationProgress = remember { Animatable(1f) }

    LaunchedEffect(dataPoints) { // Relaunch if dataPoints change
        animationProgress.snapTo(0f) // Reset if data changes
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = animationDurationMillis)
        )
    }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(chartHeight) // Adjust height as needed
    ) {
        val currentProgress = animationProgress.value
        val canvasWidth = size.width
        val canvasHeight = size.height
        val lineThicknessPx = with(density) { lineThickness.toPx() }
        val pointRadiusPx = with(density) { pointRadius.toPx() }

        val minValue = dataPoints.minOfOrNull { it.value } ?: 0f
        val maxValue = dataPoints.maxOfOrNull { it.value } ?: 100f
        val valueRange = if (maxValue - minValue == 0f) 1f else maxValue - minValue

        fun getYPosition(value: Float): Float {
            val padding = canvasHeight * 0.2f
            return canvasHeight - padding - ((value - minValue) / valueRange) * (canvasHeight - 2 * padding)
        }

        // --- Draw the animated line with gradient ---
        val linePath = Path()

        if (dataPoints.isNotEmpty()) {
            // Find the point up to which we should draw based on animationProgress
            // This is a simplified way. For perfect segment-by-segment drawing, it's more complex.
            // A simpler visual effect is to clip the drawing area.

            // Alternative 1: Clipping (Simpler to implement for continuous line)
            clipRect(right = canvasWidth * currentProgress) {
                // Draw the full path, but it will be clipped
                dataPoints.forEachIndexed { index, point ->
                    val x = point.xProgress * canvasWidth
                    val y = getYPosition(point.value)
                    if (index == 0) {
                        linePath.moveTo(x, y)
                    } else {
                        val prevPoint = dataPoints[index - 1]
                        val prevX = prevPoint.xProgress * canvasWidth
                        val prevY = getYPosition(prevPoint.value)
                        val cpx1 = (prevX + x) / 2f
                        val cpy1 = prevY
                        val cpx2 = (prevX + x) / 2f
                        val cpy2 = y
                        linePath.cubicTo(cpx1, cpy1, cpx2, cpy2, x, y)
                    }
                }
                if (!linePath.isEmpty) {
                    drawPath(
                        path = linePath,
                        brush = Brush.horizontalGradient(colors = gradientColors),
                        style = Stroke(width = lineThicknessPx, cap = StrokeCap.Round)
                    )
                }

                // --- Draw points and labels that are within the currentProgress ---
                dataPoints.forEach { point ->
                    if (point.xProgress <= currentProgress) { // Only draw if within animated range
                        val x = point.xProgress * canvasWidth
                        val y = getYPosition(point.value)

                        drawCircle(
                            color = point.pointColor,
                            radius = pointRadiusPx,
                            center = Offset(x, y)
                        )
                        // Only draw labels for points that are fully "revealed"
                        // You might want a slight delay or only show labels when the point is clearly visible.
                        // For simplicity, showing if xProgress <= currentProgress
                        val dateTextLayoutResult = textMeasurer.measure(
                            text = point.labelDate,
                            style = TextStyle(fontSize = 12.sp, color = Color.Gray)
                        )
                        drawText(
                            textLayoutResult = dateTextLayoutResult,
                            topLeft = Offset(
                                x - dateTextLayoutResult.size.width / 2,
                                y - pointRadiusPx - dateTextLayoutResult.size.height - 5.dp.toPx()
                            )
                        )
                        // ... (rest of your label and bubble drawing logic for this point)
                        // Make sure to wrap bubble drawing logic inside this if (point.xProgress <= currentProgress)
                        val bubbleText = point.labelValue
                        val bubbleTextLayoutResult = textMeasurer.measure(
                            text = bubbleText,
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = point.pointColor,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        val bubblePadding = 8.dp.toPx()
                        val bubbleWidth = bubbleTextLayoutResult.size.width + 2 * bubblePadding
                        val bubbleHeight = bubbleTextLayoutResult.size.height + 2 * bubblePadding
                        val bubbleRectTopLeft = Offset(
                            x - bubbleWidth / 2,
                            y + pointRadiusPx + 10.dp.toPx()
                        )
                        drawRoundRect(
                            color = Color.White,
                            topLeft = bubbleRectTopLeft,
                            size = Size(bubbleWidth, bubbleHeight),
                            cornerRadius = CornerRadius(8.dp.toPx())
                        )
                        drawRoundRect(
                            color = Color.LightGray,
                            topLeft = bubbleRectTopLeft,
                            size = Size(bubbleWidth, bubbleHeight),
                            cornerRadius = CornerRadius(8.dp.toPx()),
                            style = Stroke(width = 1.dp.toPx())
                        )
                        drawText(
                            textLayoutResult = bubbleTextLayoutResult,
                            topLeft = Offset(
                                bubbleRectTopLeft.x + bubblePadding,
                                bubbleRectTopLeft.y + bubblePadding
                            )
                        )
                        if (point.additionalLabel != null) {
                            val additionalLabelLayout = textMeasurer.measure(
                                text = point.additionalLabel,
                                style = TextStyle(fontSize = 12.sp, color = Color.DarkGray)
                            )
                            drawText(
                                textLayoutResult = additionalLabelLayout,
                                topLeft = Offset(
                                    x - additionalLabelLayout.size.width / 2,
                                    bubbleRectTopLeft.y + bubbleHeight + 4.dp.toPx()
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true, name = "Weight Progress Line Chart Preview",
    backgroundColor = 0xFFFFFFFF
)
@Composable
fun WeightProgressLineChartPreview() {
    val sampleChartData = listOf(
        ChartDataPoint(
            xProgress = 0.1f,
            value = 70.5f,
            labelDate = "Today",
            pointColor = Color.Red,
            labelValue = "63.5 kg",
            additionalLabel = "Current you"
        ),
        ChartDataPoint(
            xProgress = 0.4f,
            value = 65.1f,
            labelDate = "May 01",
            pointColor = Color(0xFFFFA500), // Orange
            labelValue = "63.1 kg",
            additionalLabel = "Goal Date"
        ),
        ChartDataPoint(
            xProgress = 0.9f,
            value = 60.0f,
            labelDate = "Jun 06",
            pointColor = Color.Green,
            labelValue = "60 kg",
            additionalLabel = "Target"
        )
    )
    WeightProgressLineChart(dataPoints = sampleChartData)
}

@Preview(showBackground = true, name = "Chart with Fewer Points", backgroundColor = 0xFFFFFFFF)
@Composable
fun WeightProgressLineChartFewPointsPreview() {
    val sampleChartData = listOf(
        ChartDataPoint(
            xProgress = 0.2f,
            value = 70f,
            labelDate = "Start",
            pointColor = Color.Blue,
            labelValue = "70 kg"
        ),
        ChartDataPoint(
            xProgress = 0.8f,
            value = 65f,
            labelDate = "End",
            pointColor = Color.Magenta,
            labelValue = "65 kg",
            additionalLabel = "Target"
        )
    )
    WeightProgressLineChart(dataPoints = sampleChartData)
}

@Preview(
    showBackground = true,
    name = "Weight Gain Progress Line Chart",
    backgroundColor = 0xFFFFFFFF
)
@Composable
fun WeightGainLineChartPreview() {
    val weightGainData =
        remember { // Use remember if data could change, though static for preview is fine
            listOf(
                ChartDataPoint(
                    xProgress = 0.1f,
                    value = 50.0f,
                    labelDate = "Jan 01",
                    pointColor = Color.Red,
                    labelValue = "55.0 kg",
                    additionalLabel = "Starting Weight"
                ),
                ChartDataPoint(
                    xProgress = 0.45f,
                    value = 60.2f,
                    labelDate = "Feb 15",
                    pointColor = Color(0xFFFFA500),
                    labelValue = "57.2 kg",
                    additionalLabel = "Check-in"
                ),
                ChartDataPoint(
                    xProgress = 0.9f,
                    value = 82.0f,
                    labelDate = "Apr 01",
                    pointColor = Color.Green,
                    labelValue = "62 kg",
                    additionalLabel = "Target Weight"
                )
            )
        }
    WeightProgressLineChart(dataPoints = weightGainData)
}

@Preview(showBackground = true, name = "Chart with Fewer Points", backgroundColor = 0xFFFFFFFF)
@Composable
fun WeightGainProgressLineChartFewPointsPreview() {
    val sampleChartData = listOf(
        ChartDataPoint(
            xProgress = 0.2f,
            value = 60f,
            labelDate = "Start",
            pointColor = Color.Blue,
            labelValue = "70 kg"
        ),
        ChartDataPoint(
            xProgress = 0.8f,
            value = 75f,
            labelDate = "End",
            pointColor = Color.Magenta,
            labelValue = "65 kg",
            additionalLabel = "Target"
        )
    )
    WeightProgressLineChart(dataPoints = sampleChartData)
}