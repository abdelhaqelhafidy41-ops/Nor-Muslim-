package com.example.model

enum class PrayerLogStatus(val arabicLabel: String) {
    CONGREGATION("في جماعة"),
    ON_TIME("في وقتها"),
    LATE("قضاءً"),
    NOT_PRAYED("لم تُصلَّ")
}

data class DayPrayerLog(
    val dateKey: String,
    val fajr: PrayerLogStatus = PrayerLogStatus.ON_TIME,
    val dhuhr: PrayerLogStatus = PrayerLogStatus.CONGREGATION,
    val asr: PrayerLogStatus = PrayerLogStatus.ON_TIME,
    val maghrib: PrayerLogStatus = PrayerLogStatus.CONGREGATION,
    val isha: PrayerLogStatus = PrayerLogStatus.ON_TIME,
    val sunanCompleted: Int = 10, // e.g. out of 12
    val witrPrayed: Boolean = true,
    val qiyamPrayed: Boolean = false,
    val notes: String = ""
)

data class PrayerCommitmentStats(
    val weeklyCommitmentRate: Float, // 0..1
    val onTimePrayersCount: Int,
    val congregationCount: Int,
    val currentStreakDays: Int,
    val motivationalFeedback: String
)

data class RakatCounterState(
    val targetPrayer: String = "صلاة العصر",
    val totalRakats: Int = 4,
    val currentRakat: Int = 1,
    val currentSujood: Int = 1,
    val isCompleted: Boolean = false
)
