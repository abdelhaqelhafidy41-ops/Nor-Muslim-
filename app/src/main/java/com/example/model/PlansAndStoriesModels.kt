package com.example.model

data class PlanTask(
    val id: String,
    val title: String,
    var isDone: Boolean = false
)

data class WorshipPlan(
    val id: String,
    val title: String,
    val description: String,
    val category: String, // رمضان، ختم القرآن، قيام الليل، السنن، الحج
    val durationDays: Int,
    val currentDay: Int,
    val tasks: List<PlanTask>,
    val isCustom: Boolean = false
) {
    val progress: Float
        get() = if (tasks.isEmpty()) 0f else tasks.count { it.isDone }.toFloat() / tasks.size
}

enum class StoryCategory(val titleArabic: String) {
    SEERAH("السيرة النبوية العطرة"),
    PROPHETS("قصص الأنبياء عليهم السلام"),
    COMPANIONS("قصص الصحابة الكرام"),
    SAHABIYAT("الصحابيات وأمهات المؤمنين"),
    QURAN_STORIES("قصص القرآن الكريم")
}

data class IslamicStory(
    val id: String,
    val category: StoryCategory,
    val title: String,
    val summary: String,
    val content: String,
    val lessons: List<String>,
    val authenticSource: String,
    val isFavorite: Boolean = false
)
