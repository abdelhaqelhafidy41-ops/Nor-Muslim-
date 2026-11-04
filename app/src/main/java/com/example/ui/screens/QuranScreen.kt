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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.QuranRepository
import com.example.model.Ayah
import com.example.model.Surah
import com.example.ui.components.IslamicGlassCard
import com.example.ui.components.IslamicTopBar
import com.example.ui.components.PillBadge
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.IslamicDarkCard
import com.example.ui.theme.IslamicDarkSurface
import com.example.ui.theme.IslamicGlassBorder
import com.example.ui.theme.PurpleQuran
import com.example.ui.theme.PurpleQuranLight
import com.example.ui.theme.TextGold
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun QuranScreen(
    quranRepo: QuranRepository,
    initialSurahNumber: Int? = null,
    onNavigateMemorization: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(if (initialSurahNumber != null) 1 else 0) }
    var selectedSurah by remember {
        mutableStateOf(
            if (initialSurahNumber != null) {
                quranRepo.getSurah(initialSurahNumber) ?: quranRepo.getAllSurahs()[0]
            } else {
                quranRepo.getAllSurahs()[0]
            }
        )
    }

    var searchQuery by remember { mutableStateOf("") }
    var selectedTafseerId by remember { mutableStateOf("muyassar") }
    var currentTafseerAyah by remember { mutableStateOf<Ayah?>(null) }
    var showTafseerModal by remember { mutableStateOf(false) }

    // Reading preferences
    var fontSizeSp by remember { mutableFloatStateOf(20f) }
    var isPlayingRecitation by remember { mutableStateOf(false) }
    var currentPlayingAyahIndex by remember { mutableIntStateOf(1) }
    var repeatCount by remember { mutableIntStateOf(1) }
    var playbackSpeed by remember { mutableFloatStateOf(1.0f) }
    var showSettingsModal by remember { mutableStateOf(false) }

    val allSurahs = remember { quranRepo.getAllSurahs() }
    val filteredSurahs = remember(searchQuery) {
        if (searchQuery.isBlank()) allSurahs
        else allSurahs.filter {
            it.nameArabic.contains(searchQuery.trim()) ||
            it.nameEnglish.contains(searchQuery.trim(), ignoreCase = true) ||
            it.number.toString() == searchQuery.trim()
        }
    }

    val tafseerSources = remember { quranRepo.getTafseerSources() }
    val currentAyahs = remember(selectedSurah) { quranRepo.getAyahsForSurah(selectedSurah.number) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("quran_screen")
    ) {
        IslamicTopBar(
            title = "القرآن الكريم",
            subtitle = "المصحف الشريف والتفاسير المعتمدة",
            trailingAction = {
                Row {
                    IconButton(
                        onClick = { showSettingsModal = true },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(IslamicDarkSurface)
                            .testTag("quran_font_settings")
                    ) {
                        Icon(
                            imageVector = Icons.Default.FormatSize,
                            contentDescription = "حجم الخط",
                            tint = GoldPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = onNavigateMemorization,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(PurpleQuran.copy(alpha = 0.2f))
                            .testTag("quran_memorize_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Quiz,
                            contentDescription = "اختبار الحفظ",
                            tint = PurpleQuranLight
                        )
                    }
                }
            }
        )

        // Tabs: قائمة السور | القراءة والتلاوة
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = IslamicDarkCard,
            contentColor = GoldPrimary,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = GoldPrimary,
                    height = 3.dp
                )
            }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Text(
                        text = "فهرس السور (١١٤)",
                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 14.sp
                    )
                }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Text(
                        text = "المصحف: سورة ${selectedSurah.nameArabic}",
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 14.sp
                    )
                }
            )
        }

        if (selectedTab == 0) {
            // --- Tab 0: Surah Index & Search ---
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("surah_search_input"),
                    placeholder = { Text("ابحث باسم السورة أو رقمها...", color = TextSecondary) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = GoldPrimary) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "مسح", tint = TextSecondary)
                            }
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = IslamicGlassBorder,
                        focusedContainerColor = IslamicDarkCard,
                        unfocusedContainerColor = IslamicDarkCard,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    contentPadding = PaddingValues(bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredSurahs) { surah ->
                        IslamicGlassCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("surah_item_${surah.number}"),
                            shapeRadius = 14.dp,
                            onClick = {
                                selectedSurah = surah
                                quranRepo.updateLastRead(surah.number, 1)
                                selectedTab = 1
                            }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(
                                                Brush.radialGradient(
                                                    colors = listOf(PurpleQuran.copy(alpha = 0.3f), IslamicDarkSurface)
                                                )
                                            )
                                            .border(1.dp, GoldPrimary.copy(alpha = 0.3f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${surah.number}",
                                            color = GoldLight,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(14.dp))

                                    Column {
                                        Text(
                                            text = "سُورَةُ ${surah.nameArabic}",
                                            color = TextPrimary,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "${surah.nameEnglish} • صـ ${surah.pageNumber}",
                                                color = TextSecondary,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    PillBadge(
                                        text = surah.revelationType,
                                        color = if (surah.revelationType == "مكية") GoldPrimary else PurpleQuran
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "${surah.totalAyahs} آية",
                                        color = TextGold,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // --- Tab 1: Reader Mode ---
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // Surah Header Bar in Reader
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(IslamicDarkSurface)
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "سُورَةُ ${selectedSurah.nameArabic}",
                                color = GoldPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${selectedSurah.revelationType} • ${selectedSurah.totalAyahs} آية • صـ ${selectedSurah.pageNumber}",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }

                        // Player controls
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { repeatCount = if (repeatCount >= 5) 1 else repeatCount + 1 },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Repeat,
                                        contentDescription = "تكرار",
                                        tint = if (repeatCount > 1) GoldPrimary else TextSecondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    if (repeatCount > 1) {
                                        Text(text = "$repeatCount", color = GoldPrimary, fontSize = 10.sp)
                                    }
                                }
                            }

                            ElevatedButton(
                                onClick = { isPlayingRecitation = !isPlayingRecitation },
                                colors = ButtonDefaults.elevatedButtonColors(
                                    containerColor = PurpleQuran,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("quran_play_button")
                            ) {
                                Icon(
                                    imageVector = if (isPlayingRecitation) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isPlayingRecitation) "إيقاف" else "تلاوة",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Ayahs list
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Basmalah (unless Surah At-Tawbah)
                    if (selectedSurah.number != 9 && selectedSurah.number != 1) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                                    color = GoldPrimary,
                                    fontSize = (fontSizeSp + 2).sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    items(currentAyahs) { ayah ->
                        val isCurrentPlaying = isPlayingRecitation && ayah.numberInSurah == currentPlayingAyahIndex
                        IslamicGlassCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("ayah_${ayah.numberInSurah}"),
                            shapeRadius = 16.dp,
                            backgroundColor = if (isCurrentPlaying) Color(0x3B8B5CF6) else IslamicDarkCard,
                            borderColor = if (isCurrentPlaying) PurpleQuran else IslamicGlassBorder.copy(alpha = 0.35f)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Ayah number star / circle
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(GoldPrimary.copy(alpha = 0.15f))
                                            .border(1.dp, GoldPrimary.copy(alpha = 0.5f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${ayah.numberInSurah}",
                                            color = GoldLight,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    }

                                    Row {
                                        IconButton(
                                            onClick = {
                                                currentTafseerAyah = ayah
                                                showTafseerModal = true
                                            },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.MenuBook,
                                                contentDescription = "التفسير",
                                                tint = PurpleQuranLight,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }

                                        IconButton(
                                            onClick = {
                                                quranRepo.updateLastRead(ayah.surahNumber, ayah.numberInSurah)
                                            },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.BookmarkBorder,
                                                contentDescription = "حفظ علامة",
                                                tint = GoldPrimary,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }

                                        IconButton(
                                            onClick = { /* Share dialog */ },
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

                                // Ayah text in Uthmani Arabic script
                                Text(
                                    text = "${ayah.text} ۝",
                                    color = TextPrimary,
                                    fontSize = fontSizeSp.sp,
                                    lineHeight = (fontSizeSp * 1.75f).sp,
                                    fontWeight = FontWeight.Medium,
                                    textAlign = TextAlign.Right,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // --- Tafseer Dialog Modal ---
    if (showTafseerModal && currentTafseerAyah != null) {
        val ayah = currentTafseerAyah!!
        val tafseerText = quranRepo.getTafseer(ayah.surahNumber, ayah.numberInSurah, selectedTafseerId)

        AlertDialog(
            onDismissRequest = { showTafseerModal = false },
            confirmButton = {
                TextButton(onClick = { showTafseerModal = false }) {
                    Text("إغلاق", color = GoldPrimary, fontWeight = FontWeight.Bold)
                }
            },
            title = {
                Column {
                    Text(
                        text = "تفسير الآية (${ayah.numberInSurah}) - سورة ${selectedSurah.nameArabic}",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(tafseerSources) { src ->
                            val isSel = src.id == selectedTafseerId
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSel) PurpleQuran else IslamicDarkSurface)
                                    .clickable { selectedTafseerId = src.id }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = src.name.replace(" (", "\n(").split("\n")[0],
                                    color = if (isSel) Color.White else TextSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            },
            text = {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(IslamicDarkSurface)
                            .padding(10.dp)
                    ) {
                        Text(
                            text = ayah.text,
                            color = GoldLight,
                            fontSize = 14.sp,
                            lineHeight = 22.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = tafseerText,
                        color = TextPrimary,
                        fontSize = 14.sp,
                        lineHeight = 24.sp
                    )
                }
            },
            containerColor = IslamicDarkCard
        )
    }

    // --- Font & Reader Settings Modal ---
    if (showSettingsModal) {
        AlertDialog(
            onDismissRequest = { showSettingsModal = false },
            confirmButton = {
                TextButton(onClick = { showSettingsModal = false }) {
                    Text("تم", color = GoldPrimary, fontWeight = FontWeight.Bold)
                }
            },
            title = {
                Text(
                    text = "إعدادات المصحف والقراءة",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "حجم الخط القرآني: ${fontSizeSp.toInt()} نقطة",
                        color = TextGold,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Slider(
                        value = fontSizeSp,
                        onValueChange = { fontSizeSp = it },
                        valueRange = 16f..36f,
                        colors = SliderDefaults.colors(
                            thumbColor = GoldPrimary,
                            activeTrackColor = GoldPrimary,
                            inactiveTrackColor = IslamicDarkSurface
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "سرعة التلاوة الصوتية: ${playbackSpeed}x",
                        color = TextGold,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf(0.75f, 1.0f, 1.25f, 1.5f).forEach { speed ->
                            val isSel = playbackSpeed == speed
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) GoldPrimary else IslamicDarkSurface)
                                    .clickable { playbackSpeed = speed }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "${speed}x",
                                    color = if (isSel) Color(0xFF1E1700) else TextSecondary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            },
            containerColor = IslamicDarkCard
        )
    }
}
