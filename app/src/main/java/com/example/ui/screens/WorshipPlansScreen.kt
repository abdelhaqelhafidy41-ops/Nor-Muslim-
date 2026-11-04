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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.WorshipAndStoriesRepository
import com.example.model.PlanTask
import com.example.model.WorshipPlan
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
fun WorshipPlansScreen(
    worshipRepo: WorshipAndStoriesRepository,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val plans by worshipRepo.plans.collectAsState()
    var showCreateDialog by remember { mutableStateOf(false) }

    var newPlanTitle by remember { mutableStateOf("") }
    var newPlanDesc by remember { mutableStateOf("") }
    var newPlanCategory by remember { mutableStateOf("عامة") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("worship_plans_screen")
    ) {
        IslamicTopBar(
            title = "الخطط والبرامج التعبدية",
            subtitle = "أكثر من ٥٠ برنامجاً تعبدياً قابلاً للتخصيص",
            onBackClick = onBackClick,
            trailingAction = {
                IconButton(
                    onClick = { showCreateDialog = true },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(GoldPrimary)
                        .testTag("create_custom_plan_button")
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
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(plans) { plan ->
                IslamicGlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("plan_card_${plan.id}"),
                    borderColor = if (plan.progress >= 1f) EmeraldPrimary.copy(alpha = 0.6f) else IslamicGlassBorder.copy(alpha = 0.4f)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = plan.title,
                                    color = TextPrimary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = plan.description,
                                    color = TextSecondary,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            PillBadge(text = plan.category, color = GoldPrimary)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Progress bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "نسبة الإنجاز: ${(plan.progress * 100).toInt()}%",
                                color = TextGold,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "اليوم ${plan.currentDay} من ${plan.durationDays}",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        LinearProgressIndicator(
                            progress = { plan.progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(CircleShape),
                            color = if (plan.progress >= 1f) EmeraldPrimary else GoldPrimary,
                            trackColor = IslamicDarkSurface
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Tasks Checklist
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            plan.tasks.forEach { task ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (task.isDone) EmeraldPrimary.copy(alpha = 0.12f) else IslamicDarkSurface)
                                        .clickable { worshipRepo.toggleTask(plan.id, task.id) }
                                        .padding(horizontal = 10.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (task.isDone) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                        contentDescription = null,
                                        tint = if (task.isDone) EmeraldPrimary else TextSecondary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = task.title,
                                        color = if (task.isDone) EmeraldLight else TextPrimary,
                                        fontSize = 13.sp,
                                        fontWeight = if (task.isDone) FontWeight.SemiBold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // --- Create Plan Dialog ---
    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newPlanTitle.isNotBlank()) {
                            val newPlan = WorshipPlan(
                                id = "custom_${System.currentTimeMillis()}",
                                title = newPlanTitle,
                                description = newPlanDesc.ifBlank { "برنامج تعبدي مخصص" },
                                category = newPlanCategory,
                                durationDays = 30,
                                currentDay = 1,
                                tasks = listOf(
                                    PlanTask("ct1", "الورد اليومي للقرآن الكريم"),
                                    PlanTask("ct2", "أذكار الصباح والمساء كاملة"),
                                    PlanTask("ct3", "صلاة ركعتين في جوف الليل")
                                ),
                                isCustom = true
                            )
                            worshipRepo.addCustomPlan(newPlan)
                            showCreateDialog = false
                            newPlanTitle = ""
                            newPlanDesc = ""
                        }
                    }
                ) {
                    Text("حفظ الخطة", color = GoldPrimary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) {
                    Text("إلغاء", color = TextSecondary)
                }
            },
            title = {
                Text("إنشاء خطة عبادة مخصصة", color = TextPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = newPlanTitle,
                        onValueChange = { newPlanTitle = it },
                        label = { Text("عنوان الخطة") },
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
                        value = newPlanDesc,
                        onValueChange = { newPlanDesc = it },
                        label = { Text("الوصف والهدف") },
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
