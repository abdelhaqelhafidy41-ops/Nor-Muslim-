package com.example.model

data class Surah(
    val number: Int,
    val nameArabic: String,
    val nameEnglish: String,
    val revelationType: String, // مكية / مدنية
    val totalAyahs: Int,
    val pageNumber: Int,
    val audioUrl: String = ""
)

data class Ayah(
    val surahNumber: Int,
    val numberInSurah: Int,
    val numberInQuran: Int,
    val text: String,
    val pageNumber: Int,
    val juzNumber: Int,
    val hizbQuarter: Int,
    val isBookmarked: Boolean = false,
    val audioUrl: String = ""
)

data class TafseerSource(
    val id: String,
    val name: String,
    val author: String,
    val description: String
)

data class AyahTafseer(
    val ayahNumber: Int,
    val tafseerId: String,
    val text: String
)

data class Reciter(
    val id: String,
    val name: String,
    val style: String, // حفص عن عاصم، ورش، إلخ
    val serverPath: String,
    val isFavorite: Boolean = false,
    val isDownloaded: Boolean = false
)

data class MemorizationPlan(
    val id: String,
    val title: String,
    val surahNumber: Int,
    val surahName: String,
    val startAyah: Int,
    val endAyah: Int,
    val targetDays: Int,
    val currentDay: Int,
    val masteredAyahs: Int,
    val method: MemorizationMethod = MemorizationMethod.REPETITION,
    val isCompleted: Boolean = false,
    val userName: String = "المسلم المبارك",
    val completionDate: String = ""
)

enum class MemorizationMethod(val title: String, val desc: String) {
    REPETITION("التكرار الصوتي", "الاستماع المتكرر وتكرار الآيات حتى الرسوخ"),
    DICTATION("الحفظ بالتلقين", "قراءة الشيخ ثم ترديد المتعلم خلفه"),
    ORDERING("ترتيب الآيات", "ترتيب مقاطع الآيات المبعثرة لترسيخ السياق"),
    QUIZ("اختبار الإكمال", "اختبارات ملء الفراغ واستحضار الآية التالية")
}

data class MemorizationQuiz(
    val question: String,
    val promptType: String,
    val options: List<String>,
    val correctIndex: Int,
    val surahName: String,
    val ayahNumber: Int
)
