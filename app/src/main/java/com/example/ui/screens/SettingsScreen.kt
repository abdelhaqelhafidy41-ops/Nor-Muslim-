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
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.ui.components.PillBadge
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
fun SettingsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var prayerAudioEnabled by remember { mutableStateOf(true) }
    var offlineCachedMb by remember { mutableStateOf("142.5 ميجابايت") }

    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showSourcesDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("settings_screen")
    ) {
        IslamicTopBar(
            title = "الإعدادات العامة والخصوصية",
            subtitle = "تخصيص المظهر والتنبيهات وإدارة التخزين المحلي",
            onBackClick = onBackClick
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Profile Card
            item {
                IslamicGlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(GoldPrimary.copy(alpha = 0.2f))
                                .border(1.dp, GoldPrimary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = GoldPrimary)
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(text = "المستخدم المبارك", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(text = "حساب محلي آمن ومحمي بدون تتبع", color = TextSecondary, fontSize = 12.sp)
                        }
                    }
                }
            }

            // Notifications & Audio
            item {
                Text(text = "التنبيهات والصوتيات", style = MaterialTheme.typography.titleMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
            }

            item {
                IslamicGlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "تنبيهات مواقيت الصلاة والأذان", color = TextPrimary, fontSize = 14.sp)
                            Switch(
                                checked = notificationsEnabled,
                                onCheckedChange = { notificationsEnabled = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = GoldPrimary)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "صوت الأذان الكامل عند كل فريضة", color = TextPrimary, fontSize = 14.sp)
                            Switch(
                                checked = prayerAudioEnabled,
                                onCheckedChange = { prayerAudioEnabled = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = EmeraldPrimary)
                            )
                        }
                    }
                }
            }

            // Storage & Offline Manager
            item {
                Text(text = "العمل دون إنترنت وإدارة التخزين", style = MaterialTheme.typography.titleMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
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
                                Text(text = "المحتوى المحفوظ دون اتصال", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(text = "المصحف والتفاسير والأذكار والمواقيت المحفوظة", color = TextSecondary, fontSize = 11.sp)
                            }
                            PillBadge(text = offlineCachedMb, color = GoldLight)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        ElevatedButton(
                            onClick = { offlineCachedMb = "0.0 ميجابايت" },
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = IslamicDarkSurface,
                                contentColor = TextGold
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.CleaningServices, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("تفريغ الذاكرة المؤقتة (Cache)", fontSize = 12.sp)
                        }
                    }
                }
            }

            // Privacy & Sources
            item {
                Text(text = "الخصوصية والمصادر الشرعية", style = MaterialTheme.typography.titleMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
            }

            item {
                IslamicGlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showPrivacyDialog = true }
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "سياسة الخصوصية وأمان البيانات", color = TextPrimary, fontSize = 14.sp)
                            Icon(Icons.Default.Security, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(20.dp))
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showSourcesDialog = true }
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "بيان المصادر الشرعية المعتمدة", color = TextPrimary, fontSize = 14.sp)
                            Icon(Icons.Default.Info, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(20.dp))
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showDeleteConfirmDialog = true }
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "حذف سجل العبادات والبيانات المحلية", color = Color(0xFFEF4444), fontSize = 14.sp)
                            Icon(Icons.Default.DeleteOutline, contentDescription = null, tint = Color(0xFFEF4444), modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
        }
    }

    // --- Privacy Dialog ---
    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            confirmButton = {
                TextButton(onClick = { showPrivacyDialog = false }) {
                    Text("إغلاق", color = GoldPrimary, fontWeight = FontWeight.Bold)
                }
            },
            title = {
                Text("سياسة الخصوصية والأمان", color = TextPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    text = "١. تطبيق «نور المسلم» مصمم بنهج Offline-First؛ بياناتك التعبدية (الصلوات، الورد، التسبيح) تبقى محفوظة ومخزنة محلياً على جهازك.\n\n" +
                            "٢. لا يتم طلب إذن الموقع إلا لحساب مواقيت الصلاة واتجاه القبلة، ولا يتم تتبع موقعك أو تخزينه في أي خوادم خارجية.\n\n" +
                            "٣. التطبيق خالٍ تماماً من أي إعلانات منتهكة للخصوصية أو تتبع تجاري.",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                )
            },
            containerColor = IslamicDarkCard
        )
    }

    // --- Sources Dialog ---
    if (showSourcesDialog) {
        AlertDialog(
            onDismissRequest = { showSourcesDialog = false },
            confirmButton = {
                TextButton(onClick = { showSourcesDialog = false }) {
                    Text("إغلاق", color = GoldPrimary, fontWeight = FontWeight.Bold)
                }
            },
            title = {
                Text("المصادر الشرعية المعتمدة", color = TextPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    text = "• نصوص القرآن الكريم برواية حفص عن عاصم بالرسم العثماني المعتمد بمجمع الملك فهد لطباعة المصحف الشريف.\n\n" +
                            "• تفاسير القرآن: التفسير الميسر، تفسير السعدي، تفسير ابن كثير، الطبري، والقرطبي.\n\n" +
                            "• نصوص الأذكار والأدعية مأخوذة من صحيح البخاري، صحيح مسلم، وكتب السنن المعتمدة ومراجعة بأحكام الأئمة المحققين.\n\n" +
                            "• مواقيت الصلاة محسوبة وفق المعايير الفلكية الشرعية المعتمدة دولياً.",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                )
            },
            containerColor = IslamicDarkCard
        )
    }

    // --- Delete Confirm Dialog ---
    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            confirmButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("نعم، احذف البيانات", color = Color(0xFFEF4444), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("إلغاء", color = TextSecondary)
                }
            },
            title = {
                Text("تأكيد حذف البيانات", color = TextPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    text = "هل أنت متأكد من رغبتك في إعادة ضبط وحذف سجل العبادات والتسبيح؟ لا يمكن التراجع عن هذا الإجراء.",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            },
            containerColor = IslamicDarkCard
        )
    }
}
