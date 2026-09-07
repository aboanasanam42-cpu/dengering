package com.example.ui.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DiagramData
import com.example.data.model.DiagramType

@Composable
fun MathDiagramView(
    diagramData: DiagramData,
    modifier: Modifier = Modifier
) {
    if (diagramData.type == DiagramType.NONE) return

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF8FAFC))
            .border(1.5.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "📊 المخطط البياني والهندسي التوضيحي",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0C69B3)
            )

            Spacer(modifier = Modifier.height(8.dp))

            when (diagramData.type) {
                DiagramType.RIGHT_TRIANGLE -> RightTriangleDiagram(diagramData)
                DiagramType.SIMILAR_TRIANGLES -> SimilarTrianglesDiagram(diagramData)
                DiagramType.RATIO_PROPORTION_BARS -> RatioBarsDiagram(diagramData)
                DiagramType.POWER_ROOT_SCALE -> PowerScaleDiagram(diagramData)
                DiagramType.NONE -> {}
            }
        }
    }
}

@Composable
private fun RightTriangleDiagram(data: DiagramData) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
    ) {
        val w = size.width
        val h = size.height

        val pC = Offset(w * 0.25f, h * 0.82f) // Right angle corner
        val pA = Offset(w * 0.25f, h * 0.18f) // Top corner
        val pB = Offset(w * 0.78f, h * 0.82f) // Bottom-right corner

        // Draw Triangle Fill
        val triPath = Path().apply {
            moveTo(pA.x, pA.y)
            lineTo(pB.x, pB.y)
            lineTo(pC.x, pC.y)
            close()
        }
        drawPath(triPath, color = Color(0x2200BCD4))
        drawPath(
            triPath,
            color = Color(0xFF00838F),
            style = Stroke(width = 3.5f, join = StrokeJoin.Round)
        )

        // Right angle marker square at C
        val squareSize = 18f
        val rightAnglePath = Path().apply {
            moveTo(pC.x, pC.y - squareSize)
            lineTo(pC.x + squareSize, pC.y - squareSize)
            lineTo(pC.x + squareSize, pC.y)
        }
        drawPath(rightAnglePath, color = Color(0xFF00838F), style = Stroke(width = 2f))

        // Native text rendering for labels
        drawContext.canvas.nativeCanvas.apply {
            val textPaint = Paint().apply {
                color = Color(0xFF1E293B).toArgb()
                textSize = 28f
                isAntiAlias = true
            }
            val accentPaint = Paint().apply {
                color = Color(0xFFE65100).toArgb()
                textSize = 28f
                isFakeBoldText = true
                isAntiAlias = true
            }

            // Opposite (Vertical side)
            drawText(data.labelA.ifEmpty { "المقابل" }, pC.x - 120f, (pA.y + pC.y) / 2, accentPaint)

            // Adjacent (Horizontal bottom)
            drawText(data.labelB.ifEmpty { "المجاور" }, (pC.x + pB.x) / 2 - 40f, pC.y + 35f, textPaint)

            // Hypotenuse (Diagonal)
            drawText(data.labelC.ifEmpty { "الوتر" }, (pA.x + pB.x) / 2 + 20f, (pA.y + pB.y) / 2 - 10f, accentPaint)

            // Angle Theta marker
            drawText("هـ (θ)", pB.x - 50f, pB.y - 12f, accentPaint)
        }
    }
}

@Composable
private fun SimilarTrianglesDiagram(data: DiagramData) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
    ) {
        val w = size.width
        val h = size.height

        // Triangle 1 (Smaller, left)
        val t1Top = Offset(w * 0.25f, h * 0.28f)
        val t1Left = Offset(w * 0.12f, h * 0.80f)
        val t1Right = Offset(w * 0.38f, h * 0.80f)

        val t1Path = Path().apply {
            moveTo(t1Top.x, t1Top.y)
            lineTo(t1Right.x, t1Right.y)
            lineTo(t1Left.x, t1Left.y)
            close()
        }
        drawPath(t1Path, color = Color(0x224DD0E1))
        drawPath(t1Path, color = Color(0xFF00838F), style = Stroke(width = 3f, join = StrokeJoin.Round))

        // Triangle 2 (Larger, right)
        val t2Top = Offset(w * 0.72f, h * 0.12f)
        val t2Left = Offset(w * 0.52f, h * 0.80f)
        val t2Right = Offset(w * 0.92f, h * 0.80f)

        val t2Path = Path().apply {
            moveTo(t2Top.x, t2Top.y)
            lineTo(t2Right.x, t2Right.y)
            lineTo(t2Left.x, t2Left.y)
            close()
        }
        drawPath(t2Path, color = Color(0x22FFB74D))
        drawPath(t2Path, color = Color(0xFFEF6C00), style = Stroke(width = 3.5f, join = StrokeJoin.Round))

        // Dashed connector showing ratio
        val dashEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 4f), 0f)
        drawLine(
            color = Color(0xFF78909C),
            start = Offset(w * 0.38f, h * 0.50f),
            end = Offset(w * 0.52f, h * 0.50f),
            strokeWidth = 2f,
            pathEffect = dashEffect
        )

        drawContext.canvas.nativeCanvas.apply {
            val textPaint = Paint().apply {
                color = Color(0xFF1E293B).toArgb()
                textSize = 26f
                isAntiAlias = true
            }
            val ratioPaint = Paint().apply {
                color = Color(0xFF0C69B3).toArgb()
                textSize = 26f
                isFakeBoldText = true
                isAntiAlias = true
            }

            drawText("Δ أ ب ج", w * 0.20f, h * 0.95f, textPaint)
            drawText("Δ د هـ و", w * 0.67f, h * 0.95f, textPaint)
            drawText("معامل التشابه (ك)", w * 0.38f, h * 0.44f, ratioPaint)
        }
    }
}

