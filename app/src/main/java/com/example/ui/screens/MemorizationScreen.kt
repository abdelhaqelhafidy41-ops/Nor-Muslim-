package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.example.data.QuranRepository
import com.example.model.MemorizationMethod
import com.example.model.MemorizationPlan
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
import com.example.ui.theme.PurpleQuran
import com.example.ui.theme.PurpleQuranLight
import com.example.ui.theme.TextGold
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

import androidx.compose.ui.platform.LocalContext
import com.example.util.AppAudioManager
import com.example.util.AppHapticManager
import com.example.util.ShareHelper

@Composable
fun MemorizationScreen(
    quranRepo: QuranRepository,
    onBackClick: () -> Unit,
    audioManager: AppAudioManager? = null,
    hapticManager: AppHapticManager? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val plans by quranRepo.memorizationPlans.collectAsState()
    var showNewPlanDialog by remember { mutableStateOf(false) }
    var showQuizDialog by remember { mutableStateOf(false) }
    var selectedCertificatePlan by remember { mutableStateOf<MemorizationPlan?>(null) }

    // Quiz State
    val quizQuestions = remember { quranRepo.generateQuizQuestions(67) }
    var currentQuestionIdx by remember { mutableIntStateOf(0) }
    var quizScore by remember { mutableIntStateOf(0) }
    var selectedAnswerIdx by remember { mutableStateOf<Int?>(null) }
    var isAnswerSubmitted by remember { mutableStateOf(false) }

    // New Plan form state
    var newPlanTitle by remember { mutableStateOf("حفظ سورة يس") }
    var newPlanSurahName by remember { mutableStateOf("يس") }
    var newPlanDays by remember { mutableStateOf("20") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("memorization_screen")
    ) {
        IslamicTopBar(
            title = "نظام حفظ القرآن الكريم",
            subtitle = "خطط مخصصة، اختبارات ذكية، وشهادات إتمام",
            onBackClick = onBackClick,
            trailingAction = {
                IconButton(
                    onClick = { showNewPlanDialog = true },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(GoldPrimary)
                        .testTag("new_plan_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "إنشاء خطة",
                        tint = Color(0xFF1E1700)
                    )
                }
            }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 4 Methods overview banner
            item {
                IslamicGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = PurpleQuran.copy(alpha = 0.4f),
                    backgroundColor = Color(0x3B1F133F)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "طرق الحفظ الأربع المعتمدة",
                                color = GoldLight,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        MemorizationMethod.values().forEach { method ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(PurpleQuranLight)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = method.title,
                                        color = TextPrimary,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = method.desc,
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        ElevatedButton(
                            onClick = {
                                currentQuestionIdx = 0
                                quizScore = 0
                                selectedAnswerIdx = null
                                isAnswerSubmitted = false
                                showQuizDialog = true
                            },
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = GoldPrimary,
                                contentColor = Color(0xFF1E1700)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("start_quiz_button")
                        ) {
                            Icon(Icons.Default.Quiz, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("بدء اختبار الحفظ واسترجاع الآيات", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }

            // Plans list
            item {
                Text(
                    text = "خطط الحفظ الشخصية",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            items(plans) { plan ->
                val progress = plan.masteredAyahs.toFloat() / (plan.endAyah - plan.startAyah + 1).coerceAtLeast(1)
                IslamicGlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("memorization_plan_${plan.id}"),
                    borderColor = if (plan.isCompleted) EmeraldPrimary.copy(alpha = 0.5f) else IslamicGlassBorder
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = plan.title,
                                    color = TextPrimary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "طريقة الحفظ: ${plan.method.title}",
                                    color = TextGold,
                                    fontSize = 12.sp
                                )
                            }

                            if (plan.isCompleted) {
                                PillBadge(text = "مكتملة ✓", color = EmeraldPrimary)
                            } else {
                                PillBadge(text = "اليوم ${plan.currentDay} من ${plan.targetDays}", color = GoldPrimary)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "الآيات المحفوظة: ${plan.masteredAyahs} من ${plan.endAyah - plan.startAyah + 1}",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "${(progress * 100).toInt()}%",
                                color = if (plan.isCompleted) EmeraldPrimary else GoldPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(CircleShape),
                            color = if (plan.isCompleted) EmeraldPrimary else GoldPrimary,
                            trackColor = IslamicDarkSurface
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            if (plan.isCompleted) {
                                ElevatedButton(
                                    onClick = { selectedCertificatePlan = plan },
                                    colors = ButtonDefaults.elevatedButtonColors(
                                        containerColor = GoldPrimary,
                                        contentColor = Color(0xFF1E1700)
                                    ),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.testTag("view_certificate_button")
                                ) {
                                    Icon(Icons.Default.CardMembership, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("عرض شهادة الإتمام الرقمية", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // --- Interactive Quiz Dialog ---
    if (showQuizDialog) {
        val q = quizQuestions[currentQuestionIdx]
        AlertDialog(
            onDismissRequest = { showQuizDialog = false },
            confirmButton = {
                if (isAnswerSubmitted) {
                    TextButton(onClick = {
                        if (currentQuestionIdx < quizQuestions.size - 1) {
                            currentQuestionIdx++
                            selectedAnswerIdx = null
                            isAnswerSubmitted = false
                        } else {
                            showQuizDialog = false
                        }
                    }) {
                        Text(
                            text = if (currentQuestionIdx < quizQuestions.size - 1) "السؤال التالي" else "إنهاء الاختبار",
                            color = GoldPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    TextButton(
                        onClick = {
                            if (selectedAnswerIdx != null) {
                                isAnswerSubmitted = true
                                if (selectedAnswerIdx == q.correctIndex) {
                                    quizScore++
                                }
                            }
                        },
                        enabled = selectedAnswerIdx != null
                    ) {
                        Text("تحقق من الإجابة", color = GoldPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "السؤال ${currentQuestionIdx + 1} من ${quizQuestions.size}",
                        color = TextGold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "الدرجة: $quizScore",
                        color = EmeraldPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Column {
                    Text(
                        text = q.question,
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    q.options.forEachIndexed { idx, optionText ->
                        val isChosen = selectedAnswerIdx == idx
                        val isCorrect = idx == q.correctIndex
                        val btnBg = when {
                            isAnswerSubmitted && isCorrect -> EmeraldPrimary.copy(alpha = 0.25f)
                            isAnswerSubmitted && isChosen && !isCorrect -> Color(0x3BFF0000)
                            isChosen -> GoldPrimary.copy(alpha = 0.25f)
                            else -> IslamicDarkSurface
                        }
                        val borderColor = when {
                            isAnswerSubmitted && isCorrect -> EmeraldPrimary
                            isAnswerSubmitted && isChosen && !isCorrect -> Color.Red
                            isChosen -> GoldPrimary
                            else -> IslamicGlassBorder
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(btnBg)
                                .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                                .padding(12.dp)
                                .testTag("quiz_option_$idx"),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            TextButton(
                                onClick = {
                                    if (!isAnswerSubmitted) selectedAnswerIdx = idx
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = optionText,
                                    color = TextPrimary,
                                    fontSize = 13.sp,
                                    lineHeight = 20.sp,
                                    textAlign = TextAlign.Right,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            },
            containerColor = IslamicDarkCard
        )
    }

    // --- Digital Completion Certificate Modal ---
    if (selectedCertificatePlan != null) {
        val cert = selectedCertificatePlan!!
        AlertDialog(
            onDismissRequest = { selectedCertificatePlan = null },
            confirmButton = {
                Row {
                    ElevatedButton(
                        onClick = {
                            ShareHelper.shareCertificate(
                                context = context,
                                userName = cert.userName,
                                surahName = cert.surahName,
                                date = cert.completionDate
                            )
                            hapticManager?.vibrateTap()
                        },
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = GoldPrimary,
                            contentColor = Color(0xFF1E1700)
                        )
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("مشاركة الشهادة", fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = { selectedCertificatePlan = null }) {
                        Text("إغلاق", color = TextSecondary)
                    }
                }
            },
            title = {
                Text(
                    text = "شهادة إتمام حفظ رقمية",
                    color = GoldLight,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFF142442), Color(0xFF091223))
                            )
                        )
                        .border(2.dp, GoldPrimary, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                        .testTag("certificate_card")
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "﴿يَرْفَعِ اللَّهُ الَّذِينَ آمَنُوا مِنكُمْ وَالَّذِينَ أُوتُوا الْعِلْمَ دَرَجَاتٍ﴾",
                            color = GoldLight,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "شَهَادَةُ إِتْمَامِ حِفْظٍ",
                            color = GoldPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "يشهد تطبيق «نور المسلم» بأن القارئ المبارك:",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                        Text(
                            text = cert.userName,
                            color = TextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "قد أتم بفضل الله وتوفيقه حفظ: سورة ${cert.surahName}",
                            color = TextGold,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "تاريخ الإنجاز: ${cert.completionDate}",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            },
            containerColor = IslamicDarkCard
        )
    }

    // --- New Plan Dialog ---
    if (showNewPlanDialog) {
        AlertDialog(
            onDismissRequest = { showNewPlanDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val days = newPlanDays.toIntOrNull() ?: 15
                        val newPlan = MemorizationPlan(
                            id = "plan_${System.currentTimeMillis()}",
                            title = newPlanTitle,
                            surahNumber = 36,
                            surahName = newPlanSurahName,
                            startAyah = 1,
                            endAyah = 83,
                            targetDays = days,
                            currentDay = 1,
                            masteredAyahs = 0,
                            method = MemorizationMethod.REPETITION
                        )
                        quranRepo.addMemorizationPlan(newPlan)
                        showNewPlanDialog = false
                    }
                ) {
                    Text("حفظ الخطة", color = GoldPrimary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewPlanDialog = false }) {
                    Text("إلغاء", color = TextSecondary)
                }
            },
            title = {
                Text("إنشاء خطة حفظ جديدة", color = TextPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = newPlanTitle,
                        onValueChange = { newPlanTitle = it },
                        label = { Text("اسم الخطة") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GoldPrimary,
                            unfocusedBorderColor = IslamicGlassBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newPlanSurahName,
                        onValueChange = { newPlanSurahName = it },
                        label = { Text("السورة") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GoldPrimary,
                            unfocusedBorderColor = IslamicGlassBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newPlanDays,
                        onValueChange = { newPlanDays = it },
                        label = { Text("المدة المستهدفة (أيام)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GoldPrimary,
                            unfocusedBorderColor = IslamicGlassBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                }
            },
            containerColor = IslamicDarkCard
        )
    }
}
