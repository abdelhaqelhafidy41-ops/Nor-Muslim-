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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HajjMoodAndAssistantRepository
import com.example.model.QuranMoodItem
import com.example.ui.components.IslamicGlassCard
import com.example.ui.components.IslamicTopBar
import com.example.ui.components.PillBadge
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.IslamicDarkCard
import com.example.ui.theme.IslamicDarkSurface
import com.example.ui.theme.IslamicGlassBorder
import com.example.ui.theme.TextGold
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun QuranMoodScreen(
    hajjMoodRepo: HajjMoodAndAssistantRepository,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val moods = remember { hajjMoodRepo.quranMoods }
    var selectedMood by remember { mutableStateOf(moods[0]) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("quran_mood_screen")
    ) {
        IslamicTopBar(
            title = "«قرآن مود» والحالات اليومية",
            subtitle = "١٤ حالة وجدانية مع البلسم القرآني والدعاء المأثور",
            onBackClick = onBackClick
        )

        // Moods Selector Chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(moods) { mood ->
                val isSel = mood.id == selectedMood.id
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSel) GoldPrimary else IslamicDarkCard)
                        .border(
                            1.dp,
                            if (isSel) GoldLight else IslamicGlassBorder.copy(alpha = 0.4f),
                            RoundedCornerShape(16.dp)
                        )
                        .clickable { selectedMood = mood }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                        .testTag("mood_chip_${mood.id}")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = mood.iconEmoji, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = mood.titleArabic,
                            color = if (isSel) Color(0xFF1E1700) else TextPrimary,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Main Hero Spiritual Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(selectedMood.gradientStartHex),
                                    Color(selectedMood.gradientEndHex),
                                    Color(0xFF070D1E)
                                )
                            )
                        )
                        .border(1.5.dp, GoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                        .padding(20.dp)
                        .testTag("mood_hero_card")
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            PillBadge(text = selectedMood.surahAndVerse, color = GoldLight)
                            Row {
                                IconButton(
                                    onClick = { /* Play audio snippet */ },
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(Color(0x33FFFFFF))
                                ) {
                                    Icon(Icons.Default.VolumeUp, contentDescription = "استماع", tint = GoldLight, modifier = Modifier.size(18.dp))
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                IconButton(
                                    onClick = { /* Share snippet */ },
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(Color(0x33FFFFFF))
                                ) {
                                    Icon(Icons.Default.Share, contentDescription = "مشاركة", tint = Color.White, modifier = Modifier.size(18.dp))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = selectedMood.ayahText,
                            style = MaterialTheme.typography.headlineMedium,
                            color = TextPrimary,
                            fontSize = 20.sp,
                            lineHeight = 32.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x2E000000))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "💡 لَطِيفَةُ التَّدَبُّرِ: ${selectedMood.shortTafseer}",
                                color = GoldLight,
                                fontSize = 13.sp,
                                lineHeight = 20.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            // Supplication Card
            item {
                IslamicGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = GoldPrimary.copy(alpha = 0.35f)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🤲", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "الدعاء النبوي المأثور في هذه الحالة",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextGold,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = selectedMood.duaText,
                            color = TextPrimary,
                            fontSize = 15.sp,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Related Dhikr
            item {
                IslamicGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = IslamicGlassBorder
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "📿", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "الذكر والتسبيح الموصى به",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = selectedMood.relatedDhikr,
                            color = GoldLight,
                            fontSize = 14.sp,
                            lineHeight = 22.sp
                        )
                    }
                }
            }
        }
    }
}
