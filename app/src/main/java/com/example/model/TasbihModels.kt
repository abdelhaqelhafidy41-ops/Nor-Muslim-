package com.example.model

data class TasbihDhikr(
    val id: String,
    val arabicPhrase: String,
    val translationOrFadl: String,
    val defaultTarget: Int = 33,
    val currentCount: Int = 0,
    val totalCountLifetime: Long = 0
)

data class TasbihSessionRecord(
    val id: String,
    val dhikrTitle: String,
    val count: Int,
    val dateFormatted: String
)
