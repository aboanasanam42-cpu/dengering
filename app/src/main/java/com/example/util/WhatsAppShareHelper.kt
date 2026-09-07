package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.data.model.MathSolution
import java.net.URLEncoder

object WhatsAppShareHelper {

    private const val DEFAULT_PHONE_NUMBER = "967771124103"

    fun shareToWhatsApp(
        context: Context,
        solution: MathSolution,
        targetPhone: String = DEFAULT_PHONE_NUMBER
    ) {
        val messageText = solution.toFormattedString()
        try {
            val encodedMessage = URLEncoder.encode(messageText, "UTF-8")
            val url = "https://wa.me/$targetPhone?text=$encodedMessage"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            // Fallback to generic system share intent
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, messageText)
                putExtra(Intent.EXTRA_SUBJECT, "حل المسألة الرياضية - ${solution.category.titleArabic}")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            val chooser = Intent.createChooser(shareIntent, "مشاركة الحل عبر...")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)
        }
    }
}
