package com.example.util

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import androidx.core.content.FileProvider
import com.example.data.model.MathSolution
import java.io.File
import java.io.FileOutputStream

object PdfExporter {

    fun generateAndSharePdf(context: Context, solution: MathSolution): Result<File> {
        return runCatching {
            val pdfDoc = PdfDocument()
            val pageWidth = 595 // A4 standard width in points
            val pageHeight = 842 // A4 standard height in points
            val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
            val page = pdfDoc.startPage(pageInfo)
            val canvas = page.canvas

            // Paints
            val headerBgPaint = Paint().apply {
                color = Color.parseColor("#0C69B3")
                style = Paint.Style.FILL
            }

            val cardBgPaint = Paint().apply {
                color = Color.parseColor("#F5F9FD")
                style = Paint.Style.FILL
            }

            val borderPaint = Paint().apply {
                color = Color.parseColor("#B0D4F1")
                style = Paint.Style.STROKE
                strokeWidth = 1.5f
            }

            val accentPaint = Paint().apply {
                color = Color.parseColor("#E65100")
                style = Paint.Style.FILL
            }

            val titlePaint = Paint().apply {
                color = Color.WHITE
                textSize = 16f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                textAlign = Paint.Align.CENTER
            }

            val subTitlePaint = Paint().apply {
                color = Color.parseColor("#E1F5FE")
                textSize = 11f
                textAlign = Paint.Align.CENTER
            }

            val sectionTitlePaint = Paint().apply {
                color = Color.parseColor("#0C69B3")
                textSize = 13f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                textAlign = Paint.Align.RIGHT
            }

            val bodyPaint = Paint().apply {
                color = Color.parseColor("#212121")
                textSize = 11f
                textAlign = Paint.Align.RIGHT
            }

            val resultPaint = Paint().apply {
                color = Color.parseColor("#1B5E20")
                textSize = 12f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                textAlign = Paint.Align.RIGHT
            }

            // Draw Header Banner
            canvas.drawRect(0f, 0f, pageWidth.toFloat(), 110f, headerBgPaint)

            canvas.drawText("حلول الرياضيات المتطورة", pageWidth / 2f, 32f, titlePaint)
            canvas.drawText("تصميم وبرمجة / الدكتور مالك الرميمة - هاتف 771124103", pageWidth / 2f, 55f, subTitlePaint)
            canvas.drawText("فكرة أ/ محمد الرميمة | مصحح فكرة أ/ مطهر الرميمة", pageWidth / 2f, 75f, subTitlePaint)
            canvas.drawText("مجال المسألة: ${solution.category.titleArabic}", pageWidth / 2f, 95f, subTitlePaint)

            var currentY = 135f
            val marginX = 40f
            val rightMargin = pageWidth - marginX

            // Problem Box
            canvas.drawRoundRect(marginX, currentY, rightMargin, currentY + 70f, 8f, 8f, cardBgPaint)
            canvas.drawRoundRect(marginX, currentY, rightMargin, currentY + 70f, 8f, 8f, borderPaint)

            canvas.drawText("❓ المسألة الرياضية:", rightMargin - 15f, currentY + 22f, sectionTitlePaint)

            // Problem lines
            val problemLines = splitTextIntoLines(solution.problemText, bodyPaint, (pageWidth - 110).toFloat())
            var pY = currentY + 42f
            for (line in problemLines.take(2)) {
                canvas.drawText(line, rightMargin - 15f, pY, bodyPaint)
                pY += 16f
            }

            currentY += 90f

            // Given Data Section
            if (solution.givenData.isNotEmpty()) {
                canvas.drawText("📌 المعطيات والقوانين المستخدمة:", rightMargin, currentY, sectionTitlePaint)
                currentY += 18f
                for (given in solution.givenData.take(3)) {
                    canvas.drawText("• $given", rightMargin - 10f, currentY, bodyPaint)
                    currentY += 16f
                }
                for (formula in solution.formulas.take(2)) {
                    canvas.drawText("• $formula", rightMargin - 10f, currentY, bodyPaint)
                    currentY += 16f
                }
                currentY += 12f
            }

            // Step by Step Derivation
            canvas.drawText("🔢 خطوات الحل الرياضي المفصل:", rightMargin, currentY, sectionTitlePaint)
            currentY += 22f

            for (step in solution.steps.take(6)) {
                if (currentY > pageHeight - 120f) break

                // Step header
                canvas.drawCircle(rightMargin - 5f, currentY - 4f, 4f, accentPaint)
                canvas.drawText("الخطوة ${step.stepNumber}: ${step.title}", rightMargin - 15f, currentY, sectionTitlePaint)
                currentY += 18f

                if (step.calculation.isNotBlank()) {
                    canvas.drawText("المعادلة: ${step.calculation}", rightMargin - 20f, currentY, bodyPaint)
                    currentY += 16f
                }
                if (step.explanation.isNotBlank()) {
                    canvas.drawText("الشرح: ${step.explanation}", rightMargin - 20f, currentY, bodyPaint)
                    currentY += 16f
                }
                currentY += 8f
            }

            // Final Result Card at bottom
            val resultCardY = pageHeight - 110f
            val resultBg = Paint().apply {
                color = Color.parseColor("#E8F5E9")
                style = Paint.Style.FILL
            }
            val resultBorder = Paint().apply {
                color = Color.parseColor("#4CAF50")
                style = Paint.Style.STROKE
                strokeWidth = 2f
            }

            canvas.drawRoundRect(marginX, resultCardY, rightMargin, resultCardY + 60f, 10f, 10f, resultBg)
            canvas.drawRoundRect(marginX, resultCardY, rightMargin, resultCardY + 60f, 10f, 10f, resultBorder)

            canvas.drawText("🏁 النتيجة النهائية:", rightMargin - 15f, resultCardY + 24f, resultPaint)
            val resLines = splitTextIntoLines(solution.finalResult, resultPaint, (pageWidth - 110).toFloat())
            var rY = resultCardY + 44f
            for (line in resLines.take(2)) {
                canvas.drawText(line, rightMargin - 15f, rY, resultPaint)
                rY += 16f
            }

            // Footer
            val footerPaint = Paint().apply {
                color = Color.GRAY
                textSize = 9f
                textAlign = Paint.Align.CENTER
            }
            canvas.drawText("تم استخراج التقرير بواسطة تطبيق حلول الرياضيات - الدكتور مالك الرميمة", pageWidth / 2f, pageHeight - 20f, footerPaint)

            pdfDoc.finishPage(page)

            // Save PDF to file
            val outputDir = File(context.cacheDir, "documents")
            if (!outputDir.exists()) outputDir.mkdirs()
            val pdfFile = File(outputDir, "Math_Solution_${System.currentTimeMillis()}.pdf")
            val outputStream = FileOutputStream(pdfFile)
            pdfDoc.writeTo(outputStream)
            outputStream.flush()
            outputStream.close()
            pdfDoc.close()

            // Open share intent
            val contentUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                pdfFile
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, contentUri)
                putExtra(Intent.EXTRA_SUBJECT, "حل مسألة: ${solution.category.titleArabic}")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            val chooser = Intent.createChooser(shareIntent, "حفظ أو مشاركة ملف PDF")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)

            pdfFile
        }
    }

    private fun splitTextIntoLines(text: String, paint: Paint, maxWidth: Float): List<String> {
        val result = mutableListOf<String>()
        val words = text.split(" ")
        var currentLine = StringBuilder()

        for (word in words) {
            val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
            if (paint.measureText(testLine) <= maxWidth) {
                currentLine = StringBuilder(testLine)
            } else {
                if (currentLine.isNotEmpty()) {
                    result.add(currentLine.toString())
                }
                currentLine = StringBuilder(word)
            }
        }
        if (currentLine.isNotEmpty()) {
            result.add(currentLine.toString())
        }
        return result
    }
}
