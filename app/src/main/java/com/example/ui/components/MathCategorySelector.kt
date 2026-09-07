package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MathCategory

@Composable
fun MathCategorySelector(
    selectedCategory: MathCategory?,
    onCategorySelected: (MathCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = Color(0x33000000)
            ),
        shape = RoundedCornerShape(24.dp),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    color = Color(0xFFD4E6F6),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(vertical = 10.dp, horizontal = 6.dp)
        ) {
            // Little top pill indicator
            Box(
                modifier = Modifier
                    .size(width = 36.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFFCFD8DC))
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 4 category items in Row (RTL ordering in Arabic)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Top
            ) {
                // Category 1: Powers & Roots
                CategoryItem(
                    category = MathCategory.POWERS_AND_ROOTS,
                    title = "الأسس\nوالجذور",
                    isSelected = selectedCategory == MathCategory.POWERS_AND_ROOTS,
                    onClick = { onCategorySelected(MathCategory.POWERS_AND_ROOTS) },
                    testTag = "category_powers_roots"
                ) {
                    PowersAndRootsIcon()
                }

                // Category 2: Trigonometric Ratios
                CategoryItem(
                    category = MathCategory.TRIGONOMETRY,
                    title = "النسب\nالمثلثية",
                    isSelected = selectedCategory == MathCategory.TRIGONOMETRY,
                    onClick = { onCategorySelected(MathCategory.TRIGONOMETRY) },
                    testTag = "category_trigonometry"
                ) {
                    TrigonometryIcon()
                }

                // Category 3: Triangle Similarity
                CategoryItem(
                    category = MathCategory.TRIANGLE_SIMILARITY,
                    title = "تشابه\nالمثلثات",
                    isSelected = selectedCategory == MathCategory.TRIANGLE_SIMILARITY,
                    onClick = { onCategorySelected(MathCategory.TRIANGLE_SIMILARITY) },
                    testTag = "category_similarity"
                ) {
                    TriangleSimilarityIcon()
                }

                // Category 4: Ratio & Proportion
                CategoryItem(
                    category = MathCategory.RATIO_AND_PROPORTION,
                    title = "النسبة\nوالتناسب",
                    isSelected = selectedCategory == MathCategory.RATIO_AND_PROPORTION,
                    onClick = { onCategorySelected(MathCategory.RATIO_AND_PROPORTION) },
                    testTag = "category_ratio"
                ) {
                    RatioAndProportionIcon()
                }
            }
        }
    }
}

@Composable
private fun CategoryItem(
    category: MathCategory,
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String,
    icon: @Composable () -> Unit
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFF00A896) else Color.Transparent,
        label = "border_color"
    )
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFFE0F2F1) else Color.Transparent,
        label = "bg_color"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(bgColor)
            .border(width = 2.dp, color = borderColor, shape = RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp, vertical = 6.dp)
            .testTag(testTag)
    ) {
        Box(
            modifier = Modifier.size(52.dp),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color(0xFF004D40) else Color(0xFF1E293B),
            textAlign = TextAlign.Center,
            lineHeight = 14.sp
        )
    }
}

// Custom vector canvas icons representing the exact icons in the screenshot:

