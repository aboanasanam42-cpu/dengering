package com.example.ui.components

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BottomActionsBar(
    onSavePdf: () -> Unit,
    onShareWhatsApp: () -> Unit,
    onSaveExcel: () -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = Color(0x33000000)
            ),
        shape = RoundedCornerShape(24.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    color = Color(0xFFD4E6F6),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(horizontal = 8.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. PDF
            ActionButtonItem(
                label = "حفظ PDF",
                testTag = "action_save_pdf",
                onClick = onSavePdf
            ) {
                PdfBadgeIcon()
            }

            // 2. WhatsApp
            ActionButtonItem(
                label = "إرسال عبر\nواتساب",
                testTag = "action_share_whatsapp",
                onClick = onShareWhatsApp
            ) {
                WhatsAppBadgeIcon()
            }

            // 3. Excel
            ActionButtonItem(
                label = "حفظ Excel",
                testTag = "action_save_excel",
                onClick = onSaveExcel
            ) {
                ExcelBadgeIcon()
            }

            // 4. Delete / Clear
            ActionButtonItem(
                label = "مسح\nالمسألة",
                testTag = "action_clear_problem",
                onClick = onClear
            ) {
                TrashBadgeIcon()
            }
        }
    }
}

@Composable
private fun ActionButtonItem(
    label: String,
    testTag: String,
    onClick: () -> Unit,
    icon: @Composable () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp, vertical = 4.dp)
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
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B),
            textAlign = TextAlign.Center,
            lineHeight = 14.sp
        )
    }
}

// 1. PDF Icon matching the screenshot
@Composable
fun PdfBadgeIcon() {
    Box(
        modifier = Modifier
            .size(46.dp)
            .shadow(4.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF26A69A), Color(0xFFF4511E))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(28.dp)) {
            val w = size.width
            val h = size.height

            // White page sheet with folded corner
            val page = Path().apply {
                moveTo(w * 0.18f, h * 0.10f)
                lineTo(w * 0.62f, h * 0.10f)
                lineTo(w * 0.82f, h * 0.30f)
                lineTo(w * 0.82f, h * 0.90f)
                lineTo(w * 0.18f, h * 0.90f)
                close()
            }
            drawPath(page, color = Color.White)

            // Red banner on page
            drawRoundRect(
                color = Color(0xFFE53935),
                topLeft = Offset(w * 0.12f, h * 0.40f),
                size = Size(w * 0.76f, h * 0.30f),
                cornerRadius = CornerRadius(4f, 4f)
            )

            // PDF text on banner
            drawContext.canvas.nativeCanvas.apply {
                val paint = android.graphics.Paint().apply {
                    color = android.graphics.Color.WHITE
                    textSize = 18f
                    isFakeBoldText = true
                    textAlign = android.graphics.Paint.Align.CENTER
                    isAntiAlias = true
                }
                drawText("PDF", w * 0.50f, h * 0.62f, paint)
            }
        }
    }
}

// 2. WhatsApp / Paper plane Icon matching the screenshot
@Composable
fun WhatsAppBadgeIcon() {
    Box(
        modifier = Modifier
            .size(46.dp)
            .shadow(4.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF29B6F6), Color(0xFF0288D1))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(26.dp)) {
            val w = size.width
            val h = size.height

            // Paper plane / Send shape in crisp white
            val plane = Path().apply {
                moveTo(w * 0.15f, h * 0.50f)
                lineTo(w * 0.85f, h * 0.15f)
                lineTo(w * 0.60f, h * 0.85f)
                lineTo(w * 0.45f, h * 0.60f)
                close()
            }
            drawPath(plane, color = Color.White)
            drawLine(
                color = Color(0xFF0288D1),
                start = Offset(w * 0.85f, h * 0.15f),
                end = Offset(w * 0.45f, h * 0.60f),
                strokeWidth = 2f
            )
        }
    }
}

// 3. Excel Badge Icon matching the screenshot
@Composable
fun ExcelBadgeIcon() {
    Box(
        modifier = Modifier
            .size(46.dp)
            .shadow(4.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF26A69A), Color(0xFFF57C00))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(28.dp)) {
            val w = size.width
            val h = size.height

            // Green spreadsheet book
            drawRoundRect(
                color = Color(0xFF1E7E34),
                topLeft = Offset(w * 0.12f, h * 0.15f),
                size = Size(w * 0.76f, h * 0.70f),
                cornerRadius = CornerRadius(6f, 6f)
            )

            // Grid lines on right side
            val linePaint = Color(0x88FFFFFF)
            drawLine(linePaint, Offset(w * 0.48f, h * 0.25f), Offset(w * 0.80f, h * 0.25f), 1.5f)
            drawLine(linePaint, Offset(w * 0.48f, h * 0.45f), Offset(w * 0.80f, h * 0.45f), 1.5f)
            drawLine(linePaint, Offset(w * 0.48f, h * 0.65f), Offset(w * 0.80f, h * 0.65f), 1.5f)
            drawLine(linePaint, Offset(w * 0.64f, h * 0.25f), Offset(w * 0.64f, h * 0.75f), 1.5f)

            // Big 'X' on left side
            drawContext.canvas.nativeCanvas.apply {
                val paint = android.graphics.Paint().apply {
                    color = android.graphics.Color.WHITE
                    textSize = 24f
                    isFakeBoldText = true
                    textAlign = android.graphics.Paint.Align.CENTER
                    isAntiAlias = true
                }
                drawText("X", w * 0.32f, h * 0.62f, paint)
            }
        }
    }
}

// 4. Trash / Clear Icon matching the screenshot
@Composable
fun TrashBadgeIcon() {
    Box(
        modifier = Modifier
            .size(46.dp)
            .shadow(4.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFEF5350), Color(0xFFC62828))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "مسح",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}
