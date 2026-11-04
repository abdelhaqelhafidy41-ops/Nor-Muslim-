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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HajjMoodAndAssistantRepository
import com.example.model.IslamicWallpaper
import com.example.ui.components.IslamicGlassCard
import com.example.ui.components.IslamicTopBar
import com.example.ui.components.PillBadge
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.IslamicDarkCard
import com.example.ui.theme.IslamicDarkSurface
import com.example.ui.theme.IslamicGlassBorder
import com.example.ui.theme.TextGold
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.util.AppHapticManager
import com.example.util.WallpaperHelper
import kotlinx.coroutines.launch

@Composable
fun WallpapersWidgetsScreen(
    hajjMoodRepo: HajjMoodAndAssistantRepository,
    onBackClick: () -> Unit,
    hapticManager: AppHapticManager? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var selectedTab by remember { mutableIntStateOf(0) } // 0: خلفيات إسلامية | 1: الويدجت (٣٥+)
    val wallpapers = remember { hajjMoodRepo.wallpapers }
    val widgets = remember { hajjMoodRepo.widgets }

    var previewWallpaper by remember { mutableStateOf<IslamicWallpaper?>(null) }
    var isAppliedSuccess by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("wallpapers_widgets_screen")
    ) {
        IslamicTopBar(
            title = "الويدجت والخلفيات الإسلامية",
            subtitle = "أكثر من ١٥٠ خلفية فاخرة و٣٥ ويدجت ذكي",
            onBackClick = onBackClick
        )

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
                text = { Text("الخلفيات الإسلامية (١٥٠+)", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("الويدجت التفاعلي (٣٥+)", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            )
        }

        if (selectedTab == 0) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(wallpapers) { wp ->
                    Box(
                        modifier = Modifier
                            .height(180.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color(wp.primaryColorHex), Color(wp.secondaryColorHex))
                                )
                            )
                            .border(1.dp, GoldPrimary.copy(alpha = 0.4f), RoundedCornerShape(18.dp))
                            .clickable { previewWallpaper = wp }
                            .padding(12.dp),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Column {
                            PillBadge(text = wp.category, color = GoldLight)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = wp.title,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(widgets) { wid ->
                    IslamicGlassCard(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = wid.title,
                                    color = TextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = wid.description,
                                    color = TextSecondary,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp
                                )
                            }
                            PillBadge(text = "الحجم ${wid.sizeLabel}", color = GoldPrimary)
                        }
                    }
                }
            }
        }
    }

    // --- Wallpaper Preview Modal ---
    if (previewWallpaper != null) {
        val wp = previewWallpaper!!
        AlertDialog(
            onDismissRequest = {
                previewWallpaper = null
                isAppliedSuccess = false
            },
            confirmButton = {
                ElevatedButton(
                    onClick = {
                        coroutineScope.launch {
                            isAppliedSuccess = WallpaperHelper.applyWallpaper(
                                context = context,
                                title = wp.title,
                                calligraphyQuote = wp.calligraphyQuote,
                                startColorHex = wp.primaryColorHex,
                                endColorHex = wp.secondaryColorHex
                            )
                            hapticManager?.vibrateCompletion()
                        }
                    },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = GoldPrimary,
                        contentColor = Color(0xFF1E1700)
                    )
                ) {
                    Text(if (isAppliedSuccess) "تم التعيين بنجاح ✓" else "تعيين كخلفية للهاتف", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { previewWallpaper = null }) {
                    Text("إغلاق", color = TextSecondary)
                }
            },
            title = {
                Text(text = wp.title, color = TextPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(wp.primaryColorHex), Color(wp.secondaryColorHex))
                            )
                        )
                        .border(1.5.dp, GoldPrimary, RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = wp.calligraphyQuote,
                        color = GoldLight,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            },
            containerColor = IslamicDarkCard
        )
    }
}
