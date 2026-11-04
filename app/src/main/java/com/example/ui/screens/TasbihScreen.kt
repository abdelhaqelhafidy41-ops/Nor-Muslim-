package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TasbihAndSalatiRepository
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
fun TasbihScreen(
    salatiRepo: TasbihAndSalatiRepository,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedDhikr by salatiRepo.selectedDhikr.collectAsState()
    val vibrationEnabled by salatiRepo.vibrationEnabled.collectAsState()
    val soundEnabled by salatiRepo.soundEnabled.collectAsState()
    val history by salatiRepo.dailyHistory.collectAsState()

    var isFullScreen by remember { mutableStateOf(false) }
    var showHistoryDialog by remember { mutableStateOf(false) }
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1.0f,
        animationSpec = spring(),
        label = "tasbih_scale"
    )

    val progress = (selectedDhikr.currentCount.toFloat() / selectedDhikr.defaultTarget).coerceIn(0f, 1f)

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("tasbih_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!isFullScreen) {
            IslamicTopBar(
                title = "المسبحة الإلكترونية المتطورة",
                subtitle = "الذكر اليومي وأهداف التسبيح",
                onBackClick = onBackClick,
                trailingAction = {
                    Row {
                        IconButton(
                            onClick = { salatiRepo.toggleVibration() },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(if (vibrationEnabled) GoldPrimary.copy(alpha = 0.2f) else IslamicDarkSurface)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Vibration,
                                contentDescription = "الاهتزاز",
                                tint = if (vibrationEnabled) GoldPrimary else TextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        IconButton(
                            onClick = { showHistoryDialog = true },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(IslamicDarkSurface)
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = "سجل التسبيح",
                                tint = TextGold,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        IconButton(
                            onClick = { isFullScreen = true },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(IslamicDarkSurface)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Fullscreen,
                                contentDescription = "وضع الشاشة الكاملة",
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            )

            // Select Dhikr Row
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(salatiRepo.defaultDhikrs) { d ->
                    val isSel = d.id == selectedDhikr.id
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isSel) EmeraldPrimary else IslamicDarkCard)
                            .border(1.dp, if (isSel) EmeraldLight else IslamicGlassBorder, RoundedCornerShape(14.dp))
                            .clickable { salatiRepo.selectDhikr(d) }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                            .testTag("tasbih_select_${d.id}")
                    ) {
                        Text(
                            text = d.arabicPhrase,
                            color = if (isSel) Color(0xFF002213) else TextPrimary,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        } else {
            // Full screen top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedDhikr.arabicPhrase,
                    color = GoldLight,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = { isFullScreen = false },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(IslamicDarkSurface)
                ) {
                    Icon(
                        imageVector = Icons.Default.FullscreenExit,
                        contentDescription = "الخروج من الشاشة الكاملة",
                        tint = GoldPrimary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(0.5f))

        // Large Dhikr Phrase display
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            Text(
                text = selectedDhikr.arabicPhrase,
                color = TextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = selectedDhikr.translationOrFadl,
                color = TextSecondary,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }

        Spacer(modifier = Modifier.weight(0.8f))

        // Big Circular Tasbih Counter Button
        Box(
            modifier = Modifier
                .size(240.dp)
                .scale(scale)
                .testTag("tasbih_main_button"),
            contentAlignment = Alignment.Center
        ) {
            // Progress Ring
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.size(240.dp),
                color = EmeraldPrimary,
                trackColor = IslamicDarkSurface,
                strokeWidth = 10.dp
            )

            // Inner Clickable Bead Sphere
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF1B3B2B),
                                Color(0xFF0A1F16),
                                Color(0xFF040D09)
                            )
                        )
                    )
                    .border(2.dp, EmeraldPrimary.copy(alpha = 0.5f), CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            isPressed = true
                            salatiRepo.incrementTasbih()
                            isPressed = false
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${selectedDhikr.currentCount}",
                        color = GoldLight,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "الهدف: ${selectedDhikr.defaultTarget}",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(0.8f))

        // Bottom Controls: Reset & Stats
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "مجموع التسبيحات الكلي",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
                Text(
                    text = "${selectedDhikr.totalCountLifetime} تسبيحة",
                    color = TextGold,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            IconButton(
                onClick = { salatiRepo.resetTasbih() },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(IslamicDarkSurface)
                    .border(1.dp, IslamicGlassBorder, CircleShape)
                    .testTag("tasbih_reset_button")
            ) {
                Icon(
                    imageVector = Icons.Default.RestartAlt,
                    contentDescription = "إعادة ضبط العداد",
                    tint = GoldPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    // --- History Dialog ---
    if (showHistoryDialog) {
        AlertDialog(
            onDismissRequest = { showHistoryDialog = false },
            confirmButton = {
                TextButton(onClick = { showHistoryDialog = false }) {
                    Text("إغلاق", color = GoldPrimary, fontWeight = FontWeight.Bold)
                }
            },
            title = {
                Text("سجل جلسات التسبيح", color = TextPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(history) { item ->
                        IslamicGlassCard(modifier = Modifier.fillMaxWidth(), shapeRadius = 10.dp) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = item.dhikrTitle, color = TextPrimary, fontWeight = FontWeight.Bold)
                                    Text(text = item.dateFormatted, color = TextSecondary, fontSize = 11.sp)
                                }
                                Text(
                                    text = "${item.count} مرة",
                                    color = EmeraldPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
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
