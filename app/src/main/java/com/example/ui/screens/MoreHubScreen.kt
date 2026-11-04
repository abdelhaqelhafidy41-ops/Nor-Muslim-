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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.IslamicGlassCard
import com.example.ui.components.IslamicTopBar
import com.example.ui.theme.BluePrayer
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.IslamicDarkCard
import com.example.ui.theme.IslamicGlassBorder
import com.example.ui.theme.PurpleQuran
import com.example.ui.theme.TextGold
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

data class HubFeature(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val accentColor: Color,
    val route: String
)

@Composable
fun MoreHubScreen(
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val features = listOf(
        HubFeature(
            id = "mood",
            title = "«قرآن مود» والحالات اليومية",
            subtitle = "١٤ حالة نفسية وروحية مع آيات السكينة والأدعية المواسية",
            icon = Icons.Default.Mood,
            accentColor = PurpleQuran,
            route = "quran_mood"
        ),
        HubFeature(
            id = "hajj",
            title = "دليل مناسك الحج والعمرة",
            subtitle = "دليل تفاعلي خطوة بخطوة مع عداد أشواط الطواف والسعي",
            icon = Icons.Default.FlightTakeoff,
            accentColor = GoldPrimary,
            route = "hajj"
        ),
        HubFeature(
            id = "radio",
            title = "الإذاعات الإسلامية المباشرة",
            subtitle = "أكثر من ١٩٠ إذاعة للقرآن الكريم والدروس والتلاوات النادرة",
            icon = Icons.Default.Radio,
            accentColor = BluePrayer,
            route = "radio"
        ),
        HubFeature(
            id = "stories",
            title = "السيرة وقصص ٤٧ صحابياً والأنبياء",
            subtitle = "مكتبة معرفية ثرية بالدروس والعبر الموثقة من كتب التاريخ والسير",
            icon = Icons.Default.MenuBook,
            accentColor = EmeraldPrimary,
            route = "stories"
        ),
        HubFeature(
            id = "plans",
            title = "الخطط والبرامج التعبدية (٥٠+ خطة)",
            subtitle = "خطط رمضان، ختم القرآن، قيام الليل، وصيام النوافل مع المتابعة",
            icon = Icons.Default.EventNote,
            accentColor = Color(0xFFF59E0B),
            route = "plans"
        ),
        HubFeature(
            id = "assistant",
            title = "المساعد الإسلامي الذكي",
            subtitle = "إجابات موثقة بالأدلة من الكتاب والسنة وأقوال أئمة الفقه",
            icon = Icons.Default.Psychology,
            accentColor = Color(0xFFA78BFA),
            route = "assistant"
        ),
        HubFeature(
            id = "wallpapers",
            title = "الويدجت والخلفيات الإسلامية",
            subtitle = "أكثر من ١٥٠ خلفية فاخرة و٣٥ ويدجت لشاشتك الرئيسية",
            icon = Icons.Default.Wallpaper,
            accentColor = Color(0xFF38BDF8),
            route = "wallpapers"
        ),
        HubFeature(
            id = "settings",
            title = "الإعدادات العامة وإدارة التخزين",
            subtitle = "المظهر، الصوت، حساب المواقيت، والعمل دون اتصال بالإنترنت",
            icon = Icons.Default.Settings,
            accentColor = Color(0xFF94A3B8),
            route = "settings"
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("more_hub_screen")
    ) {
        IslamicTopBar(
            title = "المزيد من الأقسام الإيمانية",
            subtitle = "جميع خدمات وخصائص تطبيق نور المسلم"
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(features) { feat ->
                IslamicGlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("hub_feature_${feat.id}"),
                    borderColor = feat.accentColor.copy(alpha = 0.35f),
                    onClick = { onNavigate(feat.route) }
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
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(feat.accentColor.copy(alpha = 0.18f))
                                    .border(1.dp, feat.accentColor.copy(alpha = 0.4f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = feat.icon,
                                    contentDescription = feat.title,
                                    tint = feat.accentColor,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {
                                Text(
                                    text = feat.title,
                                    color = TextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = feat.subtitle,
                                    color = TextSecondary,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "الانتقال",
                            tint = feat.accentColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