@Composable
private fun RatioBarsDiagram(data: DiagramData) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
    ) {
        val w = size.width
        val h = size.height

        val barHeight = 28f
        val maxBarWidth = w * 0.55f

        val valA = if (data.valueA > 0) data.valueA else 4f
        val valB = if (data.valueB > 0) data.valueB else 6f
        val maxVal = maxOf(valA, valB)

        val widthA = (valA / maxVal) * maxBarWidth
        val widthB = (valB / maxVal) * maxBarWidth

        // Bar A (Teal)
        drawRoundRect(
            color = Color(0xFF00A896),
            topLeft = Offset(w * 0.08f, h * 0.20f),
            size = Size(widthA, barHeight),
            cornerRadius = CornerRadius(8f, 8f)
        )

        // Bar B (Orange)
        drawRoundRect(
            color = Color(0xFFF4511E),
            topLeft = Offset(w * 0.08f, h * 0.58f),
            size = Size(widthB, barHeight),
            cornerRadius = CornerRadius(8f, 8f)
        )

        drawContext.canvas.nativeCanvas.apply {
            val paint = Paint().apply {
                color = Color(0xFF1E293B).toArgb()
                textSize = 26f
                isAntiAlias = true
            }
            drawText("${data.labelA.ifEmpty { "الكمية 1" }}: $valA", w * 0.08f + widthA + 16f, h * 0.20f + 22f, paint)
            drawText("${data.labelB.ifEmpty { "الكمية 2" }}: $valB", w * 0.08f + widthB + 16f, h * 0.58f + 22f, paint)
        }
    }
}

@Composable
private fun PowerScaleDiagram(data: DiagramData) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        val w = size.width
        val h = size.height

        // Horizontal baseline
        drawLine(
            color = Color(0xFF0C69B3),
            start = Offset(w * 0.10f, h * 0.70f),
            end = Offset(w * 0.90f, h * 0.70f),
            strokeWidth = 3f,
            cap = StrokeCap.Round
        )

        // Circles representing exponential scale
        drawCircle(color = Color(0xFF00A896), radius = 18f, center = Offset(w * 0.25f, h * 0.70f))
        drawCircle(color = Color(0xFFFFA000), radius = 24f, center = Offset(w * 0.55f, h * 0.70f))
        drawCircle(color = Color(0xFFE65100), radius = 32f, center = Offset(w * 0.82f, h * 0.70f))

        drawContext.canvas.nativeCanvas.apply {
            val textPaint = Paint().apply {
                color = Color.White.toArgb()
                textSize = 22f
                isFakeBoldText = true
                textAlign = Paint.Align.CENTER
                isAntiAlias = true
            }
            val labelPaint = Paint().apply {
                color = Color(0xFF1E293B).toArgb()
                textSize = 24f
                textAlign = Paint.Align.CENTER
                isAntiAlias = true
            }

            drawText("أ", w * 0.25f, h * 0.70f + 8f, textPaint)
            drawText("أ²", w * 0.55f, h * 0.70f + 8f, textPaint)
            drawText("أ^م", w * 0.82f, h * 0.70f + 8f, textPaint)

            drawText(data.labelA.ifEmpty { "القاعدة" }, w * 0.25f, h * 0.35f, labelPaint)
            drawText(data.labelB.ifEmpty { "الناتج المضاعف" }, w * 0.82f, h * 0.35f, labelPaint)
        }
    }
}
