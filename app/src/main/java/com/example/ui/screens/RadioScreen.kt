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
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HajjMoodAndAssistantRepository
import com.example.model.RadioStation
import com.example.ui.components.IslamicGlassCard
import com.example.ui.components.IslamicTopBar
import com.example.ui.components.PillBadge
import com.example.ui.theme.BluePrayer
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.IslamicDarkCard
import com.example.ui.theme.IslamicDarkSurface
import com.example.ui.theme.IslamicGlassBorder
import com.example.ui.theme.TextGold
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun RadioScreen(
    hajjMoodRepo: HajjMoodAndAssistantRepository,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activeRadio by hajjMoodRepo.activeRadio.collectAsState()
    val stations = remember { hajjMoodRepo.radioStations }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("radio_screen")
    ) {
        IslamicTopBar(
            title = "الإذاعات الإسلامية المباشرة",
            subtitle = "أكثر من ١٩٠ محطة بث للقرآن والتلاوات والدروس",
            onBackClick = onBackClick
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Live Status Hero Banner
            item {
                IslamicGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = BluePrayer.copy(alpha = 0.5f),
                    backgroundColor = Color(0x3B0D2040)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(BluePrayer.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (activeRadio != null) Icons.Default.Equalizer else Icons.Default.Radio,
                                    contentDescription = null,
                                    tint = BluePrayer,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (activeRadio != null) "جاري البث المباشر" else "اختر إذاعة للاستماع",
                                    color = if (activeRadio != null) EmeraldPrimary else TextGold,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = activeRadio?.name ?: "الإذاعات القانونية المرخصة",
                                    color = TextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        PillBadge(
                            text = if (activeRadio != null) "مباشر 🔴" else "متصل بالإنترنت",
                            color = if (activeRadio != null) Color(0xFFEF4444) else EmeraldPrimary
                        )
                    }
                }
            }

            items(stations) { station ->
                val isPlaying = activeRadio?.id == station.id
                IslamicGlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("radio_station_${station.id}"),
                    borderColor = if (isPlaying) BluePrayer else IslamicGlassBorder.copy(alpha = 0.35f),
                    backgroundColor = if (isPlaying) Color(0x3B0E2A54) else IslamicDarkCard
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            IconButton(
                                onClick = { hajjMoodRepo.toggleRadio(station) },
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(if (isPlaying) BluePrayer else IslamicDarkSurface)
                                    .testTag("radio_play_${station.id}")
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = if (isPlaying) "إيقاف" else "تشغيل",
                                    tint = if (isPlaying) Color(0xFF021727) else GoldPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = station.name,
                                    color = TextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    PillBadge(text = station.category, color = BluePrayer)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = station.country,
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }

                        Icon(
                            imageVector = Icons.Default.Wifi,
                            contentDescription = "اتصال مستقر",
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
