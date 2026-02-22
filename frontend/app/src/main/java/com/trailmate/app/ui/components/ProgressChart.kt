package com.trailmate.app.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun ProgressChart(
    weekData: List<Float>
) {
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }

    val labels = listOf("Mon","Tue","Wed","Thu","Fri","Sat","Sun")
    val maxValue = weekData.maxOrNull() ?: 1f
    val highestIndex = weekData.indexOf(maxValue)

    val anim by animateFloatAsState(
        targetValue = if (weekData.isNotEmpty()) 1f else 0f,
        animationSpec = tween(900, easing = FastOutSlowInEasing),
        label = ""
    )

    val total = weekData.sum()

    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(Modifier.padding(18.dp)) {

            Text("Weekly Progress", style = MaterialTheme.typography.titleLarge)
            Text("${"%.1f".format(total)} km total", color = Color.Gray)

            Spacer(Modifier.height(24.dp))

            if (weekData.isEmpty()) {
                Box(Modifier.fillMaxWidth().height(220.dp), Alignment.Center) {
                    Text("No activity yet", color = Color.Gray)
                }
                return@Surface
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .onSizeChanged { canvasSize = it }
                    .pointerInput(weekData) {
                        detectTapGestures { tap ->
                            val step = canvasSize.width / (weekData.size - 1)
                            val index = (tap.x / step).roundToInt()
                            selectedIndex = index.coerceIn(0, weekData.lastIndex)
                        }
                    }
            ) {

                Canvas(Modifier.fillMaxSize()) {

                    val stepX = size.width / (weekData.size - 1)
                    val h = size.height

                    fun point(i: Int): Offset {
                        val x = stepX * i
                        val y = h - (weekData[i] / maxValue * h)
                        return Offset(x, y)
                    }

                    /// GRID
                    repeat(3) {
                        val y = h * (it + 1) / 4
                        drawLine(Color(0xFFEDEDED), Offset(0f, y), Offset(size.width, y), 2f)
                    }

                    /// PATH
                    val path = Path()
                    path.moveTo(point(0).x, point(0).y)

                    for (i in 0 until weekData.size - 1) {
                        val p1 = point(i)
                        val p2 = point(i + 1)
                        val midX = (p1.x + p2.x) / 2

                        path.cubicTo(
                            midX, p1.y,
                            midX, p2.y,
                            p2.x, p2.y
                        )
                    }

                    /// GLOW
                    drawPath(
                        path,
                        brush = Brush.linearGradient(
                            listOf(Color(0xFFFF8A3D), Color(0xFFFF3D00))
                        ),
                        style = Stroke(width = 16f, cap = StrokeCap.Round),
                        alpha = 0.25f
                    )

                    /// MAIN LINE
                    drawPath(
                        path,
                        brush = Brush.linearGradient(
                            listOf(Color(0xFFFF8A3D), Color(0xFFFF3D00))
                        ),
                        style = Stroke(width = 6f, cap = StrokeCap.Round)
                    )

                    /// DOTS
                    weekData.forEachIndexed { i, v ->

                        if (i > anim * weekData.size) return@forEachIndexed

                        val p = point(i)

                        val color =
                            when (i) {
                                selectedIndex -> Color.Red
                                highestIndex -> Color(0xFF00C853)
                                else -> Color(0xFFFF6A00)
                            }

                        val r =
                            when (i) {
                                selectedIndex -> 14f
                                highestIndex -> 12f
                                else -> 7f
                            }

                        drawCircle(color, r, p)
                    }
                }

                /// TOOLTIP
                androidx.compose.animation.AnimatedVisibility(
                    visible = selectedIndex != null,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut()
                ) {

                    selectedIndex?.let { i ->
                        Box(
                            Modifier
                                .align(Alignment.TopCenter)
                                .background(Color.Black, RoundedCornerShape(10.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                "${labels[i]}  ${weekData[i]} km",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                labels.forEach {
                    Text(it, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}
