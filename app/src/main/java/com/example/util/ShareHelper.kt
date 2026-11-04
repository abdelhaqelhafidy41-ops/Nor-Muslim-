package com.example.util

import android.content.Context
import android.content.Intent

object ShareHelper {

    fun shareText(context: Context, title: String, content: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, title)
            putExtra(Intent.EXTRA_TEXT, content)
        }
        val chooser = Intent.createChooser(intent, title)
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    }

    fun shareAyah(context: Context, surahName: String, ayahNumber: Int, text: String, tafseer: String? = null) {
        val formatted = buildString {
            append("﴿$text﴾\n")
            append("[سورة $surahName - الآية $ayahNumber]\n")
            if (!tafseer.isNullOrBlank()) {
                append("\nالتفسير الميسر: $tafseer\n")
            }
            append("\n— تم المشاركة عبر تطبيق «نور المسلم»")
        }
        shareText(context, "مشاركة آية كريمة", formatted)
    }

    fun shareDhikr(context: Context, text: String, benefit: String, reference: String) {
        val formatted = buildString {
            append("$text\n\n")
            if (benefit.isNotBlank()) {
                append("✨ الفضل: $benefit\n")
            }
            if (reference.isNotBlank()) {
                append("📖 المصدر: $reference\n")
            }
            append("\n— تطبيق «نور المسلم» للأذكار والتحصينات")
        }
        shareText(context, "مشاركة ذكر نبوي مبارك", formatted)
    }

    fun shareCertificate(context: Context, userName: String, surahName: String, date: String) {
        val formatted = buildString {
            append("📜 شهادة إتمام حفظ القرآن الكريم\n\n")
            append("يشهد تطبيق «نور المسلم» بأن القارئ المبارك: $userName\n")
            append("قد أتم بفضل الله وتوفيقه حفظ: سورة $surahName\n")
            append("تاريخ الإتمام: $date\n\n")
            append("﴿يَرْفَعِ اللَّهُ الَّذِينَ آمَنُوا مِنكُمْ وَالَّذِينَ أُوتُوا الْعِلْمَ دَرَجَاتٍ﴾")
        }
        shareText(context, "شهادة إتمام حفظ سورة $surahName", formatted)
    }
}
