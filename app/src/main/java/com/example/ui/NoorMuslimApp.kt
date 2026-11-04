package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.util.AppAudioManager
import com.example.util.AppHapticManager
import com.example.util.CompassSensorManager
import androidx.compose.ui.unit.sp
import com.example.data.AzkarRepository
import com.example.data.HajjMoodAndAssistantRepository
import com.example.data.PrayerRepository
import com.example.data.QuranRepository
import com.example.data.TasbihAndSalatiRepository
import com.example.ui.components.AudioMiniPlayerBar
import com.example.ui.screens.AzkarScreen
import com.example.ui.screens.HajjUmrahScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.IslamicAssistantScreen
import com.example.ui.screens.MemorizationScreen
import com.example.ui.screens.MoreHubScreen
import com.example.ui.screens.PrayerScreen
import com.example.ui.screens.QiblaScreen
import com.example.ui.screens.QuranMoodScreen
import com.example.ui.screens.QuranScreen
import com.example.ui.screens.RadioScreen
import com.example.ui.screens.SalatiScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.StoriesScreen
import com.example.ui.screens.TasbihScreen
import com.example.ui.screens.WallpapersWidgetsScreen
import com.example.ui.screens.WorshipPlansScreen
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.IslamicDarkBg
import com.example.ui.theme.IslamicDarkCard
import com.example.ui.theme.IslamicDarkSurface
import com.example.ui.theme.IslamicGlassBorder
import com.example.ui.theme.TextGold
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

sealed class NavItem(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Home : NavItem("home", "الرئيسية", Icons.Filled.Home, Icons.Outlined.Home)
    object Quran : NavItem("quran", "القرآن", Icons.Filled.Book, Icons.Outlined.Book)
    object Prayer : NavItem("prayer", "الصلاة", Icons.Filled.AccessTime, Icons.Outlined.AccessTime)
    object Azkar : NavItem("azkar", "الأذكار", Icons.Filled.Spa, Icons.Outlined.Spa)
    object More : NavItem("more", "المزيد", Icons.Filled.MoreHoriz, Icons.Outlined.MoreHoriz)
}

