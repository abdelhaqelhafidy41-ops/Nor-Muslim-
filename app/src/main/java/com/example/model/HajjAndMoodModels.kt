package com.example.model

data class HajjStep(
    val stepOrder: Int,
    val title: String,
    val phase: String, // إحرام، طواف، منى، عرفات، مزدلفة، رمي الجمار، طواف الوداع
    val description: String,
    val supplication: String = "",
    val fiqhRule: String = "سنة", // ركن / واجب / سنة
    val prohibitions: List<String> = emptyList(),
    val isDone: Boolean = false
)

enum class LapRitualType(val arabicName: String, val maxLaps: Int = 7) {
    TAWAF("طواف الكعبة المشرفة"),
    SAI("السعي بين الصفا والمروة")
}

data class LapCounterState(
    val type: LapRitualType = LapRitualType.TAWAF,
    val currentLap: Int = 1,
    val isFinished: Boolean = false,
    val activeDua: String = "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ"
)

data class QuranMoodItem(
    val id: String,
    val titleArabic: String, // الحزن، القلق، الخوف، طلب الرزق، إلخ
    val iconEmoji: String,
    val ayahText: String,
    val surahAndVerse: String,
    val shortTafseer: String,
    val duaText: String,
    val relatedDhikr: String,
    val gradientStartHex: Long,
    val gradientEndHex: Long
)
