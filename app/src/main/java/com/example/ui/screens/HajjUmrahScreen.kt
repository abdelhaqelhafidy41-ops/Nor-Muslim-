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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.HajjMoodAndAssistantRepository
import com.example.model.LapRitualType
import com.example.ui.components.IslamicGlassCard
import com.example.ui.components.IslamicTopBar
import com.example.ui.components.PillBadge
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.IslamicDarkCard
import com.example.ui.theme.IslamicDarkSurface
import com.example.ui.theme.IslamicGlassBorder
import com.example.ui.theme.TextGold
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

import com.example.util.AppAudioManager
import com.example.util.AppHapticManager

@Composable
fun HajjUmrahScreen(
    hajjRepo: HajjMoodAndAssistantRepository,
    onBackClick: () -> Unit,
    audioManager: AppAudioManager? = null,
    hapticManager: AppHapticManager? = null,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: عداد الطواف والسعي | 1: خطوات العمرة | 2: خطوات الحج
    val lapState by hajjRepo.lapState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("hajj_umrah_screen")
    ) {
        IslamicTopBar(
            title = "دليل ومناسك الحج والعمرة",
            subtitle = "خطوات المناسك وأشواط الطواف والسعي التفاعلية",
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
                text = { Text("عداد الأشواط (٧)", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("مناسك العمرة", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("مناسك الحج", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            )
        }

        if (selectedTab == 0) {
            // --- Interactive Lap Counter (Tawaf / Sa'i) ---
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Switch between Tawaf & Sa'i
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ElevatedButton(
                        onClick = { hajjRepo.startRitual(LapRitualType.TAWAF) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = if (lapState.type == LapRitualType.TAWAF) GoldPrimary else IslamicDarkSurface,
                            contentColor = if (lapState.type == LapRitualType.TAWAF) Color(0xFF1E1700) else TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("طواف الكعبة (٧)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }

                    ElevatedButton(
                        onClick = { hajjRepo.startRitual(LapRitualType.SAI) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = if (lapState.type == LapRitualType.SAI) GoldPrimary else IslamicDarkSurface,
                            contentColor = if (lapState.type == LapRitualType.SAI) Color(0xFF1E1700) else TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("السعي (صفا ومروة)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Big Counter Box
                IslamicGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = GoldPrimary.copy(alpha = 0.5f),
                    backgroundColor = Color(0x3B15254A)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = lapState.type.arabicName,
                            color = TextGold,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        if (lapState.isFinished) {
                            Text(
                                text = "اكتملت ٧ أشواط بحمد الله تقبل الله منكم",
                                color = EmeraldLight,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        } else {
                            Text(
                                text = "الشوط ${lapState.currentLap} من ٧",
                                color = TextPrimary,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        LinearProgressIndicator(
                            progress = { (lapState.currentLap.toFloat() / 7f).coerceIn(0f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(CircleShape),
                            color = GoldPrimary,
                            trackColor = IslamicDarkSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Active Lap Dua Card
                IslamicGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = IslamicGlassBorder
                ) {
                    Column {
                        Text(
                            text = "الدعاء والذكر الموصى به في هذا الشوط:",
                            color = TextGold,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = lapState.activeDua,
                            color = TextPrimary,
                            fontSize = 15.sp,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Actions: Next Lap & Undo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            hajjRepo.undoLap()
                            hapticManager?.vibrateTap()
                        },
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(IslamicDarkSurface)
                            .border(1.dp, IslamicGlassBorder, CircleShape)
                            .testTag("undo_lap_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Undo,
                            contentDescription = "تراجع شوط",
                            tint = TextSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    ElevatedButton(
                        onClick = {
                            val nextWillFinish = lapState.currentLap >= 6
                            hajjRepo.nextLap()
                            if (nextWillFinish) {
                                hapticManager?.vibrateCompletion()
                                audioManager?.playBeepTone()
                            } else {
                                hapticManager?.vibrateTap()
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(54.dp)
                            .testTag("next_lap_button"),
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = if (lapState.isFinished) EmeraldPrimary else GoldPrimary,
                            contentColor = Color(0xFF1E1700)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(
                            imageVector = if (lapState.isFinished) Icons.Default.Check else Icons.Default.TouchApp,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (lapState.isFinished) "تم إتمام المناسك بنجاح" else "تسجيل إتمام الشوط الحالي",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        } else if (selectedTab == 1) {
            // --- Tab 1: Umrah Steps ---
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(hajjRepo.umrahSteps) { step ->
                    IslamicGlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(GoldPrimary.copy(alpha = 0.2f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "${step.stepOrder}", color = GoldLight, fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = step.title,
                                        color = TextPrimary,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                PillBadge(text = step.fiqhRule, color = GoldPrimary)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = step.description,
                                color = TextSecondary,
                                fontSize = 13.sp,
                                lineHeight = 20.sp
                            )

                            if (step.supplication.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(IslamicDarkSurface)
                                        .padding(10.dp)
                                ) {
                                    Text(
                                        text = "🤲 ${step.supplication}",
                                        color = GoldLight,
                                        fontSize = 12.sp,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // --- Tab 2: Hajj Steps ---
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(hajjRepo.hajjSteps) { step ->
                    IslamicGlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(EmeraldPrimary.copy(alpha = 0.2f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "${step.stepOrder}", color = EmeraldLight, fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = step.title,
                                        color = TextPrimary,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                PillBadge(text = step.fiqhRule, color = EmeraldPrimary)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = step.description,
                                color = TextSecondary,
                                fontSize = 13.sp,
                                lineHeight = 20.sp
                            )

                            if (step.supplication.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(IslamicDarkSurface)
                                        .padding(10.dp)
                                ) {
                                    Text(
                                        text = "🤲 ${step.supplication}",
                                        color = TextGold,
                                        fontSize = 12.sp,
                                        lineHeight = 18.sp
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
