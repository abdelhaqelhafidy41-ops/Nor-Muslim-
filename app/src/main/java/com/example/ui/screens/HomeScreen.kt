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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NightlightRound
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AzkarRepository
import com.example.data.PrayerRepository
import com.example.data.QuranRepository
import com.example.data.TasbihAndSalatiRepository
import com.example.model.PrayerName
import com.example.ui.components.IslamicGlassCard
import com.example.ui.components.PillBadge
import com.example.ui.theme.BluePrayer
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.IslamicDarkCard
import com.example.ui.theme.IslamicDarkSurface
import com.example.ui.theme.IslamicGlassBorder
import com.example.ui.theme.PurpleQuran
import com.example.ui.theme.TextGold
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Calendar

data class ShortcutItem(
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val route: String
)

@Composable
fun HomeScreen(
    prayerRepo: PrayerRepository,
    quranRepo: QuranRepository,
    azkarRepo: AzkarRepository,
    salatiRepo: TasbihAndSalatiRepository,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val prayerData = prayerRepo.getTodayPrayerTimes()
    val lastReadSurah by quranRepo.lastReadSurah.collectAsState()
    val lastReadAyah by quranRepo.lastReadAyah.collectAsState()
    val todayAyahData = remember { quranRepo.getAyahOfTheDay() }
    val featuredDhikr = remember { azkarRepo.getFeaturedDhikr() }
    val prayerStats = remember { salatiRepo.getStats() }

    var showTafseerDialog by remember { mutableStateOf(false) }

    val shortcuts = listOf(
        ShortcutItem("أذكار الصباح", Icons.Default.WbSunny, Color(0xFFF59E0B), "azkar_sabah"),
        ShortcutItem("أذكار المساء", Icons.Default.NightlightRound, Color(0xFF8B5CF6), "azkar_masaa"),
        ShortcutItem("المصحف", Icons.Default.Book, PurpleQuran, "quran"),
        ShortcutItem("مواقيت الصلاة", Icons.Default.AccessTime, BluePrayer, "prayer"),
        ShortcutItem("القبلة", Icons.Default.Explore, Color(0xFF38BDF8), "qibla"),
        ShortcutItem("التسبيح", Icons.Default.TouchApp, EmeraldPrimary, "tasbih"),
        ShortcutItem("الحج والعمرة", Icons.Default.FlightTakeoff, GoldPrimary, "hajj"),
        ShortcutItem("الخطط التعبدية", Icons.Default.EventNote, Color(0xFF10B981), "plans"),
        ShortcutItem("المساعد الذكي", Icons.Default.Psychology, Color(0xFFA78BFA), "assistant")
    )

    // Calculate greeting based on hour
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val greeting = if (hour in 4..11) "صباح مبارك يعمره ذكر الله" else if (hour in 12..17) "طاب يومك بنور الطاعة والقرآن" else "مساء السكينة والطمأنينة"

    val hoursLeft = prayerData.remainingTimeSeconds / 3600
    val minutesLeft = (prayerData.remainingTimeSeconds % 3600) / 60
    val secondsLeft = prayerData.remainingTimeSeconds % 60
    val countdownFormatted = String.format("%02d:%02d:%02d", hoursLeft, minutesLeft, secondsLeft)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // --- Header Banner ---
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF0F1B3B),
                                Color(0xFF0A1226)
                            )
                        )
                    )
                    .border(
                        1.dp,
                        IslamicGlassBorder.copy(alpha = 0.3f),
                        RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
                    )
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = greeting,
                                color = TextGold,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "تطبيق نور المسلم",
                                color = TextPrimary,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // City badge
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(IslamicDarkSurface)
                                .border(1.dp, IslamicGlassBorder.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                                .clickable { onNavigate("prayer") }
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = prayerData.cityName,
                                color = TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Date row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = prayerData.hijriDate,
                            color = GoldLight,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = prayerData.gregorianDate,
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Next Prayer Countdown Card
                    IslamicGlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("next_prayer_card"),
                        backgroundColor = Color(0x3B15254A),
                        borderColor = GoldPrimary.copy(alpha = 0.4f),
                        onClick = { onNavigate("prayer") }
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(GoldPrimary.copy(alpha = 0.2f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AccessTime,
                                            contentDescription = null,
                                            tint = GoldPrimary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "الصلاة القادمة",
                                            color = TextSecondary,
                                            fontSize = 11.sp
                                        )
                                        Text(
                                            text = "صلاة ${prayerData.nextPrayer.arabicName}",
                                            color = TextPrimary,
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "المتبقي للأذان",
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                    Text(
                                        text = countdownFormatted,
                                        color = GoldPrimary,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Horizontal prayer times strip
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                prayerData.times.forEach { pItem ->
                                    val isHighlighted = pItem.isNext
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(if (isHighlighted) GoldPrimary.copy(alpha = 0.2f) else Color.Transparent)
                                            .padding(horizontal = 6.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = pItem.prayer.arabicName,
                                            color = if (isHighlighted) GoldLight else TextSecondary,
                                            fontSize = 11.sp,
                                            fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.Normal
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = pItem.timeFormatted.replace(" ص", "").replace(" م", ""),
                                            color = if (isHighlighted) TextPrimary else TextSecondary,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- Quick Shortcuts Grid/Row ---
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "الوصول السريع",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(shortcuts) { item ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .width(78.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(IslamicDarkCard)
                                .border(1.dp, IslamicGlassBorder.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
                                .clickable { onNavigate(item.route) }
                                .padding(vertical = 12.dp)
                                .testTag("shortcut_${item.route}")
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(item.color.copy(alpha = 0.18f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title,
                                    tint = item.color,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = item.title,
                                color = TextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        // --- Continue Reading Card (أكمل من حيث توقفت) ---
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                IslamicGlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("continue_reading_card"),
                    backgroundColor = Color(0x351F143D),
                    borderColor = PurpleQuran.copy(alpha = 0.45f),
                    onClick = { onNavigate("quran_read_${lastReadSurah.number}") }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(PurpleQuran.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bookmark,
                                    contentDescription = null,
                                    tint = PurpleQuran,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "أكمل من حيث توقفت",
                                        color = TextGold,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    PillBadge(text = "صفحة ${lastReadSurah.pageNumber}", color = PurpleQuran)
                                }
                                Text(
                                    text = "سورة ${lastReadSurah.nameArabic} - الآية $lastReadAyah",
                                    color = TextPrimary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        IconButton(
                            onClick = { onNavigate("quran_read_${lastReadSurah.number}") },
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(PurpleQuran)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "متابعة القراءة",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }

        // --- Ayah of the Day (آية اليوم) ---
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = GoldPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "آية اليوم وتدبر",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "سورة البقرة: ٢٥٥",
                        color = TextGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                IslamicGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = GoldPrimary.copy(alpha = 0.35f),
                    backgroundColor = Color(0x33101B33)
                ) {
                    Column {
                        Text(
                            text = todayAyahData.first.text,
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextPrimary,
                            fontSize = 17.sp,
                            lineHeight = 28.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        if (showTafseerDialog) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(IslamicDarkSurface)
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = todayAyahData.second,
                                    color = TextSecondary,
                                    fontSize = 13.sp,
                                    lineHeight = 20.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ElevatedButton(
                                onClick = { showTafseerDialog = !showTafseerDialog },
                                colors = ButtonDefaults.elevatedButtonColors(
                                    containerColor = IslamicDarkSurface,
                                    contentColor = GoldPrimary
                                ),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = if (showTafseerDialog) "إخفاء التفسير" else "عرض التفسير الميسر",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            IconButton(
                                onClick = { /* Share dialog feedback */ },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(IslamicDarkSurface)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "مشاركة الآية",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- Worship Commitment & Salati Summary ---
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                IslamicGlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigate("salati") },
                    backgroundColor = Color(0x350A261D),
                    borderColor = EmeraldPrimary.copy(alpha = 0.4f)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "إنجاز العبادات والصلوات اليومية",
                                    color = EmeraldPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "نسبة الالتزام الأسبوعي: ${(prayerStats.weeklyCommitmentRate * 100).toInt()}%",
                                    color = TextPrimary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            PillBadge(
                                text = "🔥 ${prayerStats.currentStreakDays} يوماً مستمراً",
                                color = EmeraldPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        LinearProgressIndicator(
                            progress = { prayerStats.weeklyCommitmentRate },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(CircleShape),
                            color = EmeraldPrimary,
                            trackColor = Color(0x3310B981)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = prayerStats.motivationalFeedback,
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // --- Dhikr of the Day ---
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                IslamicGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = IslamicGlassBorder.copy(alpha = 0.4f),
                    backgroundColor = Color(0x30132244)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "ذكر اليوم المبارك",
                                color = TextGold,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            PillBadge(text = featuredDhikr.reference, color = GoldLight)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = featuredDhikr.text,
                            color = TextPrimary,
                            fontSize = 15.sp,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "💡 ${featuredDhikr.benefit}",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
