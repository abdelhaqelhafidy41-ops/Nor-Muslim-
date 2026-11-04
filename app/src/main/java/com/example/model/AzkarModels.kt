package com.example.model

data class DhikrItem(
    val id: String,
    val categoryId: String,
    val text: String,
    val countTarget: Int,
    var currentCount: Int = 0,
    val benefit: String = "",
    val reference: String = "", // e.g., صحيح البخاري، صحيح مسلم
    val hadithGrade: String = "صحيح",
    val audioUrl: String = "",
    val isFavorite: Boolean = false
)

data class AzkarCategory(
    val id: String,
    val title: String,
    val description: String,
    val itemCount: Int,
    val iconType: String = "star"
)
