package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val IslamicDarkColorScheme = darkColorScheme(
    primary = GoldPrimary,
    onPrimary = Color(0xFF1E1700),
    primaryContainer = GoldDark,
    onPrimaryContainer = GoldLight,
    secondary = EmeraldPrimary,
    onSecondary = Color(0xFF002114),
    secondaryContainer = EmeraldDark,
    onSecondaryContainer = EmeraldLight,
    tertiary = PurpleQuran,
    onTertiary = Color.White,
    tertiaryContainer = PurpleQuranDark,
    onTertiaryContainer = PurpleQuranLight,
    background = IslamicDarkBg,
    onBackground = TextPrimary,
    surface = IslamicDarkCard,
    onSurface = TextPrimary,
    surfaceVariant = IslamicDarkSurface,
    onSurfaceVariant = TextSecondary,
    outline = IslamicGlassBorder
)

private val IslamicLightColorScheme = lightColorScheme(
    primary = GoldDark,
    onPrimary = Color.White,
    primaryContainer = GoldLight,
    onPrimaryContainer = Color(0xFF261900),
    secondary = EmeraldDark,
    onSecondary = Color.White,
    secondaryContainer = EmeraldLight,
    onSecondaryContainer = Color(0xFF002114),
    tertiary = PurpleQuranDark,
    onTertiary = Color.White,
    tertiaryContainer = PurpleQuranLight,
    onTertiaryContainer = Color(0xFF21005D),
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF0F172A),
    surface = Color.White,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFE2E8F0),
    onSurfaceVariant = Color(0xFF475569),
    outline = Color(0xFFCBD5E1)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to stunning Islamic Dark luxury theme
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) IslamicDarkColorScheme else IslamicLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
