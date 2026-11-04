package com.example.data

import com.example.model.AzkarCategory
import com.example.model.DhikrItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AzkarRepository {

    private val _items = MutableStateFlow(AzkarData.azkarItems)
    val items: StateFlow<List<DhikrItem>> = _items.asStateFlow()

    private val _favorites = MutableStateFlow<Set<String>>(setOf("sabah_1", "sabah_3", "quran_1"))
    val favorites: StateFlow<Set<String>> = _favorites.asStateFlow()

    fun getCategories(): List<AzkarCategory> = AzkarData.categories

    fun getItemsForCategory(categoryId: String): List<DhikrItem> {
        return _items.value.filter { it.categoryId == categoryId }
    }

    fun incrementDhikr(id: String) {
        val updated = _items.value.map { item ->
            if (item.id == id) {
                val newCount = (item.currentCount + 1).coerceAtMost(item.countTarget)
                item.copy(currentCount = newCount)
            } else item
        }
        _items.value = updated
    }

    fun resetDhikr(id: String) {
        val updated = _items.value.map { item ->
            if (item.id == id) item.copy(currentCount = 0) else item
        }
        _items.value = updated
    }

    fun resetCategory(categoryId: String) {
        val updated = _items.value.map { item ->
            if (item.categoryId == categoryId) item.copy(currentCount = 0) else item
        }
        _items.value = updated
    }

    fun toggleFavorite(id: String) {
        val cur = _favorites.value.toMutableSet()
        if (cur.contains(id)) cur.remove(id) else cur.add(id)
        _favorites.value = cur
    }

    fun getFeaturedDhikr(): DhikrItem {
        return _items.value.firstOrNull { it.id == "sabah_3" } ?: _items.value[0]
    }
}
