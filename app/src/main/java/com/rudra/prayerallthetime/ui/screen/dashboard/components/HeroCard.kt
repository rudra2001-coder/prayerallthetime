package com.rudra.prayerallthetime.ui.screen.dashboard.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.rudra.prayerallthetime.ui.theme.*
import kotlinx.coroutines.delay
import java.util.Locale
import java.util.concurrent.TimeUnit

// Extension function for better performance
private fun Modifier.conditional(condition: Boolean, modifier: Modifier.() -> Modifier): Modifier {
    return if (condition) then(modifier(Modifier)) else this
}

// Arabic text direction utility
private fun isArabicLocale(): Boolean {
    return Locale.getDefault().language == "ar"
}

// Enhanced text composable with Arabic support
@Composable
fun ArabicAwareText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 14.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    color: Color = Color.White,
    textAlign: TextAlign = if (isArabicLocale()) TextAlign.End else TextAlign.Start
) {
    Text(
        text = text,
        modifier = modifier,
        fontSize = fontSize,
        fontWeight = fontWeight,
        color = color,
        textAlign = textAlign
    )
}

// Helper to format remaining time
private fun formatRemainingTime(millis: Long): String {
    if (millis <= 0) return "00:00:00"
    val hours = TimeUnit.MILLISECONDS.toHours(millis)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

@Composable
fun PremiumHeroCard(
    nextPrayerName: String = "Fajr",
    countdown: String = "02:45:18",
    nextPrayerMillis: Long = 0L,
    sunriseTime: String = "06:45 AM",
    hijriDate: String = "25 Ramadan 1445 AH",
    gregorianDate: String = "Monday, April 15, 2026",
    cityName: String = "Mecca, Saudi Arabia",
    prayerTime: String = "05:15 AM",
    isAlarmSet: Boolean = true,
    qiblaDirection: String = "90°",
    prayerProgress: Float = 0.75f,
    isLoading: Boolean = false,
    currentPrayerName: String = "",
    nextPrayerArabicName: String = "",
    temperature: String? = null,
    humidity: String? = null,
    windDirection: String? = null,
    isPremiumUser: Boolean = false,
    onDetectLocationClick: () -> Unit = {},
    onAlarmClick: () -> Unit = {},
    onCalendarClick: () -> Unit = {},
    onLocationClick: () -> Unit = {},
    onPrayerInfoClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Internal ticker for perfect countdown
    var currentMillis by remember { mutableStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            currentMillis = System.currentTimeMillis()
        }
    }

    val activeCountdown = if (nextPrayerMillis > 0) {
        formatRemainingTime(nextPrayerMillis - currentMillis)
    } else {
        countdown
    }

    val infiniteTransition = rememberInfiniteTransition()
    val moonPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -1000f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing)
        )
    )

    var isPressed by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .fillMaxWidth()
            .heightIn(min = 580.dp, max = 720.dp)
            .pointerInput(Unit) {
                while (true) {
                    awaitPointerEventScope {
                        awaitFirstDown()
                        isPressed = true
                        waitForUpOrCancellation()
                        isPressed = false
                    }
                }
            }
            .scale(if (isPressed) 0.98f else 1f)
            .animateContentSize(),
        shape = RoundedCornerShape(36.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isPressed) 8.dp else 20.dp,
            pressedElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        border = BorderStroke(1.5.dp, IslamicGold.copy(alpha = 0.4f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF0F1B4C),
                            Color(0xFF1A237E),
                            Color(0xFF283593)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                )
                .drawBehind {
                    // Star field animation
                    for (i in 0..25) {
                        val x = (i * size.width / 25) + (Math.sin((moonPhase + i).toDouble()) * 15).toFloat()
                        val y = (i * size.height / 25) + (Math.cos((moonPhase + i).toDouble()) * 15).toFloat()
                        drawCircle(
                            color = Color.White.copy(alpha = 0.15f + (i % 5) * 0.05f),
                            radius = 1.2f + (i % 4).toFloat(),
                            center = Offset(x % size.width, y % size.height)
                        )
                    }

                    // Moon design
                    val moonCenter = Offset(size.width * 0.85f, size.height * 0.12f)
                    drawCircle(
                        color = IslamicGold.copy(alpha = 0.2f),
                        radius = 50f,
                        center = moonCenter
                    )
                    drawCircle(
                        color = Color(0xFF1A237E),
                        radius = 42f,
                        center = moonCenter.copy(x = moonCenter.x + 12f)
                    )

                    // Progress ring
                    drawArc(
                        color = IslamicGold.copy(alpha = 0.25f),
                        startAngle = -90f,
                        sweepAngle = 360f * prayerProgress.coerceIn(0f, 1f),
                        useCenter = false,
                        topLeft = Offset(size.width * 0.04f, size.height * 0.04f),
                        size = Size(size.width * 0.92f, size.height * 0.92f),
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                    )

                    if (isLoading) {
                        val shimmerWidth = 400f
                        drawRect(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.White.copy(alpha = 0.08f),
                                    Color.Transparent
                                ),
                                start = Offset(shimmerOffset, 0f),
                                end = Offset(shimmerOffset + shimmerWidth, 0f)
                            ),
                            topLeft = Offset(0f, 0f),
                            size = size
                        )
                    }
                }
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Area
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White.copy(alpha = 0.05f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            ArabicAwareText(
                                text = "🌙 ISLAMIC DATE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            ArabicAwareText(
                                text = hijriDate,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = IslamicGold
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            PremiumIconButton(
                                icon = Icons.Default.CalendarMonth,
                                onClick = onCalendarClick,
                                tint = Color.White,
                                backgroundColor = Color.White.copy(alpha = 0.1f),
                                tooltip = "Calendar"
                            )
                            PremiumIconButton(
                                icon = Icons.Default.Notifications,
                                onClick = onAlarmClick,
                                tint = if (isAlarmSet) IslamicGold else Color.White.copy(alpha = 0.5f),
                                backgroundColor = if (isAlarmSet) IslamicGold.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.1f),
                                tooltip = "Alarm"
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Next Prayer Info
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(32.dp))
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(IslamicGold.copy(alpha = 0.15f), Color.Transparent)
                            )
                        )
                        .border(1.dp, IslamicGold.copy(alpha = 0.2f), RoundedCornerShape(32.dp))
                        .clickable(onClick = onPrayerInfoClick)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ArabicAwareText(
                            text = "🕌 NEXT PRAYER",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = IslamicGold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                ArabicAwareText(
                                    text = nextPrayerName,
                                    fontSize = 38.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                                if (nextPrayerArabicName.isNotEmpty()) {
                                    ArabicAwareText(
                                        text = nextPrayerArabicName,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = IslamicGold.copy(alpha = 0.9f)
                                    )
                                }
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                ArabicAwareText(
                                    text = prayerTime,
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = IslamicGold
                                )
                                ArabicAwareText(
                                    text = "Adhan Time",
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.6f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                        LinearProgressIndicator(
                            progress = prayerProgress.coerceIn(0f, 1f),
                            modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                            color = IslamicGold,
                            trackColor = Color.White.copy(alpha = 0.1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Countdown Timer
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(Color.White.copy(alpha = 0.05f))
                        .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(32.dp))
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        ArabicAwareText(
                            text = "⏳ TIME REMAINING",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = buildAnnotatedString {
                                activeCountdown.forEach { char ->
                                    if (char == ':') {
                                        withStyle(SpanStyle(color = IslamicGold, fontWeight = FontWeight.Bold)) {
                                            append(" : ")
                                        }
                                    } else {
                                        withStyle(SpanStyle(fontSize = 44.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)) {
                                            append(char.toString())
                                        }
                                    }
                                }
                            }
                        )
                        LiveSecondsIndicator()
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Quick Info Row
                InfoCardRow(
                    sunriseTime = sunriseTime,
                    qiblaDirection = qiblaDirection,
                    cityName = cityName,
                    onLocationClick = onLocationClick,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Footer
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ArabicAwareText(
                        text = gregorianDate,
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.6f),
                        fontWeight = FontWeight.Medium
                    )
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = IslamicGold)
                    } else {
                        Icon(Icons.Default.Verified, contentDescription = null, tint = IslamicGold, modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                AnimatedHadithQuote()
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun LiveSecondsIndicator() {
    var tick by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(500)
            tick = !tick
        }
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(if (tick) IslamicGold else IslamicGold.copy(alpha = 0.3f))
        )
        Spacer(modifier = Modifier.width(6.dp))
        ArabicAwareText(
            text = "LIVE UPDATE",
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = IslamicGold
        )
    }
}

@Composable
fun InfoCardRow(
    sunriseTime: String,
    qiblaDirection: String,
    cityName: String,
    onLocationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        InfoCard(
            title = "SUNRISE",
            value = sunriseTime,
            icon = Icons.Default.WbSunny,
            iconTint = Color(0xFFFFD93D),
            modifier = Modifier.weight(1f)
        )
        InfoCard(
            title = "QIBLA",
            value = qiblaDirection,
            icon = Icons.Outlined.LocationOn,
            iconTint = Color(0xFF4ECDC4),
            modifier = Modifier.weight(1f)
        )
        InfoCard(
            title = "CITY",
            value = cityName,
            icon = Icons.Default.Place,
            iconTint = Color(0xFF9575CD),
            modifier = Modifier.weight(1f).clickable(onClick = onLocationClick)
        )
    }
}

