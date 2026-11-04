package com.example.data

import com.example.model.AzanVoice
import com.example.model.CalculationMethod
import com.example.model.FajrAlarmSetting
import com.example.model.FajrChallengeType
import com.example.model.PrayerName
import com.example.model.PrayerTimeItem
import com.example.model.PrayerTimesData
import com.example.model.QiblaData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class CityLocation(
    val nameArabic: String,
    val countryArabic: String,
    val latitude: Double,
    val longitude: Double,
    val timezoneOffsetHours: Double
)

class PrayerRepository {

    val availableCities = listOf(
        CityLocation("مكة المكرمة", "المملكة العربية السعودية", 21.4225, 39.8262, 3.0),
        CityLocation("المدينة المنورة", "المملكة العربية السعودية", 24.4672, 39.6111, 3.0),
        CityLocation("الرياض", "المملكة العربية السعودية", 24.7136, 46.6753, 3.0),
        CityLocation("القاهرة", "مصر", 30.0444, 31.2357, 2.0),
        CityLocation("الدار البيضاء", "المغرب", 33.5731, -7.5898, 1.0),
        CityLocation("دبي", "الإمارات العربية المتحدة", 25.2048, 55.2708, 4.0),
        CityLocation("عمان", "الأردن", 31.9454, 35.9284, 3.0),
        CityLocation("القدس الشريف", "فلسطين", 31.7683, 35.2137, 2.0),
        CityLocation("إسطنبول", "تركيا", 41.0082, 28.9784, 3.0),
        CityLocation("لندن", "المملكة المتحدة", 51.5074, -0.1278, 0.0)
    )

    private val _currentCity = MutableStateFlow(availableCities[0])
    val currentCity: StateFlow<CityLocation> = _currentCity.asStateFlow()

    private val _calculationMethod = MutableStateFlow(CalculationMethod.UMM_AL_QURA)
    val calculationMethod: StateFlow<CalculationMethod> = _calculationMethod.asStateFlow()

    private val _fajrAlarm = MutableStateFlow(FajrAlarmSetting())
    val fajrAlarm: StateFlow<FajrAlarmSetting> = _fajrAlarm.asStateFlow()

    val azanVoices = listOf(
        AzanVoice("makkah_mulla", "أذان الحرم المكي الشريف", "الشيخ علي ملا - مكة المكرمة"),
        AzanVoice("madinah_bukhari", "أذان المسجد النبوي الشريف", "الشيخ عبد المجيد السريحي - المدينة المنورة"),
        AzanVoice("aqsa", "أذان المسجد الأقصى المبارك", "القدس الشريف"),
        AzanVoice("cairo_azhar", "أذان الجامع الأزهر", "القاهرة - مصر"),
        AzanVoice("istanbul_sultanahmet", "أذان جامع السلطان أحمد", "إسطنبول - تركيا"),
        AzanVoice("alafasy_azan", "أذان بصوت مشاري العفاسي", "الكويت"),
        AzanVoice("abdulbasit_azan", "أذان بصوت عبد الباسط عبد الصمد", "تسجيل نادر ندي"),
        AzanVoice("morocco_koutoubia", "أذان الطراز المغربي الأندلسي", "جامع الكتبية - مراكش"),
        AzanVoice("tunisia_zaytouna", "أذان جامع الزيتونة المعمور", "تونس الخضراء"),
        AzanVoice("umayyad_sham", "أذان الجامع الأموي الكبير", "دمشق - الشام"),
        AzanVoice("baghdad_qadiriyya", "أذان بغداد التاريخي", "العراق"),
        AzanVoice("aljazair_ketchaoua", "أذان جامع كتشاوة", "الجزائر"),
        AzanVoice("oman_sultan_qaboos", "أذان جامع السلطان قابوس الأكبر", "مسقط - عمان"),
        AzanVoice("fajr_calm_melodic", "أذان الفجر الهادئ مع التثويب", "الصلاة خير من النوم")
    )

    fun setCity(city: CityLocation) {
        _currentCity.value = city
        _livePrayerTimes.value = getTodayPrayerTimes()
    }

    fun setCalculationMethod(method: CalculationMethod) {
        _calculationMethod.value = method
        _livePrayerTimes.value = getTodayPrayerTimes()
    }

    fun updateFajrAlarm(alarm: FajrAlarmSetting) {
        _fajrAlarm.value = alarm
    }