@Composable
fun NoorMuslimApp() {
    val context = LocalContext.current
    val quranRepo = remember { QuranRepository() }
    val azkarRepo = remember { AzkarRepository() }
    val prayerRepo = remember { PrayerRepository() }
    val salatiRepo = remember { TasbihAndSalatiRepository() }
    val hajjMoodRepo = remember { HajjMoodAndAssistantRepository() }

    val audioManager = remember { AppAudioManager(context) }
    val hapticManager = remember { AppHapticManager(context) }
    val compassSensorManager = remember { CompassSensorManager(context) }

    DisposableEffect(Unit) {
        onDispose {
            audioManager.release()
            compassSensorManager.stopListening()
        }
    }

    val activeRadio by hajjMoodRepo.activeRadio.collectAsState()
    val playbackState by audioManager.playbackState.collectAsState()

    var currentScreen by remember { mutableStateOf("home") }
    var selectedSurahForReading by remember { mutableStateOf<Int?>(null) }
    var initialAzkarCategory by remember { mutableStateOf("sabah") }

    val navItems = listOf(
        NavItem.Home,
        NavItem.Quran,
        NavItem.Prayer,
        NavItem.Azkar,
        NavItem.More
    )

    // Back handling
    val isPrimaryScreen = currentScreen in listOf("home", "quran", "prayer", "azkar", "more")
    if (!isPrimaryScreen) {
        BackHandler {
            currentScreen = "home"
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(IslamicDarkBg),
            containerColor = IslamicDarkBg,
            bottomBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                ) {
                    // Floating Mini Player if audio playback or live radio is active
                    val isPlayingAny = playbackState.isPlaying || activeRadio != null
                    if (isPlayingAny) {
                        val playerTitle = if (playbackState.title.isNotEmpty()) playbackState.title else (activeRadio?.name ?: "بث مباشر")
                        val playerSubtitle = if (playbackState.subtitle.isNotEmpty()) playbackState.subtitle else (activeRadio?.country ?: "الإذاعة الإسلامية")
                        AudioMiniPlayerBar(
                            title = playerTitle,
                            subtitle = playerSubtitle,
                            isPlaying = playbackState.isPlaying,
                            onTogglePlay = { audioManager.togglePlayPause() },
                            onClose = {
                                audioManager.stop()
                                if (activeRadio != null) hajjMoodRepo.toggleRadio(activeRadio!!)
                            }
                        )
                    }

                    // Bottom Navigation Bar with Glassmorphic styling
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp))
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        IslamicDarkCard.copy(alpha = 0.98f),
                                        IslamicDarkSurface.copy(alpha = 0.98f)
                                    )
                                )
                            )
                            .border(
                                1.dp,
                                IslamicGlassBorder.copy(alpha = 0.35f),
                                RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp)
                            )
                    ) {
                        NavigationBar(
                            containerColor = Color.Transparent,
                            tonalElevation = 0.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("bottom_nav_bar")
                        ) {
                            navItems.forEach { item ->
                                val selected = currentScreen == item.route ||
                                    (item.route == "quran" && currentScreen.startsWith("quran_read")) ||
                                    (item.route == "azkar" && currentScreen.startsWith("azkar_"))

                                NavigationBarItem(
                                    selected = selected,
                                    onClick = {
                                        currentScreen = item.route
                                        if (item.route == "quran") selectedSurahForReading = null
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                            contentDescription = item.title,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = item.title,
                                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 11.sp
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color(0xFF1E1700),
                                        selectedTextColor = GoldLight,
                                        indicatorColor = GoldPrimary,
                                        unselectedIconColor = TextSecondary,
                                        unselectedTextColor = TextSecondary
                                    ),
                                    modifier = Modifier.testTag("nav_${item.route}")
                                )
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when {
                    currentScreen == "home" -> HomeScreen(
                        prayerRepo = prayerRepo,
                        quranRepo = quranRepo,
                        azkarRepo = azkarRepo,
                        salatiRepo = salatiRepo,
                        audioManager = audioManager,
                        hapticManager = hapticManager,
                        onNavigate = { route ->
                            if (route.startsWith("quran_read_")) {
                                val sNum = route.removePrefix("quran_read_").toIntOrNull() ?: 1
                                selectedSurahForReading = sNum
                                currentScreen = "quran"
                            } else if (route == "azkar_sabah") {
                                initialAzkarCategory = "sabah"
                                currentScreen = "azkar"
                            } else if (route == "azkar_masaa") {
                                initialAzkarCategory = "masaa"
                                currentScreen = "azkar"
                            } else {
                                currentScreen = route
                            }
                        }
                    )
                    currentScreen == "quran" -> QuranScreen(
                        quranRepo = quranRepo,
                        initialSurahNumber = selectedSurahForReading,
                        audioManager = audioManager,
                        hapticManager = hapticManager,
                        onNavigateMemorization = { currentScreen = "memorization" }
                    )
                    currentScreen == "prayer" -> PrayerScreen(
                        prayerRepo = prayerRepo,
                        audioManager = audioManager,
                        hapticManager = hapticManager,
                        onNavigateSalati = { currentScreen = "salati" },
                        onNavigateQibla = { currentScreen = "qibla" }
                    )
                    currentScreen == "azkar" -> AzkarScreen(
                        azkarRepo = azkarRepo,
                        initialCategoryId = initialAzkarCategory,
                        audioManager = audioManager,
                        hapticManager = hapticManager,
                        onNavigateTasbih = { currentScreen = "tasbih" }
                    )
                    currentScreen == "more" -> MoreHubScreen(
                        onNavigate = { route -> currentScreen = route }
                    )
                    currentScreen == "memorization" -> MemorizationScreen(
                        quranRepo = quranRepo,
                        audioManager = audioManager,
                        hapticManager = hapticManager,
                        onBackClick = { currentScreen = "quran" }
                    )
                    currentScreen == "tasbih" -> TasbihScreen(
                        salatiRepo = salatiRepo,
                        audioManager = audioManager,
                        hapticManager = hapticManager,
                        onBackClick = { currentScreen = "home" }
                    )
                    currentScreen == "salati" -> SalatiScreen(
                        salatiRepo = salatiRepo,
                        onBackClick = { currentScreen = "prayer" }
                    )
                    currentScreen == "qibla" -> QiblaScreen(
                        prayerRepo = prayerRepo,
                        compassSensorManager = compassSensorManager,
                        hapticManager = hapticManager,
                        onBackClick = { currentScreen = "prayer" }
                    )
                    currentScreen == "quran_mood" -> QuranMoodScreen(
                        hajjMoodRepo = hajjMoodRepo,
                        audioManager = audioManager,
                        hapticManager = hapticManager,
                        onBackClick = { currentScreen = "more" }
                    )
                    currentScreen == "hajj" -> HajjUmrahScreen(
                        hajjRepo = hajjMoodRepo,
                        audioManager = audioManager,
                        hapticManager = hapticManager,
                        onBackClick = { currentScreen = "more" }
                    )
                    currentScreen == "radio" -> RadioScreen(
                        hajjMoodRepo = hajjMoodRepo,
                        audioManager = audioManager,
                        hapticManager = hapticManager,
                        onBackClick = { currentScreen = "more" }
                    )
                    currentScreen == "stories" -> StoriesScreen(
                        storiesRepo = remember { com.example.data.WorshipAndStoriesRepository() },
                        onBackClick = { currentScreen = "more" }
                    )
                    currentScreen == "plans" -> WorshipPlansScreen(
                        worshipRepo = remember { com.example.data.WorshipAndStoriesRepository() },
                        onBackClick = { currentScreen = "more" }
                    )
                    currentScreen == "assistant" -> IslamicAssistantScreen(
                        assistantRepo = hajjMoodRepo,
                        onBackClick = { currentScreen = "more" }
                    )
                    currentScreen == "wallpapers" -> WallpapersWidgetsScreen(
                        hajjMoodRepo = hajjMoodRepo,
                        hapticManager = hapticManager,
                        onBackClick = { currentScreen = "more" }
                    )
                    currentScreen == "settings" -> SettingsScreen(
                        onBackClick = { currentScreen = "more" }
                    )
                    else -> HomeScreen(
                        prayerRepo = prayerRepo,
                        quranRepo = quranRepo,
                        azkarRepo = azkarRepo,
                        salatiRepo = salatiRepo,
                        audioManager = audioManager,
                        hapticManager = hapticManager,
                        onNavigate = { route -> currentScreen = route }
                    )
                }
            }
        }
    }
}
