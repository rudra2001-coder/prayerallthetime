package com.rudra.prayerallthetime.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

// ============================================
// LIGHT COLOR SCHEME
// ============================================
private val LightColorScheme = lightColorScheme(
    primary = EmeraldGreen,
    onPrimary = Color.White,
    primaryContainer = EmeraldGreenLight,
    onPrimaryContainer = EmeraldGreenDark,
    secondary = IslamicGold,
    onSecondary = Color.Black,
    secondaryContainer = IslamicGoldLight,
    onSecondaryContainer = IslamicGoldDark,
    tertiary = RamadanPurple,
    onTertiary = Color.White,
    tertiaryContainer = RamadanLight,
    onTertiaryContainer = RamadanColor,
    background = SurfaceElevated,
    onBackground = TextPrimaryLight,
    surface = SurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = SurfaceElevated,
    onSurfaceVariant = TextSecondaryLight,
    surfaceTint = EmeraldGreen,
    inverseSurface = MidnightBlue,
    inverseOnSurface = SoftWhite,
    error = ErrorColor,
    onError = Color.White,
    errorContainer = ErrorLight,
    onErrorContainer = ErrorDark,
    outline = DividerLight,
    outlineVariant = CardBorder,
    scrim = Color.Black.copy(alpha = 0.5f)
)

// ============================================
// DARK COLOR SCHEME
// ============================================
private val DarkColorScheme = darkColorScheme(
    primary = EmeraldGreenMedium,
    onPrimary = Color.White,
    primaryContainer = EmeraldGreenDark,
    onPrimaryContainer = EmeraldGreenLight,
    secondary = IslamicGold,
    onSecondary = Color.Black,
    secondaryContainer = IslamicGoldDark,
    onSecondaryContainer = IslamicGoldLight,
    tertiary = RamadanPurple,
    onTertiary = Color.White,
    tertiaryContainer = RamadanColor,
    onTertiaryContainer = RamadanLight,
    background = MidnightBlue,
    onBackground = TextPrimaryDark,
    surface = MidnightBlueLight,
    onSurface = TextPrimaryDark,
    surfaceVariant = MidnightBlueCard,
    onSurfaceVariant = TextSecondaryDark,
    surfaceTint = EmeraldGreenMedium,
    inverseSurface = SurfaceLight,
    inverseOnSurface = TextPrimaryLight,
    error = ErrorColor,
    onError = Color.White,
    errorContainer = ErrorDark,
    onErrorContainer = ErrorLight,
    outline = DividerDark,
    outlineVariant = CardBorderDark,
    scrim = Color.Black.copy(alpha = 0.7f)
)

// ============================================
// SHAPES
// ============================================
val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

// ============================================
// GRADIENTS
// ============================================
object AppGradients {
    // Primary Islamic Gradient
    val primaryGradient = Brush.linearGradient(
        colors = listOf(EmeraldGreen, EmeraldGreenMedium)
    )
    
    // Gold Gradient
    val goldGradient = Brush.linearGradient(
        colors = listOf(IslamicGold, IslamicGoldShimmer)
    )
    
    // Premium Background Gradient
    val premiumBackground = Brush.verticalGradient(
        colors = listOf(PremiumBackgroundStart, PremiumBackgroundEnd)
    )
    
    // Islamic Theme Gradient
    val islamicGradient = Brush.linearGradient(
        colors = listOf(IslamicGradientStart, IslamicGradientEnd)
    )
    
    // Ramadan Gradient
    val ramadanGradient = Brush.linearGradient(
        colors = listOf(RamadanGradientStart, RamadanGradientEnd)
    )
    
    // Surface Gradient (for cards)
    val surfaceGradient = Brush.verticalGradient(
        colors = listOf(
            SurfaceLight,
            SurfaceElevated
        )
    )
    
    // Dark Surface Gradient
    val darkSurfaceGradient = Brush.verticalGradient(
        colors = listOf(
            MidnightBlueLight,
            MidnightBlueCard
        )
    )
    
