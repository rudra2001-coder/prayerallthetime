package com.rudra.prayerallthetime.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
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
