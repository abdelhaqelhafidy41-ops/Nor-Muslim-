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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.data.TasbihAndSalatiRepository
import com.example.model.PrayerLogStatus
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

@Composable
fun SalatiScreen(
    salatiRepo: TasbihAndSalatiRepository,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val todayLog by salatiRepo.todayLog.collectAsState()
    val rakatState by salatiRepo.rakatState.collectAsState()
    val stats = remember { salatiRepo.getStats() }

    var showRakatCounterDialog by remember { mutableStateOf(false) }

    val prayerNames = listOf("الفجر", "الظهر", "العصر", "المغرب", "العشاء")
    val prayerStatuses = listOf(
        todayLog.fajr,
        todayLog.dhuhr,
        todayLog.asr,
        todayLog.maghrib,
        todayLog.isha
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("salati_screen")
    ) {
        IslamicTopBar(
            title = "قسم «صلاتي»",
            subtitle = "سجل الصلوات الخمس والسنن وعداد الركعات",
            onBackClick = onBackClick,
            trailingAction = {
                ElevatedButton(
                    onClick = { showRakatCounterDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = GoldPrimary,
                        contentColor = Color(0xFF1E1700)
                    ),
                    modifier = Modifier.testTag("open_rakat_counter")
                ) {
                    Icon(Icons.Default.TouchApp, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("عداد الركعات", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Stats Banner
            item {
                IslamicGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = EmeraldPrimary.copy(alpha = 0.5f),
                    backgroundColor = Color(0x3B072B1E)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "مؤشر الالتزام الإيماني",
                                    color = EmeraldLight,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "نسبة أداء الصلوات: ${(stats.weeklyCommitmentRate * 100).toInt()}%",
                                    color = TextPrimary,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            PillBadge(text = "🔥 ${stats.currentStreakDays} يوم التزام", color = EmeraldPrimary)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        LinearProgressIndicator(
                            progress = { stats.weeklyCommitmentRate },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(CircleShape),
                            color = EmeraldPrimary,
                            trackColor = IslamicDarkSurface
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = stats.motivationalFeedback,
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // 5 Daily Prayers Log
            item {
                Text(
                    text = "تسجيل صلوات اليوم",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            items(5) { idx ->
                val name = prayerNames[idx]
                val currentStatus = prayerStatuses[idx]

                IslamicGlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("prayer_log_$idx")
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "صلاة $name",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            PillBadge(
                                text = currentStatus.arabicLabel,
                                color = when (currentStatus) {
                                    PrayerLogStatus.CONGREGATION -> EmeraldPrimary
                                    PrayerLogStatus.ON_TIME -> GoldPrimary
                                    PrayerLogStatus.LATE -> TextGold
                                    PrayerLogStatus.NOT_PRAYED -> Color(0xFFEF4444)
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Status Selector buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            PrayerLogStatus.values().forEach { st ->
                                val isSelected = currentStatus == st
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) GoldPrimary else IslamicDarkSurface)
                                        .clickable { salatiRepo.updatePrayerStatus(idx, st) }
                                        .padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = st.arabicLabel,
                                        color = if (isSelected) Color(0xFF1E1700) else TextSecondary,
                                        fontSize = 10.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Sunan Rawatib & Witr & Qiyam
            item {
                Text(
                    text = "السنن والرواتب والنوافل",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                IslamicGlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "السنن الرواتب اليومية",
                                    color = TextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "١٢ ركعة تُبنى بها دار في الجنة (٢ قبل الفجر، ٤ قبل الظهر، ٢ بعده، ٢ بعد المغرب، ٢ بعد العشاء)",
                                    color = TextSecondary,
                                    fontSize = 11.sp,
                                    lineHeight = 16.sp
                                )
                            }
                            Text(
                                text = "${todayLog.sunanCompleted} / 12",
                                color = EmeraldPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            listOf(0, 4, 8, 10, 12).forEach { num ->
                                val isSel = todayLog.sunanCompleted == num
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSel) EmeraldPrimary else IslamicDarkSurface)
                                        .clickable { salatiRepo.setSunanCount(num) }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "$num ركعات",
                                        color = if (isSel) Color(0xFF002213) else TextSecondary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Witr & Qiyam Switches
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "صلاة الشفع والوتر",
                                color = TextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Switch(
                                checked = todayLog.witrPrayed,
                                onCheckedChange = { salatiRepo.toggleWitr() },
                                colors = SwitchDefaults.colors(checkedThumbColor = GoldPrimary)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "قيام الليل والتهجد",
                                color = TextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Switch(
                                checked = todayLog.qiyamPrayed,
                                onCheckedChange = { salatiRepo.toggleQiyam() },
                                colors = SwitchDefaults.colors(checkedThumbColor = EmeraldPrimary)
                            )
                        }
                    }
                }
            }
        }
    }

    // --- Interactive Rakat & Sujood Counter Dialog ---
    if (showRakatCounterDialog) {
        AlertDialog(
            onDismissRequest = { showRakatCounterDialog = false },
            confirmButton = {
                TextButton(onClick = { showRakatCounterDialog = false }) {
                    Text("إغلاق", color = GoldPrimary, fontWeight = FontWeight.Bold)
                }
            },
            title = {
                Text(
                    text = "عداد الركعات والسجدات (دفع الشك والسهو)",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = rakatState.targetPrayer,
                        color = GoldLight,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    if (rakatState.isCompleted) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(EmeraldPrimary.copy(alpha = 0.2f))
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(36.dp))
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "اكتملت الصلاة بحمد الله وتوفيقه",
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    } else {
                        // Current Rakat & Sujood display
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "الركعة الحالية", color = TextSecondary, fontSize = 12.sp)
                                Text(
                                    text = "${rakatState.currentRakat} / ${rakatState.totalRakats}",
                                    color = GoldPrimary,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "السجدة", color = TextSecondary, fontSize = 12.sp)
                                Text(
                                    text = "${rakatState.currentSujood} / 2",
                                    color = EmeraldPrimary,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Tap button to advance
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .clip(CircleShape)
                                .background(GoldPrimary)
                                .clickable { salatiRepo.nextSujoodOrRakat() }
                                .testTag("advance_rakat_sujood_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.TouchApp,
                                    contentDescription = null,
                                    tint = Color(0xFF1E1700),
                                    modifier = Modifier.size(28.dp)
                                )
                                Text(
                                    text = "سجدت الآن",
                                    color = Color(0xFF1E1700),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(onClick = { salatiRepo.resetRakatCounter() }) {
                        Icon(Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("إعادة البدء من الركعة الأولى", color = TextSecondary, fontSize = 12.sp)
                    }
                }
            },
            containerColor = IslamicDarkCard
        )
    }
}