    // Success Gradient
    val successGradient = Brush.linearGradient(
        colors = listOf(SuccessColor, SuccessDark)
    )
    
    // Prayer Time Gradients
    val fajrGradient = Brush.linearGradient(
        colors = listOf(FajrColor, MidnightBlue)
    )
    
    val sunriseGradient = Brush.linearGradient(
        colors = listOf(WarningColor, IslamicGold)
    )
    
    val dhuhrGradient = Brush.linearGradient(
        colors = listOf(DhuhrColor, WarningColor)
    )
    
    val asrGradient = Brush.linearGradient(
        colors = listOf(AsrColor, InfoColor)
    )
    
    val maghribGradient = Brush.linearGradient(
        colors = listOf(MaghribColor, RamadanPurple)
    )
    
    val ishaGradient = Brush.linearGradient(
        colors = listOf(IshaColor, MidnightBlueDark)
    )
}

// ============================================
// SPACING
// ============================================
object AppSpacing {
    val xs = 4.dp
    val sm = 8.dp
    val md = 16.dp
    val lg = 24.dp
    val xl = 32.dp
    val xxl = 48.dp
}

// ============================================
// ELEVATION
// ============================================
object AppElevation {
    val none = 0.dp
    val xs = 2.dp
    val sm = 4.dp
    val md = 8.dp
    val lg = 12.dp
    val xl = 16.dp
}

// ============================================
// COMPOSITION LOCAL PROVIDERS
// ============================================
val LocalAppGradient = staticCompositionLocalOf { AppGradients }
val LocalAppSpacing = staticCompositionLocalOf { AppSpacing }
val LocalAppElevation = staticCompositionLocalOf { AppElevation }

// ============================================
// LEGACY COLORS (for backward compatibility)
// ============================================
val PrimaryColor = Color(0xFF1A237E)
val SecondaryColor = Color(0xFF283593)
val AccentColor = Color(0xFF00ACC1)
val BackgroundColor = Color(0xFF0D1B2A)
val GradientStartLegacy = Color(0xFF4A90E2)
val GradientEndLegacy = Color(0xFF9B59B6)
val PremiumBackgroundStartLegacy = Color(0xFF1A237E)
val PremiumBackgroundEndLegacy = Color(0xFF0D1B2A)

// Legacy Premium Gradient
val PremiumGradient = Brush.verticalGradient(
    colors = listOf(PremiumBackgroundStartLegacy, PremiumBackgroundEndLegacy)
)

// ============================================
// THEME COMPOSABLE
// ============================================
@Composable
fun PrayerAllTheTimeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Auto dark mode based on system setting
    dynamicColor: Boolean = false, // Disable dynamic colors for consistent Islamic theme
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = if (darkTheme) MidnightBlue.hashCode() else SurfaceLight.hashCode()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }
    
    CompositionLocalProvider(
        LocalAppGradient provides AppGradients,
        LocalAppSpacing provides AppSpacing,
        LocalAppElevation provides AppElevation
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = AppShapes,
            content = content
        )
    }
}

// ============================================
// EXTENSION FUNCTIONS
// ============================================
fun getPrayerGradient(prayerName: String): Brush {
    return when (prayerName.lowercase()) {
        "fajr" -> AppGradients.fajrGradient
        "sunrise" -> AppGradients.sunriseGradient
        "dhuhr" -> AppGradients.dhuhrGradient
        "asr" -> AppGradients.asrGradient
        "maghrib" -> AppGradients.maghribGradient
        "isha" -> AppGradients.ishaGradient
        else -> AppGradients.primaryGradient
    }
}

fun getPrayerColor(prayerName: String): Color {
    return when (prayerName.lowercase()) {
        "fajr" -> FajrColor
        "sunrise" -> WarningColor
        "dhuhr" -> DhuhrColor
        "asr" -> AsrColor
        "maghrib" -> MaghribColor
        "isha" -> IshaColor
        else -> EmeraldGreen
    }
}
