package com.example.data

import com.example.model.Ayah
import com.example.model.MemorizationMethod
import com.example.model.MemorizationPlan
import com.example.model.MemorizationQuiz
import com.example.model.Reciter
import com.example.model.Surah
import com.example.model.TafseerSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class QuranRepository {

    private val _lastReadSurah = MutableStateFlow(QuranData.surahs.first { it.number == 67 }) // Al-Mulk default
    val lastReadSurah: StateFlow<Surah> = _lastReadSurah.asStateFlow()

    private val _lastReadAyah = MutableStateFlow(1)
    val lastReadAyah: StateFlow<Int> = _lastReadAyah.asStateFlow()

    private val _memorizationPlans = MutableStateFlow(
        listOf(
            MemorizationPlan(
                id = "plan_mulk",
                title = "حفظ سورة الملك المباركة",
                surahNumber = 67,
                surahName = "الملك",
                startAyah = 1,
                endAyah = 30,
                targetDays = 15,
                currentDay = 6,
                masteredAyahs = 12,
                method = MemorizationMethod.REPETITION,
                userName = "أحمد عبد الله",
                completionDate = "1448/02/10 هـ"
            ),
            MemorizationPlan(
                id = "plan_kahf",
                title = "حفظ أوائل سورة الكهف (العشر الأولى)",
                surahNumber = 18,
                surahName = "الكهف",
                startAyah = 1,
                endAyah = 10,
                targetDays = 5,
                currentDay = 5,
                masteredAyahs = 10,
                method = MemorizationMethod.QUIZ,
                isCompleted = true,
                userName = "أحمد عبد الله",
                completionDate = "1448/01/25 هـ"
            )
        )
    )
    val memorizationPlans: StateFlow<List<MemorizationPlan>> = _memorizationPlans.asStateFlow()

    fun getAllSurahs(): List<Surah> = QuranData.surahs

    fun getAyahsForSurah(surahNumber: Int): List<Ayah> {
        val found = QuranData.sampleAyahs[surahNumber]
        if (found != null) return found

        // Generate full structured verses for the surah if not in pre-seeded map
        val surah = QuranData.surahs.firstOrNull { it.number == surahNumber } ?: QuranData.surahs[0]
        return (1..surah.totalAyahs.coerceAtMost(30)).map { idx ->
            Ayah(
                surahNumber = surahNumber,
                numberInSurah = idx,
                numberInQuran = (surahNumber * 10) + idx,
                text = "آيات سورة ${surah.nameArabic} المباركة - الآية رقم ($idx)",
                pageNumber = surah.pageNumber,
                juzNumber = ((surah.pageNumber - 1) / 20) + 1,
                hizbQuarter = ((surah.pageNumber - 1) / 5) + 1
            )
        }
    }

    fun searchQuran(query: String): List<Ayah> {
        if (query.isBlank()) return emptyList()
        val clean = query.trim().replace("[ًٌٍَُِّْـ]".toRegex(), "")
        val results = mutableListOf<Ayah>()
        QuranData.sampleAyahs.values.flatten().forEach { ayah ->
            val textClean = ayah.text.replace("[ًٌٍَُِّْـ]".toRegex(), "")
            if (textClean.contains(clean, ignoreCase = true)) {
                results.add(ayah)
            }
        }
        return results
    }

    fun getTafseer(surahNumber: Int, ayahNumber: Int, tafseerId: String): String {
        return QuranData.sampleTafseers[Pair(surahNumber, tafseerId)]
            ?: QuranData.sampleTafseers[Pair(surahNumber, "muyassar")]
            ?: "تفسير الآية المباركة رقم ($ayahNumber) من سورة ${getSurah(surahNumber)?.nameArabic ?: ""}: بيان عظيم لتوحيد الله تعالى ورحمته بعباده وبيان شرائعه وإحسانه."
    }

    fun getSurah(surahNumber: Int): Surah? {
        return QuranData.surahs.firstOrNull { it.number == surahNumber }
    }

    fun getTafseerSources(): List<TafseerSource> = QuranData.tafseerSources

    fun getReciters(): List<Reciter> = QuranData.reciters

    fun updateLastRead(surahNumber: Int, ayahNumber: Int) {
        val s = getSurah(surahNumber) ?: return
        _lastReadSurah.value = s
        _lastReadAyah.value = ayahNumber
    }

    fun getAyahOfTheDay(): Pair<Ayah, String> {
        val ayah = Ayah(
            surahNumber = 2,
            numberInSurah = 255,
            numberInQuran = 262,
            text = "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَّهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ",
            pageNumber = 42,
            juzNumber = 3,
            hizbQuarter = 5
        )
        val tafseer = "آية الكرسي هي أعظم آية في كتاب الله؛ تضمنت توحيد الله الأكبر وعظمته وسعة سلطانه وقيوميته الدائمة التي لا يعتريها نعاس ولا نوم."
        return Pair(ayah, tafseer)
    }

    fun generateQuizQuestions(surahNumber: Int): List<MemorizationQuiz> {
        return listOf(
            MemorizationQuiz(
                question = "أكمل الآية الكريمة: ﴿الَّذِي خَلَقَ الْمَوْتَ وَالْحَيَاةَ لِيَبْلُوَكُمْ ...﴾",
                promptType = "إكمال الآية",
                options = listOf(
                    "أَيُّكُمْ أَحْسَنُ عَمَلًا ۚ وَهُوَ الْعَزِيزُ الْغَفُورُ",
                    "أَيُّكُمْ أَشَدُّ إِيمَانًا ۚ وَهُوَ الرَّحِيمُ",
                    "لَعَلَّكُمْ تَتَّقُونَ وَتَشْكُرُونَ",
                    "فَمَنِ اتَّبَعَ هُدَايَ فَلَا يَضِلُّ"
                ),
                correctIndex = 0,
                surahName = "الملك",
                ayahNumber = 2
            ),
            MemorizationQuiz(
                question = "ما هي الآية التالية لقوله تعالى: ﴿قُلْ هُوَ اللَّهُ أَحَدٌ﴾؟",
                promptType = "الآية التالية",
                options = listOf(
                    "وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ",
                    "اللَّهُ الصَّمَدُ",
                    "لَمْ يَلِدْ وَلَمْ يُولَدْ",
                    "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ"
                ),
                correctIndex = 1,
                surahName = "الإخلاص",
                ayahNumber = 2
            ),
            MemorizationQuiz(
                question = "في سورة الفاتحة، ما الآية التي تأتي بعد ﴿إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ﴾؟",
                promptType = "الآية التالية",
                options = listOf(
                    "مَالِكِ يَوْمِ الدِّينِ",
                    "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ",
                    "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ",
                    "الرَّحْمَٰنِ الرَّحِيمِ"
                ),
                correctIndex = 1,
                surahName = "الفاتحة",
                ayahNumber = 6
            )
        )
    }

    fun addMemorizationPlan(plan: MemorizationPlan) {
        _memorizationPlans.value = _memorizationPlans.value + plan
    }
}
