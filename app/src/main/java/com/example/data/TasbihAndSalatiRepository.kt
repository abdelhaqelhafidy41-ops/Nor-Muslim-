package com.example.data

import com.example.model.DayPrayerLog
import com.example.model.PrayerCommitmentStats
import com.example.model.PrayerLogStatus
import com.example.model.RakatCounterState
import com.example.model.TasbihDhikr
import com.example.model.TasbihSessionRecord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TasbihAndSalatiRepository {

    // --- Electronic Tasbih ---
    val defaultDhikrs = listOf(
        TasbihDhikr("subhanallah", "سُبْحَانَ اللَّهِ", "تنزيه الله تعالى عن كل نقص وعيب، وثوابها ملء الميزان", 33, 0, 1420),
        TasbihDhikr("alhamdulillah", "الحَمْدُ لِلَّهِ", "إثبات صفات الكمال لله، وأفضل الدعاء الحمد لله", 33, 0, 980),
        TasbihDhikr("allahuakbar", "اللَّهُ أَكْبَرُ", "إعظام الله في القلب فوق كل شيء في الوجود", 33, 0, 1150),
        TasbihDhikr("tahlil", "لَا إِلَهَ إِلَّا اللَّهُ", "كلمة الإخلاص ومفتاح الجنة وأفضل الذكر", 100, 0, 2400),
        TasbihDhikr("istighfar", "أَسْتَغْفِرُ اللَّهَ وَأَتُوبُ إِلَيْهِ", "جالب للرزق ودافع للبلاء ومغفرة للذنوب", 100, 0, 3100),
        TasbihDhikr("salawat", "اللَّهُمَّ صَلِّ وَسَلِّمْ عَلَى نَبِيِّنَا مُحَمَّدٍ", "من صلى عليه صلاة صلى الله عليه بها عشراً", 100, 0, 2850),
        TasbihDhikr("hawqala", "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ", "كنز من كنوز الجنة ودواء لتسعة وتسعين داء", 33, 0, 890),
        TasbihDhikr("subhan_wa_bihamdih", "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ ، سُبْحَانَ اللَّهِ العَظِيمِ", "كلمتان خفيفتان على اللسان ثقيلتان في الميزان حبيبتان إلى الرحمن", 100, 0, 1600)
    )

    private val _selectedDhikr = MutableStateFlow(defaultDhikrs[0])
    val selectedDhikr: StateFlow<TasbihDhikr> = _selectedDhikr.asStateFlow()

    private val _vibrationEnabled = MutableStateFlow(true)
    val vibrationEnabled: StateFlow<Boolean> = _vibrationEnabled.asStateFlow()

    private val _soundEnabled = MutableStateFlow(false)
    val soundEnabled: StateFlow<Boolean> = _soundEnabled.asStateFlow()

    private val _dailyHistory = MutableStateFlow(
        listOf(
            TasbihSessionRecord("1", "سُبْحَانَ اللَّهِ", 99, "اليوم"),
            TasbihSessionRecord("2", "أَسْتَغْفِرُ اللَّهَ", 100, "اليوم"),
            TasbihSessionRecord("3", "اللَّهُمَّ صَلِّ عَلَى مُحَمَّدٍ", 200, "أمس")
        )
    )
    val dailyHistory: StateFlow<List<TasbihSessionRecord>> = _dailyHistory.asStateFlow()

    fun selectDhikr(dhikr: TasbihDhikr) {
        _selectedDhikr.value = dhikr
    }

    fun incrementTasbih() {
        val cur = _selectedDhikr.value
        val nextCount = cur.currentCount + 1
        val newDhikr = cur.copy(
            currentCount = nextCount,
            totalCountLifetime = cur.totalCountLifetime + 1
        )
        _selectedDhikr.value = newDhikr
    }

    fun resetTasbih() {
        val cur = _selectedDhikr.value
        if (cur.currentCount > 0) {
            val record = TasbihSessionRecord(
                id = System.currentTimeMillis().toString(),
                dhikrTitle = cur.arabicPhrase,
                count = cur.currentCount,
                dateFormatted = "الآن"
            )
            _dailyHistory.value = listOf(record) + _dailyHistory.value
        }
        _selectedDhikr.value = cur.copy(currentCount = 0)
    }

    fun toggleVibration() {
        _vibrationEnabled.value = !_vibrationEnabled.value
    }

    fun toggleSound() {
        _soundEnabled.value = !_soundEnabled.value
    }

    // --- Salati Prayer Tracking ---
    private val _todayLog = MutableStateFlow(
        DayPrayerLog(
            dateKey = "today",
            fajr = PrayerLogStatus.CONGREGATION,
            dhuhr = PrayerLogStatus.CONGREGATION,
            asr = PrayerLogStatus.ON_TIME,
            maghrib = PrayerLogStatus.CONGREGATION,
            isha = PrayerLogStatus.ON_TIME,
            sunanCompleted = 10,
            witrPrayed = true,
            qiyamPrayed = false
        )
    )
    val todayLog: StateFlow<DayPrayerLog> = _todayLog.asStateFlow()

    private val _rakatState = MutableStateFlow(RakatCounterState())
    val rakatState: StateFlow<RakatCounterState> = _rakatState.asStateFlow()

    fun updatePrayerStatus(prayerIndex: Int, status: PrayerLogStatus) {
        val cur = _todayLog.value
        _todayLog.value = when (prayerIndex) {
            0 -> cur.copy(fajr = status)
            1 -> cur.copy(dhuhr = status)
            2 -> cur.copy(asr = status)
            3 -> cur.copy(maghrib = status)
            4 -> cur.copy(isha = status)
            else -> cur
        }
    }

    fun toggleWitr() {
        _todayLog.value = _todayLog.value.copy(witrPrayed = !_todayLog.value.witrPrayed)
    }

    fun toggleQiyam() {
        _todayLog.value = _todayLog.value.copy(qiyamPrayed = !_todayLog.value.qiyamPrayed)
    }

    fun setSunanCount(count: Int) {
        _todayLog.value = _todayLog.value.copy(sunanCompleted = count.coerceIn(0, 12))
    }

    fun getStats(): PrayerCommitmentStats {
        return PrayerCommitmentStats(
            weeklyCommitmentRate = 0.94f,
            onTimePrayersCount = 33,
            congregationCount = 21,
            currentStreakDays = 14,
            motivationalFeedback = "ما شاء الله تبارك الله! استمرارك على الصلاة في وقتها وبالمسجد نور وبركة ليومك."
        )
    }

    // Rakat / Sujood counter to remove doubts
    fun startRakatCounter(prayerName: String, totalRakats: Int) {
        _rakatState.value = RakatCounterState(
            targetPrayer = prayerName,
            totalRakats = totalRakats,
            currentRakat = 1,
            currentSujood = 1,
            isCompleted = false
        )
    }

    fun nextSujoodOrRakat() {
        val s = _rakatState.value
        if (s.isCompleted) return

        if (s.currentSujood < 2) {
            _rakatState.value = s.copy(currentSujood = s.currentSujood + 1)
        } else {
            if (s.currentRakat < s.totalRakats) {
                _rakatState.value = s.copy(
                    currentRakat = s.currentRakat + 1,
                    currentSujood = 1
                )
            } else {
                _rakatState.value = s.copy(isCompleted = true)
            }
        }
    }

    fun resetRakatCounter() {
        val s = _rakatState.value
        _rakatState.value = s.copy(currentRakat = 1, currentSujood = 1, isCompleted = false)
    }
}
