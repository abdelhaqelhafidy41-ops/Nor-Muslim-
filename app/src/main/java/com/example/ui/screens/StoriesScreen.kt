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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.WorshipAndStoriesRepository
import com.example.model.IslamicStory
import com.example.model.StoryCategory
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
fun StoriesScreen(
    storiesRepo: WorshipAndStoriesRepository,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val allStories = remember { storiesRepo.stories }
    var selectedCategory by remember { mutableStateOf<StoryCategory?>(null) }
    var viewingStory by remember { mutableStateOf<IslamicStory?>(null) }

    val filtered = remember(selectedCategory) {
        if (selectedCategory == null) allStories
        else allStories.filter { it.category == selectedCategory }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("stories_screen")
    ) {
        IslamicTopBar(
            title = "السيرة والقصص الإسلامية",
            subtitle = "سيرة النبي ﷺ وقصص الصحابة والأنبياء والعبر المستفادة",
            onBackClick = onBackClick
        )

        // Categories Row
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                val isAll = selectedCategory == null
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isAll) GoldPrimary else IslamicDarkCard)
                        .border(1.dp, if (isAll) GoldLight else IslamicGlassBorder, RoundedCornerShape(14.dp))
                        .clickable { selectedCategory = null }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "الكل",
                        color = if (isAll) Color(0xFF1E1700) else TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
            items(StoryCategory.values()) { cat ->
                val isSel = selectedCategory == cat
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isSel) GoldPrimary else IslamicDarkCard)
                        .border(1.dp, if (isSel) GoldLight else IslamicGlassBorder, RoundedCornerShape(14.dp))
                        .clickable { selectedCategory = cat }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = cat.titleArabic,
                        color = if (isSel) Color(0xFF1E1700) else TextPrimary,
                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 13.sp
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filtered) { story ->
                IslamicGlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("story_card_${story.id}"),
                    onClick = { viewingStory = story }
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            PillBadge(text = story.category.titleArabic, color = EmeraldPrimary)
                            Text(
                                text = "اضغط للمطالعة ←",
                                color = TextGold,
                                fontSize = 11.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = story.title,
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = story.summary,
                            color = TextSecondary,
                            fontSize = 13.sp,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "📚 المرجع: ${story.authenticSource}",
                            color = GoldLight,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }

    // --- Story Detail Modal ---
    if (viewingStory != null) {
        val s = viewingStory!!
        AlertDialog(
            onDismissRequest = { viewingStory = null },
            confirmButton = {
                TextButton(onClick = { viewingStory = null }) {
                    Text("إغلاق", color = GoldPrimary, fontWeight = FontWeight.Bold)
                }
            },
            title = {
                Column {
                    PillBadge(text = s.category.titleArabic, color = EmeraldPrimary)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = s.title, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                }
            },
            text = {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    item {
                        Text(
                            text = s.content,
                            color = TextPrimary,
                            fontSize = 14.sp,
                            lineHeight = 24.sp
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "الدروس والعبر المستفادة:",
                            color = GoldLight,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    items(s.lessons) { lesson ->
                        Row(verticalAlignment = Alignment.Top) {
                            Text(text = "•", color = EmeraldPrimary, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = lesson,
                                color = TextSecondary,
                                fontSize = 12.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "المصدر: ${s.authenticSource}",
                            color = TextGold,
                            fontSize = 11.sp
                        )
                    }
                }
            },
            containerColor = IslamicDarkCard
        )
    }
}
