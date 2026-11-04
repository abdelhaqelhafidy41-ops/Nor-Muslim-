package com.example.model

data class RadioStation(
    val id: String,
    val name: String,
    val category: String, // تلاوات، دروس، إذاعات دولية، أناشيد
    val country: String,
    val streamUrl: String,
    val isFavorite: Boolean = false,
    val isPlaying: Boolean = false
)

data class IslamicWallpaper(
    val id: String,
    val title: String,
    val category: String, // المساجد، الكعبة، الخط العربي، الزخارف، الطبيعة، آيات
    val primaryColorHex: Long,
    val secondaryColorHex: Long,
    val calligraphyQuote: String = ""
)

data class WidgetPreview(
    val id: String,
    val title: String,
    val description: String,
    val sizeLabel: String // 2x2, 4x2, 4x1
)

data class AssistantFaq(
    val id: String,
    val question: String,
    val answer: String,
    val quranEvidence: String,
    val hadithEvidence: String,
    val topic: String
)

data class AssistantMessage(
    val id: String,
    val isUser: Boolean,
    val text: String,
    val evidenceList: List<String> = emptyList(),
    val hadithGrade: String = "",
    val requiresMuftiDisclaimer: Boolean = false
)
