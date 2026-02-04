package com.rudra.prayerallthetime.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = EmeraldGreen,
    onPrimary = Color.White,
    primaryContainer = EmeraldGreenDark,
    secondary = IslamicGold,
    onSecondary = Color.Black,
    background = MidnightBlue,
    onBackground = SoftWhite,
    surface = MidnightBlueLight,
    onSurface = SoftWhite,
    tertiary = IslamicGoldDark

)
val PrimaryColor = Color(0xFF1A237E)
val SecondaryColor = Color(0xFF283593)
val AccentColor = Color(0xFF00ACC1)
val BackgroundColor = Color(0xFF0D1B2A)


val GradientStart = Color(0xFF4A90E2)
val GradientEnd = Color(0xFF9B59B6)


// Premium Colors
val PremiumBackgroundStart = Color(0xFF1A237E)
val PremiumBackgroundEnd = Color(0xFF0D1B2A)
val PremiumGradient = Brush.verticalGradient(
    colors = listOf(PremiumBackgroundStart, PremiumBackgroundEnd)
)

// Card Colors
val CardBackground = Color(0xFFFFFFFF)
val PrayerCardBackground = Color(0xFFFFFFFF)
val QuranCardBackground = Color(0xFF2E7D32)
val HadithCardBackground = Color(0xFF5D4037)
val RamadanCardBackground = Color(0xFF6A1B9A)
val StatsCardBackground = Color(0xFFFFFFFF)

// Accent Colors
val QuranAccent = Color(0xFFC8E6C9)
val HadithAccent = Color(0xFFD7CCC8)
val RamadanAccent = Color(0xFFE1BEE7)

// Status Colors
val SuccessColor = Color(0xFF4CAF50)
val WarningColor = Color(0xFFFF9800)
val StreakColor = Color(0xFFFF6F00)
val PrayerColor = Color(0xFF2196F3)
val TasbeehColor = Color(0xFF9C27B0)
val TaraweehColor = Color(0xFF673AB7)

// Text Colors
val PrimaryText = Color(0xFF212121)
val SecondaryText = Color(0xFF757575)
val OnAccentText = Color(0xFFFFFFFF)

// UI Colors
val ProgressTrackColor = Color(0xFFE0E0E0)
val DividerColor = Color(0xFFE0E0E0)
val PrayerCardShadow = Color(0x40000000)
@Composable
fun PrayerAllTheTimeTheme(
    content: @Composable () -> Unit
) {
    // Forced Dark Theme for the aesthetic requested
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
