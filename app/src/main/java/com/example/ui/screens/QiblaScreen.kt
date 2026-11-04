package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PrayerRepository
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
import kotlin.math.abs

@Composable
fun QiblaScreen(
    prayerRepo: PrayerRepository,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentCity by prayerRepo.currentCity.collectAsState()
    val qiblaData = remember(currentCity) { prayerRepo.calculateQibla() }

    // Simulated device heading rotation (can be adjusted via slider or live sensors)
    var deviceHeading by remember { mutableFloatStateOf(0f) }
    var showCalibrationDialog by remember { mutableStateOf(false) }

    val angleDifference = (qiblaData.azimuthDegrees - deviceHeading + 360) % 360
    val isFacingKaaba = abs(angleDifference) < 5 || abs(angleDifference - 360) < 5

    val animatedRotation by animateFloatAsState(
        targetValue = -deviceHeading,
        animationSpec = tween(300),
        label = "compass_rotation"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("qibla_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IslamicTopBar(
            title = "بوصلة القبلة المشرفة",
            subtitle = "الاتجاه نحو الكعبة المشرفة في مكة المكرمة",
            onBackClick = onBackClick,
            trailingAction = {
                IconButton(
                    onClick = { showCalibrationDialog = true },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(IslamicDarkSurface)
                ) {
                    Icon(
                        imageVector = Icons.Default.CompassCalibration,
                        contentDescription = "معايرة البوصلة",
                        tint = GoldPrimary
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // City & Distance Banner
        IslamicGlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            borderColor = BluePrayer.copy(alpha = 0.4f),
            backgroundColor = Color(0x3B0D2342)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(BluePrayer.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = null,
                            tint = BluePrayerLight,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = currentCity.nameArabic,
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "زاوية القبلة: ${qiblaData.azimuthDegrees.toInt()}° شمالاً",
                            color = TextGold,
                            fontSize = 12.sp
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "المسافة إلى الكعبة",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                    Text(
                        text = "${qiblaData.distanceToKaabaKm} كم",
                        color = GoldLight,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(0.5f))

        // Status indicator
        if (isFacingKaaba) {
            PillBadge(
                text = "أنت الآن تواجه القبلة المشرفة بدقة ✓",
                color = EmeraldPrimary,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        } else {
            PillBadge(
                text = "قم بتدوير الهاتف حتى يتطابق المؤشر الذهبي مع الكعبة",
                color = GoldPrimary,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        // Compass Visual Dial
        Box(
            modifier = Modifier
                .size(280.dp)
                .testTag("compass_dial"),
            contentAlignment = Alignment.Center
        ) {
            // Outer Dial Circle
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2, size.height / 2)
                val radius = size.minDimension / 2 - 12.dp.toPx()

                // Background gradient
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFF132244), Color(0xFF091223))
                    ),
                    radius = radius,
                    center = center
                )

                // Outer decorative border
                drawCircle(
                    color = Color(0x44D4AF37),
                    radius = radius,
                    center = center,
                    style = Stroke(width = 3.dp.toPx())
                )
            }

            // Rotating Needle & Kaaba Pointer
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .rotate(animatedRotation),
                contentAlignment = Alignment.Center
            ) {
                // Kaaba marker at azimuth angle
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .rotate(qiblaData.azimuthDegrees),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF1E1700))
                                .border(2.dp, GoldPrimary, RoundedCornerShape(6.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "🕋",
                                fontSize = 18.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "الكعبة",
                            color = GoldLight,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Compass Needle Pointer
                Icon(
                    imageVector = Icons.Default.Navigation,
                    contentDescription = "اتجاه الشمال",
                    tint = if (isFacingKaaba) EmeraldPrimary else GoldPrimary,
                    modifier = Modifier
                        .size(80.dp)
                        .rotate(qiblaData.azimuthDegrees)
                )
            }
        }

        Spacer(modifier = Modifier.weight(0.5f))

        // Manual Heading Calibration Slider (for testing and offline sensor fallback)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "محاكاة اتجاه البوصلة (درجة): ${deviceHeading.toInt()}°",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
                Text(
                    text = if (isFacingKaaba) "متطابق ✓" else "غيّر الاتجاه",
                    color = if (isFacingKaaba) EmeraldPrimary else TextGold,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Slider(
                value = deviceHeading,
                onValueChange = { deviceHeading = it },
                valueRange = 0f..360f,
                colors = SliderDefaults.colors(
                    thumbColor = GoldPrimary,
                    activeTrackColor = BluePrayer,
                    inactiveTrackColor = IslamicDarkSurface
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }

    // --- Calibration Dialog ---
    if (showCalibrationDialog) {
        AlertDialog(
            onDismissRequest = { showCalibrationDialog = false },
            confirmButton = {
                TextButton(onClick = { showCalibrationDialog = false }) {
                    Text("فهمت ذلك", color = GoldPrimary, fontWeight = FontWeight.Bold)
                }
            },
            title = {
                Text("إرشادات معايرة بوصلة القبلة", color = TextPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    Text(
                        text = "للحصول على أعلى دقة في تحديد اتجاه الكعبة المشرفة:\n\n" +
                                "١. حرّك الهاتف في الهواء على شكل الرقم ٨ بالإنجليزية (∞) عدة مرات لإعادة ضبط مستشعر المغناطيسية.\n" +
                                "٢. ابتعد عن الأجهزة الإلكترونية والأجسام المعدنية القريبة لتجنب التشويش المغناطيسي.\n" +
                                "٣. ضع الهاتف في وضع أفقي مستوٍ تماماً على راحة يدك.",
                        color = TextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 22.sp
                    )
                }
            },
            containerColor = IslamicDarkCard
        )
    }
}