@Composable
fun InfoCard(
    title: String,
    value: String,
    icon: ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.height(8.dp))
        ArabicAwareText(title, fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.White.copy(alpha = 0.5f))
        ArabicAwareText(value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White, textAlign = TextAlign.Center)
    }
}

@Composable
fun AnimatedHadithQuote() {
    val quotes = listOf(
        "الصلاة عماد الدين" to "Prayer is the pillar of religion",
        "أقرب ما يكون العبد من ربه وهو ساجد" to "The closest a servant is to his Lord is while prostrating",
        "الصلاة نور" to "Prayer is light"
    )
    var index by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(8000)
            index = (index + 1) % quotes.size
        }
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        ArabicAwareText(quotes[index].first, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = IslamicGold, textAlign = TextAlign.Center)
        ArabicAwareText(quotes[index].second, fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f), textAlign = TextAlign.Center)
    }
}

@Composable
fun PremiumIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    tint: Color,
    backgroundColor: Color,
    tooltip: String
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(40.dp).clip(CircleShape).background(backgroundColor)
    ) {
        Icon(icon, contentDescription = tooltip, tint = tint, modifier = Modifier.size(20.dp))
    }
}

@Composable
fun ErrorHeroCard(errorMessage: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.padding(16.dp).fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A237E)),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.WifiOff, null, tint = IslamicGold, modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(16.dp))
            ArabicAwareText("Connection Error", fontWeight = FontWeight.Bold, color = Color.White)
            ArabicAwareText(errorMessage, fontSize = 13.sp, color = Color.White.copy(alpha = 0.7f), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry, colors = ButtonDefaults.buttonColors(containerColor = IslamicGold)) {
                Text("Retry")
            }
        }
    }
}

@Composable
fun HeroCard(
    nextPrayerName: String,
    countdown: String,
    sunriseTime: String,
    hijriDate: String,
    cityName: String,
    onDetectLocationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    PremiumHeroCard(
        nextPrayerName = nextPrayerName,
        countdown = countdown,
        sunriseTime = sunriseTime,
        hijriDate = hijriDate,
        cityName = cityName,
        onDetectLocationClick = onDetectLocationClick,
        modifier = modifier
    )
}