@Composable
fun PowersAndRootsIcon() {
    Canvas(modifier = Modifier.size(46.dp)) {
        val w = size.width
        val h = size.height

        // Square root symbol in maroon/magenta
        val rootPath = Path().apply {
            moveTo(w * 0.15f, h * 0.42f)
            lineTo(w * 0.28f, h * 0.42f)
            lineTo(w * 0.38f, h * 0.65f)
            lineTo(w * 0.52f, h * 0.20f)
            lineTo(w * 0.85f, h * 0.20f)
        }
        drawPath(
            path = rootPath,
            color = Color(0xFFAD1457),
            style = Stroke(width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // 'x' under square root in teal
        val xColor = Color(0xFF00838F)
        drawLine(
            color = xColor,
            start = Offset(w * 0.60f, h * 0.32f),
            end = Offset(w * 0.78f, h * 0.52f),
            strokeWidth = 3f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = xColor,
            start = Offset(w * 0.78f, h * 0.32f),
            end = Offset(w * 0.60f, h * 0.52f),
            strokeWidth = 3f,
            cap = StrokeCap.Round
        )

        // Horizontal axis line with tick marks underneath
        val axisColor = Color(0xFF37474F)
        drawLine(
            color = axisColor,
            start = Offset(w * 0.12f, h * 0.82f),
            end = Offset(w * 0.88f, h * 0.82f),
            strokeWidth = 2f,
            cap = StrokeCap.Round
        )
        // Tick marks
        for (i in 0..5) {
            val tx = w * (0.22f + i * 0.11f)
            drawLine(
                color = axisColor,
                start = Offset(tx, h * 0.74f),
                end = Offset(tx, h * 0.90f),
                strokeWidth = 1.8f,
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun TrigonometryIcon() {
    Canvas(modifier = Modifier.size(46.dp)) {
        val w = size.width
        val h = size.height

        // Building / Tower rectangle in teal
        drawRoundRect(
            color = Color(0xFF4DD0E1),
            topLeft = Offset(w * 0.50f, h * 0.35f),
            size = Size(w * 0.32f, h * 0.55f),
            cornerRadius = CornerRadius(4f, 4f)
        )
        drawRoundRect(
            color = Color(0xFF00838F),
            topLeft = Offset(w * 0.50f, h * 0.35f),
            size = Size(w * 0.32f, h * 0.55f),
            cornerRadius = CornerRadius(4f, 4f),
            style = Stroke(width = 2f)
        )

        // Windows in building
        val winColor = Color.White
        drawRect(winColor, Offset(w * 0.56f, h * 0.44f), Size(w * 0.08f, w * 0.08f))
        drawRect(winColor, Offset(w * 0.70f, h * 0.44f), Size(w * 0.08f, w * 0.08f))
        drawRect(winColor, Offset(w * 0.56f, h * 0.60f), Size(w * 0.08f, w * 0.08f))
        drawRect(winColor, Offset(w * 0.70f, h * 0.60f), Size(w * 0.08f, w * 0.08f))

        // Question mark '?' in coral on the left
        val qColor = Color(0xFFE65100)
        val qPath = Path().apply {
            moveTo(w * 0.20f, h * 0.42f)
            cubicTo(w * 0.20f, h * 0.25f, w * 0.42f, h * 0.25f, w * 0.42f, h * 0.42f)
            cubicTo(w * 0.42f, h * 0.55f, w * 0.31f, h * 0.58f, w * 0.31f, h * 0.70f)
        }
        drawPath(path = qPath, color = qColor, style = Stroke(width = 3.5f, cap = StrokeCap.Round))
        drawCircle(color = qColor, radius = 2.5f, center = Offset(w * 0.31f, h * 0.82f))
    }
}

@Composable
fun TriangleSimilarityIcon() {
    Canvas(modifier = Modifier.size(46.dp)) {
        val w = size.width
        val h = size.height

        // Top triangle (teal)
        val t1Path = Path().apply {
            moveTo(w * 0.50f, h * 0.12f)
            lineTo(w * 0.68f, h * 0.42f)
            lineTo(w * 0.32f, h * 0.42f)
            close()
        }
        drawPath(t1Path, color = Color(0xFF80DEEA))
        drawPath(t1Path, color = Color(0xFF00838F), style = Stroke(width = 2.5f, join = StrokeJoin.Round))

        // Bottom right triangle (orange)
        val t2Path = Path().apply {
            moveTo(w * 0.72f, h * 0.55f)
            lineTo(w * 0.90f, h * 0.88f)
            lineTo(w * 0.54f, h * 0.88f)
            close()
        }
        drawPath(t2Path, color = Color(0xFFFFCC80))
        drawPath(t2Path, color = Color(0xFFEF6C00), style = Stroke(width = 2.5f, join = StrokeJoin.Round))

        // Dashed lines showing proportionality and similarity
        val dashEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 4f), 0f)
        drawLine(
            color = Color(0xFF78909C),
            start = Offset(w * 0.32f, h * 0.42f),
            end = Offset(w * 0.22f, h * 0.85f),
            strokeWidth = 2f,
            pathEffect = dashEffect
        )
        drawLine(
            color = Color(0xFF78909C),
            start = Offset(w * 0.22f, h * 0.85f),
            end = Offset(w * 0.54f, h * 0.88f),
            strokeWidth = 2f,
            pathEffect = dashEffect
        )
    }
}

@Composable
fun RatioAndProportionIcon() {
    Canvas(modifier = Modifier.size(46.dp)) {
        val w = size.width
        val h = size.height

        // 4 nodes in rectangle pattern
        val sizeNode = w * 0.20f

        // Top left (Cyan square)
        drawRoundRect(
            color = Color(0xFF26C6DA),
            topLeft = Offset(w * 0.12f, h * 0.14f),
            size = Size(sizeNode, sizeNode),
            cornerRadius = CornerRadius(4f, 4f)
        )

        // Top right (Orange diamond/square)
        drawRoundRect(
            color = Color(0xFFFFA726),
            topLeft = Offset(w * 0.68f, h * 0.14f),
            size = Size(sizeNode, sizeNode),
            cornerRadius = CornerRadius(4f, 4f)
        )

        // Bottom left (Purple square)
        drawRoundRect(
            color = Color(0xFFAB47BC),
            topLeft = Offset(w * 0.12f, h * 0.66f),
            size = Size(sizeNode, sizeNode),
            cornerRadius = CornerRadius(4f, 4f)
        )

        // Bottom right (Blue square)
        drawRoundRect(
            color = Color(0xFF42A5F5),
            topLeft = Offset(w * 0.68f, h * 0.66f),
            size = Size(sizeNode, sizeNode),
            cornerRadius = CornerRadius(4f, 4f)
        )

        // Directional arrows connecting nodes
        val arrowColor = Color(0xFF37474F)
        val stroke = Stroke(width = 2f, cap = StrokeCap.Round)

        // Top horizontal arrow
        drawLine(arrowColor, Offset(w * 0.35f, h * 0.24f), Offset(w * 0.65f, h * 0.24f), strokeWidth = 2f)

        // Bottom horizontal arrow
        drawLine(arrowColor, Offset(w * 0.65f, h * 0.76f), Offset(w * 0.35f, h * 0.76f), strokeWidth = 2f)

        // Diagonal proportional cross-arrow
        drawLine(arrowColor, Offset(w * 0.30f, h * 0.34f), Offset(w * 0.70f, h * 0.66f), strokeWidth = 2f)
    }
}
