package com.example.model

enum class PrayerName(val arabicName: String, val isObligatory: Boolean) {
    FAJR("الفجر", true),
    SUNRISE("الشروق", false),
    DHUHR("الظهر", true),
    ASR("العصر", true),
    MAGHRIB("المغرب", true),
    ISHA("العشاء", true)
}

data class PrayerTimeItem(
    val prayer: PrayerName,
    val timeFormatted: String, // "05:12 ص"
    val hour24: Int,
    val minute: Int,
    val isNext: Boolean = false
)

data class PrayerTimesData(
    val cityName: String,
    val countryName: String,
    val gregorianDate: String,
    val hijriDate: String,
    val times: List<PrayerTimeItem>,
    val nextPrayer: PrayerName,
    val remainingTimeSeconds: Long
)

enum class CalculationMethod(val id: String, val titleArabic: String) {
    UMM_AL_QURA("umm_al_qura", "أم القرى - مكة المكرمة"),
    MWL("mwl", "رابطة العالم الإسلامي"),
    EGYPTIAN("egyptian", "الهيئة العامة المصرية للمساحة"),
    ISNA("isna", "الجمعية الإسلامية لأمريكا الشمالية"),
    KARACHI("karachi", "جامعة العلوم الإسلامية بكراتشي")
}

data class AzanVoice(
    val id: String,
    val title: String,
    val reciterOrPlace: String,
    val isFull: Boolean = true
)

enum class FajrChallengeType(val titleArabic: String, val description: String) {
    DHIKR_TASBIH("التسبيح ٣ مرات", "سبّح بحمد ربك لإيقاف التنبيه"),
    MATH_PUZZLE("مسألة حسابية بسيطة", "حل مسألة لتنشيط العقل والاستيقاظ"),
    DUA_RECITE("ترديد دعاء الاستيقاظ", "الحمد لله الذي أحيانا بعد ما أماتنا")
}

data class FajrAlarmSetting(
    val isEnabled: Boolean = true,
    val minutesBeforeFajr: Int = 15,
    val soundId: String = "azan_makkah",
    val challengeType: FajrChallengeType = FajrChallengeType.DHIKR_TASBIH,
    val allowsSnooze: Boolean = true
)

data class QiblaData(
    val azimuthDegrees: Float,
    val userHeadingDegrees: Float = 0f,
    val distanceToKaabaKm: Int = 0,
    val isAlignedWithKaaba: Boolean = false,
    val magneticInterference: Boolean = false
)
