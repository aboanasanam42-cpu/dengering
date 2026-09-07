package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MathCategory
import com.example.ui.UiState
import com.example.ui.components.MathCategorySelector
import com.example.ui.components.SolveGradientButton
import com.example.ui.components.TopHeaderCard

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MainScreen(
    uiState: UiState,
    onCategorySelected: (MathCategory) -> Unit,
    onProblemInputChanged: (String) -> Unit,
    onSolveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF3F7FA))
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Top Card with Branding and Contributors
            TopHeaderCard(
                showBackButton = false
            )

            Spacer(modifier = Modifier.height(18.dp))

            // 2. Central Problem Box matching the design in الواجهة ١
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                // Outer Card Box
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 22.dp, bottom = 36.dp)
                        .shadow(
                            elevation = 6.dp,
                            shape = RoundedCornerShape(26.dp),
                            spotColor = Color(0x33000000)
                        ),
                    shape = RoundedCornerShape(26.dp),
                    color = Color.White
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 3.dp,
                                brush = Brush.linearGradient(
                                    listOf(
                                        Color(0xFF00A896), // Teal on left/top
                                        Color(0xFF26A69A),
                                        Color(0xFFFF8A65), // Orange on right/bottom
                                        Color(0xFFF4511E)
                                    )
                                ),
                                shape = RoundedCornerShape(26.dp)
                            )
                            .padding(top = 34.dp, start = 16.dp, end = 16.dp, bottom = 44.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "أدخل المسألة الرياضية",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0C69B3),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Text Field for entering the problem
                        OutlinedTextField(
                            value = uiState.problemInputText,
                            onValueChange = onProblemInputChanged,
                            placeholder = {
                                Text(
                                    text = if (uiState.selectedCategory != null)
                                        "اكتب المسألة الخاصة بـ ${uiState.selectedCategory.titleArabic} هنا..."
                                    else
                                        "أدخل المسألة الرياضية هنا...",
                                    fontSize = 13.sp,
                                    color = Color(0xFF94A3B8)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .testTag("main_problem_input"),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF00A896),
                                unfocusedBorderColor = Color(0xFFE2E8F0),
                                focusedContainerColor = Color(0xFFF8FAFC),
                                unfocusedContainerColor = Color(0xFFF8FAFC)
                            ),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = { onSolveClick() })
                        )

                        // Sample problems quick selector chips
                        AnimatedVisibility(
                            visible = uiState.selectedCategory != null,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            uiState.selectedCategory?.let { category ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 10.dp)
                                ) {
                                    Text(
                                        text = "مسائل نموذجية سريعة (اضغط للتجربة):",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF64748B)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    FlowRow(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        category.sampleProblems.take(3).forEach { sample ->
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(Color(0xFFE2F1F8))
                                                    .clickable { onProblemInputChanged(sample) }
                                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                            ) {
                                                Text(
                                                    text = sample,
                                                    fontSize = 11.sp,
                                                    color = Color(0xFF0277BD),
                                                    maxLines = 1
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Top Badge with Gear & Math Symbol straddling the top border
                Box(
                    modifier = Modifier
                        .offset(y = 0.dp)
                        .size(46.dp)
                        .shadow(6.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(2.dp, Color(0xFF00A896), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "خيارات المسألة",
                        tint = Color(0xFF00838F),
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Center "الحل" button straddling the bottom border
                SolveGradientButton(
                    onClick = onSolveClick,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = 0.dp),
                    testTag = "main_solve_button"
                )
            }
        }

        // 3. Category Selector at bottom
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            MathCategorySelector(
                selectedCategory = uiState.selectedCategory,
                onCategorySelected = onCategorySelected,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
