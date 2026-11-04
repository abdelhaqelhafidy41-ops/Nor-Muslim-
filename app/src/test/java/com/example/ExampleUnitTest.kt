package com.example

import com.example.data.AzkarRepository
import com.example.data.PrayerRepository
import com.example.data.QuranRepository
import com.example.data.TasbihAndSalatiRepository
import com.example.model.PrayerName
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testQuranSurahsCount() {
        val repo = QuranRepository()
        val surahs = repo.getAllSurahs()
        assertEquals(114, surahs.size)
        assertEquals("الفَاتِحَة", surahs[0].nameArabic)
        assertEquals("النَّاس", surahs[113].nameArabic)
    }

    @Test
    fun testPrayerTimesCalculation() {
        val repo = PrayerRepository()
        val prayerData = repo.getTodayPrayerTimes()
        assertNotNull(prayerData)
        assertEquals(6, prayerData.times.size)
        assertTrue(prayerData.times.any { it.prayer == PrayerName.FAJR })
        assertTrue(prayerData.times.any { it.prayer == PrayerName.DHUHR })
        assertTrue(prayerData.times.any { it.prayer == PrayerName.ASR })
        assertTrue(prayerData.times.any { it.prayer == PrayerName.MAGHRIB })
        assertTrue(prayerData.times.any { it.prayer == PrayerName.ISHA })
    }

    @Test
    fun testAzkarCategoriesAndCounting() {
        val repo = AzkarRepository()
        val categories = repo.getCategories()
        assertTrue(categories.isNotEmpty())

        val sabahItems = repo.getItemsForCategory("sabah")
        assertTrue(sabahItems.isNotEmpty())

        val firstItem = sabahItems[0]
        val initialCount = firstItem.currentCount
        repo.incrementDhikr(firstItem.id)
        val updatedItem = repo.getItemsForCategory("sabah").first { it.id == firstItem.id }
        assertEquals(initialCount + 1, updatedItem.currentCount)
    }

    @Test
    fun testTasbihElectronicCounter() {
        val repo = TasbihAndSalatiRepository()
        val initialCount = repo.selectedDhikr.value.currentCount
        repo.incrementTasbih()
        assertEquals(initialCount + 1, repo.selectedDhikr.value.currentCount)
        repo.resetTasbih()
        assertEquals(0, repo.selectedDhikr.value.currentCount)
    }

    @Test
    fun testQiblaCalculation() {
        val repo = PrayerRepository()
        val qibla = repo.calculateQibla(21.4225, 39.8262) // In Makkah at Kaaba
        assertEquals(0, qibla.distanceToKaabaKm)
    }
}
