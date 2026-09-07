package com.example.util

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.example.data.model.MathSolution
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets

object ExcelExporter {

    fun generateAndShareExcel(context: Context, solution: MathSolution): Result<File> {
        return runCatching {
            val outputDir = File(context.cacheDir, "documents")
            if (!outputDir.exists()) outputDir.mkdirs()

            val excelFile = File(outputDir, "Math_Solution_${System.currentTimeMillis()}.csv")
            val fos = FileOutputStream(excelFile)
            val writer = OutputStreamWriter(fos, StandardCharsets.UTF_8)

            // Write UTF-8 BOM so Excel on Windows / Android / Mac natively displays Arabic correctly
            writer.write("\uFEFF")

            // Title & Info
            writer.write("تقرير حلول الرياضيات المتطورة\n")
            writer.write("تصميم وبرمجة / الدكتور مالك الرميمة - هاتف 771124103\n")
            writer.write("فكرة أ/ محمد الرميمة | مصحح فكرة أ/ مطهر الرميمة\n")
            writer.write("المجال الرياضي,${escapeCsv(solution.category.titleArabic)}\n")
            writer.write("المسألة,${escapeCsv(solution.problemText)}\n\n")

            // Table Header
            writer.write("رقم الخطوة,البيان الرياضي,المعادلة / الحسابات,الشرح التفصيلي\n")

            // Given data row
            if (solution.givenData.isNotEmpty()) {
                val givenCombined = solution.givenData.joinToString(" | ")
                writer.write("0,المعطيات,${escapeCsv(givenCombined)},معطيات المسألة المستخرجة\n")
            }

            // Formulas row
            if (solution.formulas.isNotEmpty()) {
                val formulasCombined = solution.formulas.joinToString(" | ")
                writer.write("0,القوانين والنظريات,${escapeCsv(formulasCombined)},القواعد المطبقة في الحل\n")
            }

            // Steps rows
            for (step in solution.steps) {
                writer.write(
                    "${step.stepNumber}," +
                    "${escapeCsv(step.title)}," +
                    "${escapeCsv(step.calculation)}," +
                    "${escapeCsv(step.explanation)}\n"
                )
            }

            // Final Result row
            writer.write("النتيجة,الناتج النهائي,${escapeCsv(solution.finalResult)},القيمة النهائية المستنتجة للمسألة\n")

            writer.flush()
            writer.close()
            fos.close()

            // Open share intent
            val contentUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                excelFile
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/csv"
                putExtra(Intent.EXTRA_STREAM, contentUri)
                putExtra(Intent.EXTRA_SUBJECT, "جدول حل مسألة: ${solution.category.titleArabic}")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            val chooser = Intent.createChooser(shareIntent, "حفظ أو فتح ملف Excel")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)

            excelFile
        }
    }

    private fun escapeCsv(value: String): String {
        val sanitized = value.replace("\n", " ").replace("\r", " ")
        return if (sanitized.contains(",") || sanitized.contains("\"")) {
            "\"${sanitized.replace("\"", "\"\"")}\""
        } else {
            "\"$sanitized\""
        }
    }
}
