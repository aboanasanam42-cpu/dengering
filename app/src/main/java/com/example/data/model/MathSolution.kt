package com.example.data.model

enum class DiagramType {
    NONE,
    RIGHT_TRIANGLE,
    SIMILAR_TRIANGLES,
    RATIO_PROPORTION_BARS,
    POWER_ROOT_SCALE
}

data class SolutionStep(
    val stepNumber: Int,
    val title: String,
    val calculation: String,
    val explanation: String
)

data class DiagramData(
    val type: DiagramType = DiagramType.NONE,
    val labelA: String = "",
    val labelB: String = "",
    val labelC: String = "",
    val valueA: Float = 0f,
    val valueB: Float = 0f,
    val valueC: Float = 0f,
    val ratio: Float = 1f,
    val note: String = ""
)

data class MathSolution(
    val problemText: String,
    val category: MathCategory,
    val title: String,
    val givenData: List<String>,
    val formulas: List<String>,
    val steps: List<SolutionStep>,
    val finalResult: String,
    val diagramData: DiagramData = DiagramData(),
    val rawText: String = ""
) {
    fun toFormattedString(): String {
        val builder = StringBuilder()
        builder.appendLine("📐 حل المسألة الرياضية (${category.titleArabic})")
        builder.appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
        builder.appendLine("❓ المسألة:")
        builder.appendLine(problemText)
        builder.appendLine()

        if (givenData.isNotEmpty()) {
            builder.appendLine("📌 المعطيات:")
            givenData.forEach { builder.appendLine("  • $it") }
            builder.appendLine()
        }

        if (formulas.isNotEmpty()) {
            builder.appendLine("📜 القوانين والنظريات:")
            formulas.forEach { builder.appendLine("  • $it") }
            builder.appendLine()
        }

        if (steps.isNotEmpty()) {
            builder.appendLine("🔢 خطوات الحل التفصيلية:")
            steps.forEach { step ->
                builder.appendLine("الخطوة ${step.stepNumber}: ${step.title}")
                if (step.calculation.isNotBlank()) {
                    builder.appendLine("  ${step.calculation}")
                }
                if (step.explanation.isNotBlank()) {
                    builder.appendLine("  الشرح: ${step.explanation}")
                }
                builder.appendLine()
            }
        }

        builder.appendLine("🏁 النتيجة النهائية:")
        builder.appendLine(finalResult)
        builder.appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
        builder.appendLine("تصميم وبرمجة / الدكتور مالك الرميمة - 771124103")
        return builder.toString()
    }
}