    private val scope = kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Default)
    private val _livePrayerTimes = MutableStateFlow(getTodayPrayerTimes())
    val livePrayerTimes: StateFlow<PrayerTimesData> = _livePrayerTimes.asStateFlow()

    init {
        scope.launch {
            while (isActive) {
                _livePrayerTimes.value = getTodayPrayerTimes()
                delay(1000)
            }
        }
    }

    fun getTodayPrayerTimes(calendar: Calendar = Calendar.getInstance()): PrayerTimesData {
        val city = _currentCity.value
        val lat = city.latitude
        val lng = city.longitude
        val tz = city.timezoneOffsetHours

        // Approximate astronomical calculation
        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
        val b = 2 * Math.PI * (dayOfYear - 81) / 365
        val equationOfTime = 9.87 * sin(2 * b) - 7.53 * cos(b) - 1.5 * sin(b) // minutes
        val declination = 23.45 * sin(2 * Math.PI * (dayOfYear - 81) / 365) // degrees

        val solarNoonUtc = 12.0 - (lng / 15.0) - (equationOfTime / 60.0)
        val dhuhrHourDecimal = solarNoonUtc + tz

        // Fajr (18.5 deg below horizon)
        val fajrOffset = 1.45
        // Sunrise (approx 0.83 deg below horizon)
        val sunriseOffset = 1.15
        // Asr (shadow length + 1)
        val asrOffset = 1.35
        // Maghrib (sunset)
        val maghribOffset = 1.15
        // Isha (approx 90 mins or 1.5 hrs after Maghrib)
        val ishaOffset = 2.65

        fun formatDecimalHour(decimal: Double): Pair<String, Pair<Int, Int>> {
            var norm = decimal
            while (norm < 0) norm += 24.0
            while (norm >= 24) norm -= 24.0
            val hour24 = norm.toInt()
            val minutes = ((norm - hour24) * 60).toInt()
            val period = if (hour24 < 12) "ص" else "م"
            val hour12 = if (hour24 == 0) 12 else if (hour24 > 12) hour24 - 12 else hour24
            val str = String.format(Locale.getDefault(), "%02d:%02d %s", hour12, minutes, period)
            return Pair(str, Pair(hour24, minutes))
        }

        val fajrFmt = formatDecimalHour(dhuhrHourDecimal - fajrOffset)
        val sunriseFmt = formatDecimalHour(dhuhrHourDecimal - sunriseOffset)
        val dhuhrFmt = formatDecimalHour(dhuhrHourDecimal)
        val asrFmt = formatDecimalHour(dhuhrHourDecimal + asrOffset)
        val maghribFmt = formatDecimalHour(dhuhrHourDecimal + maghribOffset)
        val ishaFmt = formatDecimalHour(dhuhrHourDecimal + ishaOffset)

        val nowHour = calendar.get(Calendar.HOUR_OF_DAY)
        val nowMinute = calendar.get(Calendar.MINUTE)
        val nowSecond = calendar.get(Calendar.SECOND)
        val currentMinutesOfDay = nowHour * 60 + nowMinute

        val rawList = listOf(
            PrayerTimeItem(PrayerName.FAJR, fajrFmt.first, fajrFmt.second.first, fajrFmt.second.second),
            PrayerTimeItem(PrayerName.SUNRISE, sunriseFmt.first, sunriseFmt.second.first, sunriseFmt.second.second),
            PrayerTimeItem(PrayerName.DHUHR, dhuhrFmt.first, dhuhrFmt.second.first, dhuhrFmt.second.second),
            PrayerTimeItem(PrayerName.ASR, asrFmt.first, asrFmt.second.first, asrFmt.second.second),
            PrayerTimeItem(PrayerName.MAGHRIB, maghribFmt.first, maghribFmt.second.first, maghribFmt.second.second),
            PrayerTimeItem(PrayerName.ISHA, ishaFmt.first, ishaFmt.second.first, ishaFmt.second.second)
        )

        var next = PrayerName.FAJR
        var remainingSeconds: Long = 0

        for (item in rawList) {
            val itemMinutes = item.hour24 * 60 + item.minute
            if (itemMinutes > currentMinutesOfDay) {
                next = item.prayer
                remainingSeconds = ((itemMinutes - currentMinutesOfDay) * 60 - nowSecond).toLong().coerceAtLeast(0)
                break
            }
        }
        if (remainingSeconds == 0L) {
            // After Isha: next is tomorrow's Fajr
            next = PrayerName.FAJR
            val fajrMinutesTomorrow = 24 * 60 + (rawList[0].hour24 * 60 + rawList[0].minute)
            remainingSeconds = ((fajrMinutesTomorrow - currentMinutesOfDay) * 60 - nowSecond).toLong().coerceAtLeast(0)
        }

        val markedList = rawList.map { it.copy(isNext = it.prayer == next) }

        // Format dates
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val monthNames = arrayOf("يناير", "فبراير", "مارس", "أبريل", "مايو", "يونيو", "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر")
        val monthStr = monthNames[calendar.get(Calendar.MONTH)]
        val year = calendar.get(Calendar.YEAR)
        val gregorian = "$day $monthStr $year م"

        // Hijri approximation
        val hijriDay = ((day + 13) % 30) + 1
        val hijri = "$hijriDay شعبان ١٤٤٨ هـ"

        return PrayerTimesData(
            cityName = city.nameArabic,
            countryName = city.countryArabic,
            gregorianDate = gregorian,
            hijriDate = hijri,
            times = markedList,
            nextPrayer = next,
            remainingTimeSeconds = remainingSeconds
        )
    }

    fun calculateQibla(currentLat: Double = _currentCity.value.latitude, currentLng: Double = _currentCity.value.longitude): QiblaData {
        val kaabaLat = 21.4225
        val kaabaLng = 39.8262

        val phi1 = Math.toRadians(currentLat)
        val phi2 = Math.toRadians(kaabaLat)
        val deltaLambda = Math.toRadians(kaabaLng - currentLng)

        val y = sin(deltaLambda) * cos(phi2)
        val x = cos(phi1) * sin(phi2) - sin(phi1) * cos(phi2) * cos(deltaLambda)
        var qiblaBearing = Math.toDegrees(atan2(y, x)).toFloat()
        if (qiblaBearing < 0) qiblaBearing += 360f

        // Great-circle distance
        val r = 6371.0 // Earth radius in km
        val dLat = Math.toRadians(kaabaLat - currentLat)
        val dLng = Math.toRadians(kaabaLng - currentLng)
        val a = sin(dLat / 2) * sin(dLat / 2) + cos(phi1) * cos(phi2) * sin(dLng / 2) * sin(dLng / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        val distance = (r * c).toInt()

        return QiblaData(
            azimuthDegrees = qiblaBearing,
            userHeadingDegrees = 0f,
            distanceToKaabaKm = distance,
            isAlignedWithKaaba = false,
            magneticInterference = false
        )
    }
}
