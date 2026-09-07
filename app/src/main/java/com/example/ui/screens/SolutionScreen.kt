package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MathSolution
import com.example.ui.UiState
import com.example.ui.components.BottomActionsBar
import com.example.ui.components.MathDiagramView
import com.example.ui.components.SolveGradientButton
import com.example.ui.components.TopHeaderCard
import com.example.util.ExcelExporter
import com.example.util.PdfExporter
import com.example.util.WhatsAppShareHelper

@Composable
fun SolutionScreen(
    uiState: UiState,
    onProblemInputChanged: (String) -> Unit,
    onSolveClick: () -> Unit,
    onClearClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF3F7FA)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Scrollable Upper Content
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Top Header Card
            TopHeaderCard(
                showBackButton = true,
                onBackClick = onBackClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 2. Upper Question Card ("أدخل المسألة الرياضية")
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(22.dp),
                        spotColor = Color(0x22000000)
                    ),
                shape = RoundedCornerShape(22.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 2.5.dp,
                            brush = Brush.linearGradient(
                                listOf(Color(0xFF00A896), Color(0xFFF4511E))
                            ),
                            shape = RoundedCornerShape(22.dp)
                        )
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "أدخل المسألة الرياضية",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0C69B3),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = uiState.problemInputText,
                        onValueChange = onProblemInputChanged,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(84.dp)
                            .testTag("solution_problem_input"),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF00A896),
                            unfocusedBorderColor = Color(0xFFE2E8F0),
                            focusedContainerColor = Color(0xFFF8FAFC),
                            unfocusedContainerColor = Color(0xFFF8FAFC)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 3. Center "الحل" button
            SolveGradientButton(
                onClick = onSolveClick,
                modifier = Modifier.padding(vertical = 4.dp),
                testTag = "solution_solve_button"
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 4. Main Solution Box ("الحل الرياضي الكامل")
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp)
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(22.dp),
                        spotColor = Color(0x33000000)
                    ),
                shape = RoundedCornerShape(22.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 2.dp,
                            color = Color(0xFFCCE4F7),
                            shape = RoundedCornerShape(22.dp)
                        )
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "الحل الرياضي الكامل",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF0C69B3),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    when {
                        uiState.isLoading -> {
                            LoadingStateView()
                        }
                        uiState.solution != null -> {
                            SolutionDetailView(solution = uiState.solution)
                        }
                        else -> {
                            EmptyStateView()
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // 5. Bottom Actions Bar (PDF, WhatsApp, Excel, Delete)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            BottomActionsBar(
                onSavePdf = {
                    val sol = uiState.solution
                    if (sol != null) {
                        PdfExporter.generateAndSharePdf(context, sol)
                            .onFailure {
                                Toast.makeText(context, "فشل إنشاء ملف PDF: ${it.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(context, "يرجى الضغط على زر الحل أولاً لعرض وتصدير النتائج", Toast.LENGTH_SHORT).show()
                    }
                },
                onShareWhatsApp = {
                    val sol = uiState.solution
                    if (sol != null) {
                        WhatsAppShareHelper.shareToWhatsApp(context, sol)
                    } else {
                        Toast.makeText(context, "يرجى حل المسألة أولاً قبل المشاركة", Toast.LENGTH_SHORT).show()
                    }
                },
                onSaveExcel = {
                    val sol = uiState.solution
                    if (sol != null) {
                        ExcelExporter.generateAndShareExcel(context, sol)
                            .onFailure {
                                Toast.makeText(context, "فشل إنشاء ملف Excel: ${it.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(context, "يرجى حل المسألة أولاً لتصدير جدول Excel", Toast.LENGTH_SHORT).show()
                    }
                },
                onClear = onClearClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun LoadingStateView() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_loading")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = Color(0xFF00A896),
            strokeWidth = 4.dp,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "جاري تحليل وحساب خطوات المسألة الرياضية...",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF0288D1),
            modifier = Modifier.alpha(alpha),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun EmptyStateView() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "مربع الحل الرياضي جاهز",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF64748B),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "اضغط على زر (الحل) المركزي في الأعلى لتوليد الحل التفصيلي خطوة بخطوة ورسم المخطط البياني وتصدير الملفات.",
            fontSize = 12.sp,
            color = Color(0xFF94A3B8),
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )
    }
}

@Composable
private fun SolutionDetailView(solution: MathSolution) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Given Data Section
        if (solution.givenData.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF1F8E9))
                    .border(1.dp, Color(0xFFC8E6C9), RoundedCornerShape(12.dp))
                    .padding(10.dp)
            ) {
                Column {
                    Text(
                        text = "📌 المعطيات:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    solution.givenData.forEach {
                        Text(
                            text = "• $it",
                            fontSize = 12.sp,
                            color = Color(0xFF1B5E20)
                        )
                    }
                }
            }
        }

        // Formulas Section
        if (solution.formulas.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE1F5FE))
                    .border(1.dp, Color(0xFFB3E5FC), RoundedCornerShape(12.dp))
                    .padding(10.dp)
            ) {
                Column {
                    Text(
                        text = "📜 القوانين الرياضية المستخدمة:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0277BD)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    solution.formulas.forEach {
                        Text(
                            text = "• $it",
                            fontSize = 12.sp,
                            color = Color(0xFF01579B)
                        )
                    }
                }
            }
        }

        // Steps Section
        Text(
            text = "🔢 خطوات الحل بالتفصيل:",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0B2545),
            modifier = Modifier.padding(top = 4.dp)
        )

        solution.steps.forEach { step ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFF8FAFC),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF00A896)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${step.stepNumber}",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = step.title,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0C69B3)
                        )
                        if (step.calculation.isNotBlank()) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = step.calculation,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFE65100)
                            )
                        }
                        if (step.explanation.isNotBlank()) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = step.explanation,
                                fontSize = 11.sp,
                                color = Color(0xFF475569)
                            )
                        }
                    }
                }
            }
        }

        // Geometric / Plot Diagram Canvas View
        MathDiagramView(
            diagramData = solution.diagramData,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        // Final Result Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFF1B5E20), Color(0xFF2E7D32))
                    )
                )
                .padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "النتيجة",
                    tint = Color(0xFFB9F6CA),
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "🏁 النتيجة النهائية:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE8F5E9)
                    )
                    Text(
                        text = solution.finalResult,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
