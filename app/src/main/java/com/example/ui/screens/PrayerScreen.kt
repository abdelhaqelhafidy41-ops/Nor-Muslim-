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
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CityLocation
import com.example.data.PrayerRepository
import com.example.model.CalculationMethod
import com.example.model.FajrChallengeType
import com.example.ui.components.IslamicGlassCard
import com.example.ui.components.IslamicTopBar
import com.example.ui.components.PillBadge
import com.example.ui.theme.BluePrayer
import com.example.ui.theme.BluePrayerLight
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
fun PrayerScreen(
    prayerRepo: PrayerRepository,
    onNavigateSalati: () -> Unit,
    onNavigateQibla: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentCity by prayerRepo.currentCity.collectAsState()
    val currentMethod by prayerRepo.calculationMethod.collectAsState()
    val fajrAlarm by prayerRepo.fajrAlarm.collectAsState()
    val prayerData = prayerRepo.getTodayPrayerTimes()

    var showCityDialog by remember { mutableStateOf(false) }
    var showMethodDialog by remember { mutableStateOf(false) }
    var showAzanVoiceDialog by remember { mutableStateOf(false) }
    var showFajrAlarmDialog by remember { mutableStateOf(false) }

    val hoursLeft = prayerData.remainingTimeSeconds / 3600
    val minutesLeft = (prayerData.remainingTimeSeconds % 3600) / 60
    val secondsLeft = prayerData.remainingTimeSeconds % 60
    val countdownFormatted = String.format("%02d:%02d:%02d", hoursLeft, minutesLeft, secondsLeft)

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("prayer_screen")
    ) {
        IslamicTopBar(
            title = "مواقيت الصلاة والأذان",
            subtitle = "${currentCity.nameArabic} • ${prayerData.hijriDate}",
            trailingAction = {
                Row {
                    IconButton(
                        onClick = { showCityDialog = true },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(IslamicDarkSurface)
                            .testTag("change_city_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationCity,
                            contentDescription = "تغيير المدينة",
                            tint = GoldPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = { showMethodDialog = true },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(IslamicDarkSurface)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "طريقة الحساب",
                            tint = BluePrayerLight
                        )
                    }
                }
            }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Next Prayer Banner
            item {
                IslamicGlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("prayer_banner"),
                    borderColor = BluePrayer.copy(alpha = 0.5f),
                    backgroundColor = Color(0x3B0C1E3D)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "الصلاة القادمة بإذن الله",
                                    color = TextGold,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "صلاة ${prayerData.nextPrayer.arabicName}",
                                    color = TextPrimary,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "الوقت المتبقي",
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = countdownFormatted,
                                    color = GoldPrimary,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ElevatedButton(
                                onClick = onNavigateQibla,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("qibla_shortcut"),
                                colors = ButtonDefaults.elevatedButtonColors(
                                    containerColor = BluePrayer,
                                    contentColor = Color(0xFF021727)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("بوصلة القبلة", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }

                            ElevatedButton(
                                onClick = onNavigateSalati,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("salati_shortcut"),
                                colors = ButtonDefaults.elevatedButtonColors(
                                    containerColor = EmeraldPrimary,
                                    contentColor = Color(0xFF002214)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("قسم «صلاتي»", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // Prayers list
            item {
                Text(
                    text = "صلوات اليوم",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            items(prayerData.times) { pItem ->
                val isNext = pItem.isNext
                IslamicGlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("prayer_item_${pItem.prayer.name}"),
                    borderColor = if (isNext) GoldPrimary else IslamicGlassBorder.copy(alpha = 0.35f),
                    backgroundColor = if (isNext) Color(0x3B2E2405) else IslamicDarkCard
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(if (isNext) GoldPrimary.copy(alpha = 0.2f) else IslamicDarkSurface),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.NotificationsActive,
                                    contentDescription = null,
                                    tint = if (isNext) GoldPrimary else TextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = pItem.prayer.arabicName,
                                        color = if (isNext) GoldLight else TextPrimary,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (isNext) {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        PillBadge(text = "القادمة", color = GoldPrimary)
                                    }
                                }
                                Text(
                                    text = if (pItem.prayer.isObligatory) "فريضة مكتوبة" else "سنة مستحبة",
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Text(
                            text = pItem.timeFormatted,
                            color = if (isNext) GoldPrimary else TextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Fajr Alarm & Azan Sound Card
            item {
                Spacer(modifier = Modifier.height(10.dp))
                IslamicGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = GoldPrimary.copy(alpha = 0.4f),
                    backgroundColor = Color(0x3B15254A)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Alarm,
                                    contentDescription = null,
                                    tint = GoldPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "منبه الفجر الذكي",
                                        color = TextPrimary,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "تحديات استيقاظ (تسبيح / لغز) لضمان عدم النوم",
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Switch(
                                checked = fajrAlarm.isEnabled,
                                onCheckedChange = {
                                    prayerRepo.updateFajrAlarm(fajrAlarm.copy(isEnabled = it))
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = GoldPrimary,
                                    checkedTrackColor = GoldDark
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ElevatedButton(
                                onClick = { showAzanVoiceDialog = true },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.elevatedButtonColors(
                                    containerColor = IslamicDarkSurface,
                                    contentColor = GoldPrimary
                                ),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.VolumeUp, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("أصوات الأذان (١٤)", fontSize = 11.sp)
                            }

                            ElevatedButton(
                                onClick = { showFajrAlarmDialog = true },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.elevatedButtonColors(
                                    containerColor = IslamicDarkSurface,
                                    contentColor = BluePrayerLight
                                ),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.TouchApp, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("تحدي الاستيقاظ", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }

    // --- City Selection Dialog ---
    if (showCityDialog) {
        AlertDialog(
            onDismissRequest = { showCityDialog = false },
            confirmButton = {
                TextButton(onClick = { showCityDialog = false }) {
                    Text("إغلاق", color = GoldPrimary)
                }
            },
            title = {
                Text("اختر مدينتك لمواقيت دقيقة", color = TextPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(prayerRepo.availableCities) { city ->
                        val isSel = city.nameArabic == currentCity.nameArabic
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSel) GoldPrimary.copy(alpha = 0.2f) else IslamicDarkSurface)
                                .clickable {
                                    prayerRepo.setCity(city)
                                    showCityDialog = false
                                }
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${city.nameArabic} - ${city.countryArabic}",
                                    color = if (isSel) GoldLight else TextPrimary,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 13.sp
                                )
                                if (isSel) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            },
            containerColor = IslamicDarkCard
        )
    }

    // --- Calculation Method Dialog ---
    if (showMethodDialog) {
        AlertDialog(
            onDismissRequest = { showMethodDialog = false },
            confirmButton = {
                TextButton(onClick = { showMethodDialog = false }) {
                    Text("إغلاق", color = GoldPrimary)
                }
            },
            title = {
                Text("طريقة حساب مواقيت الصلاة", color = TextPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    CalculationMethod.values().forEach { method ->
                        val isSel = method == currentMethod
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSel) BluePrayer.copy(alpha = 0.2f) else IslamicDarkSurface)
                                .clickable {
                                    prayerRepo.setCalculationMethod(method)
                                    showMethodDialog = false
                                }
                                .padding(12.dp)
                        ) {
                            Text(
                                text = method.titleArabic,
                                color = if (isSel) BluePrayerLight else TextPrimary,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            },
            containerColor = IslamicDarkCard
        )
    }

    // --- 14 Azan Voices Dialog ---
    if (showAzanVoiceDialog) {
        AlertDialog(
            onDismissRequest = { showAzanVoiceDialog = false },
            confirmButton = {
                TextButton(onClick = { showAzanVoiceDialog = false }) {
                    Text("إغلاق", color = GoldPrimary)
                }
            },
            title = {
                Text("أصوات الأذان المرخصة (١٤ صوتاً)", color = TextPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(prayerRepo.azanVoices) { voice ->
                        val isCurrent = voice.id == fajrAlarm.soundId
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isCurrent) GoldPrimary.copy(alpha = 0.2f) else IslamicDarkSurface)
                                .clickable {
                                    prayerRepo.updateFajrAlarm(fajrAlarm.copy(soundId = voice.id))
                                }
                                .padding(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = voice.title,
                                        color = if (isCurrent) GoldLight else TextPrimary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = voice.reciterOrPlace,
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                }
                                Icon(Icons.Default.PlayArrow, contentDescription = "استماع", tint = GoldPrimary, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
            },
            containerColor = IslamicDarkCard
        )
    }

    // --- Fajr Challenge Settings Dialog ---
    if (showFajrAlarmDialog) {
        AlertDialog(
            onDismissRequest = { showFajrAlarmDialog = false },
            confirmButton = {
                TextButton(onClick = { showFajrAlarmDialog = false }) {
                    Text("حفظ", color = GoldPrimary, fontWeight = FontWeight.Bold)
                }
            },
            title = {
                Text("تحدي إيقاف منبه الفجر", color = TextPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    FajrChallengeType.values().forEach { ch ->
                        val isSel = ch == fajrAlarm.challengeType
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSel) EmeraldPrimary.copy(alpha = 0.2f) else IslamicDarkSurface)
                                .clickable {
                                    prayerRepo.updateFajrAlarm(fajrAlarm.copy(challengeType = ch))
                                }
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = ch.titleArabic,
                                    color = if (isSel) EmeraldPrimary else TextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = ch.description,
                                    color = TextSecondary,
                                    fontSize = 11.sp
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
