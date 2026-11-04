package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AzkarRepository
import com.example.model.DhikrItem
import com.example.ui.components.IslamicGlassCard
import com.example.ui.components.IslamicTopBar
import com.example.ui.components.PillBadge
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.IslamicDarkCard
import com.example.ui.theme.IslamicDarkSurface
import com.example.ui.theme.IslamicGlassBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.platform.LocalContext
import com.example.ui.theme.TextGold
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.util.AppAudioManager
import com.example.util.AppHapticManager
import com.example.util.ShareHelper

@Composable
fun AzkarScreen(
    azkarRepo: AzkarRepository,
    initialCategoryId: String = "sabah",
    onNavigateTasbih: () -> Unit,
    audioManager: AppAudioManager? = null,
    hapticManager: AppHapticManager? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val categories = remember { azkarRepo.getCategories() }
    var selectedCatId by remember { mutableStateOf(initialCategoryId) }
    val allItems by azkarRepo.items.collectAsState()
    val favorites by azkarRepo.favorites.collectAsState()

    val currentItems = remember(selectedCatId, allItems) {
        allItems.filter { it.categoryId == selectedCatId }
    }

    val selectedCategory = remember(selectedCatId) {
        categories.firstOrNull { it.id == selectedCatId } ?: categories[0]
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("azkar_screen")
    ) {
        IslamicTopBar(
            title = "الأذكار والأدعية النبوية",
            subtitle = "حصن المسلم والتحصينات اليومية",
            trailingAction = {
                ElevatedButton(
                    onClick = onNavigateTasbih,
                    shape = RoundedCornerShape(12.dp),
                    colors = androidx.compose.material3.ButtonDefaults.elevatedButtonColors(
                        containerColor = EmeraldPrimary,
                        contentColor = Color(0xFF002416)
                    ),
                    modifier = Modifier.testTag("nav_tasbih_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.TouchApp,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("السبحة", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        )

        // Category Chips Row
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { cat ->
                val isSel = cat.id == selectedCatId
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isSel) EmeraldPrimary else IslamicDarkCard)
                        .border(
                            1.dp,
                            if (isSel) EmeraldLight else IslamicGlassBorder.copy(alpha = 0.4f),
                            RoundedCornerShape(14.dp)
                        )
                        .clickable { selectedCatId = cat.id }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                        .testTag("azkar_cat_${cat.id}")
                ) {
                    Text(
                        text = cat.title,
                        color = if (isSel) Color(0xFF002213) else TextPrimary,
                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // Selected Category Info Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(IslamicDarkSurface.copy(alpha = 0.8f))
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedCategory.description,
                    color = TextSecondary,
                    fontSize = 12.sp
                )
                IconButton(
                    onClick = { azkarRepo.resetCategory(selectedCatId) },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.RestartAlt,
                        contentDescription = "إعادة ضبط الكل",
                        tint = GoldPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Azkar list
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(currentItems) { dhikr ->
                val isCompleted = dhikr.currentCount >= dhikr.countTarget
                val isFav = favorites.contains(dhikr.id)
                val progress = dhikr.currentCount.toFloat() / dhikr.countTarget.coerceAtLeast(1)

                IslamicGlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dhikr_item_${dhikr.id}"),
                    borderColor = if (isCompleted) EmeraldPrimary.copy(alpha = 0.6f) else IslamicGlassBorder.copy(alpha = 0.4f),
                    backgroundColor = if (isCompleted) Color(0x3B072B1E) else IslamicDarkCard
                ) {
                    Column {
                        // Top bar inside card: reference & grade & favorite
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                PillBadge(text = dhikr.hadithGrade, color = EmeraldPrimary)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = dhikr.reference,
                                    color = TextGold,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = {
                                        audioManager?.playDhikrAudio(dhikr.text)
                                        hapticManager?.vibrateTap()
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "استماع للذكر",
                                        tint = GoldLight,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        azkarRepo.toggleFavorite(dhikr.id)
                                        hapticManager?.vibrateTap()
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = "المفضلة",
                                        tint = if (isFav) Color(0xFFEF4444) else TextSecondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        ShareHelper.shareDhikr(
                                            context = context,
                                            text = dhikr.text,
                                            benefit = dhikr.benefit,
                                            reference = dhikr.reference
                                        )
                                        hapticManager?.vibrateTap()
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = "مشاركة",
                                        tint = TextSecondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Dhikr Text
                        Text(
                            text = dhikr.text,
                            color = TextPrimary,
                            fontSize = 17.sp,
                            lineHeight = 28.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (dhikr.benefit.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "✨ الفضل: ${dhikr.benefit}",
                                color = TextSecondary,
                                fontSize = 12.sp,
                                lineHeight = 18.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Counter Button & Progress Bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "التكرار: ${dhikr.currentCount} من ${dhikr.countTarget}",
                                        color = if (isCompleted) EmeraldLight else TextGold,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (isCompleted) {
                                        Text(text = "تم بحمد الله ✓", color = EmeraldPrimary, fontSize = 11.sp)
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                LinearProgressIndicator(
                                    progress = { progress },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(CircleShape),
                                    color = EmeraldPrimary,
                                    trackColor = IslamicDarkSurface
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            // Big Round Count Tap Button
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isCompleted) EmeraldPrimary else GoldPrimary
                                    )
                                    .clickable {
                                        if (!isCompleted) {
                                            azkarRepo.incrementDhikr(dhikr.id)
                                            if (dhikr.currentCount + 1 >= dhikr.countTarget) {
                                                hapticManager?.vibrateCompletion()
                                            } else {
                                                hapticManager?.vibrateTap()
                                            }
                                        } else {
                                            azkarRepo.resetDhikr(dhikr.id)
                                            hapticManager?.vibrateTap()
                                        }
                                    }
                                    .testTag("dhikr_tap_${dhikr.id}"),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isCompleted) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "تم",
                                        tint = Color(0xFF002213),
                                        modifier = Modifier.size(26.dp)
                                    )
                                } else {
                                    Text(
                                        text = "${dhikr.countTarget - dhikr.currentCount}",
                                        color = Color(0xFF1E1700),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
