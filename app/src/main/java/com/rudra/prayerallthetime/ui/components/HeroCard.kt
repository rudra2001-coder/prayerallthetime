package com.rudra.prayerallthetime.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.rudra.prayerallthetime.ui.theme.*
import kotlinx.coroutines.delay
import java.util.Locale

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

// Prayer name translations
val prayerNameTranslations = mapOf(
    "Fajr" to mapOf("en" to "Fajr", "ar" to "الفجر"),
    "Dhuhr" to mapOf("en" to "Dhuhr", "ar" to "الظهر"),
    "Asr" to mapOf("en" to "Asr", "ar" to "العصر"),
    "Maghrib" to mapOf("en" to "Maghrib", "ar" to "المغرب"),
    "Isha" to mapOf("en" to "Isha", "ar" to "العشاء")
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PremiumHeroCard(
    nextPrayerName: String = "Fajr",
    countdown: String = "02:45:18",
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
            .heightIn(min = 580.dp, max = 680.dp)
            .pointerInput(Unit) {
                forEachGesture {
                    awaitPointerEventScope {
                        awaitFirstDown()
                        isPressed = true
                        try {
                            awaitPointerEvent()
                        } finally {
                            isPressed = false
                        }
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
                    for (i in 0..20) {
                        val x = (i * size.width / 20) + (Math.sin((moonPhase + i).toDouble()) * 10).toFloat()
                        val y = (i * size.height / 20) + (Math.cos((moonPhase + i).toDouble()) * 10).toFloat()
                        drawCircle(
                            color = Color.White.copy(alpha = 0.2f + i * 0.01f),
                            radius = 1f + (i % 3).toFloat(),
                            center = Offset(x, y)
                        )
                    }

                    val moonCenter = Offset(size.width * 0.87f, size.height * 0.15f)
                    drawCircle(
                        color = IslamicGold.copy(alpha = 0.15f),
                        radius = 48f,
                        center = moonCenter
                    )
                    drawCircle(
                        color = Color(0xFF1A237E),
                        radius = 40f,
                        center = moonCenter.copy(x = moonCenter.x + 10f)
                    )

                    drawArc(
                        color = IslamicGold.copy(alpha = 0.3f),
                        startAngle = -90f,
                        sweepAngle = 360f * prayerProgress,
                        useCenter = false,
                        topLeft = Offset(size.width * 0.05f, size.height * 0.05f),
                        size = Size(size.width * 0.9f, size.height * 0.9f),
                        style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                    )

                    if (isLoading) {
                        val shimmerWidth = 300f
                        drawRect(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.White.copy(alpha = 0.1f),
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    IslamicGold.copy(alpha = 0.3f),
                                    Color.Transparent
                                )
                            )
                        )
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawPath(
                            path = Path().apply {
                                moveTo(size.width * 0.3f, size.height * 0.6f)
                                lineTo(size.width * 0.7f, size.height * 0.6f)
                                lineTo(size.width * 0.75f, size.height * 0.8f)
                                lineTo(size.width * 0.25f, size.height * 0.8f)
                                close()
                                moveTo(size.width * 0.5f, size.height * 0.3f)
                                quadraticBezierTo(
                                    size.width * 0.2f, size.height * 0.6f,
                                    size.width * 0.5f, size.height * 0.6f
                                )
                                quadraticBezierTo(
                                    size.width * 0.8f, size.height * 0.6f,
                                    size.width * 0.5f, size.height * 0.3f
                                )
                            },
                            color = Color.White.copy(alpha = 0.1f),
                            style = Stroke(width = 2.dp.toPx())
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .animateContentSize()
                        ) {
                            Text(
                                text = "🌙 ISLAMIC DATE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White.copy(alpha = 0.7f),
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = hijriDate,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = IslamicGold,
                                maxLines = 2
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            PremiumIconButton(
                                icon = Icons.Default.CalendarMonth,
                                onClick = onCalendarClick,
                                tint = Color.White,
                                backgroundColor = Color.White.copy(alpha = 0.15f),
                                tooltip = "View Calendar"
                            )

                            PremiumIconButton(
                                icon = Icons.Default.Notifications,
                                onClick = onAlarmClick,
                                tint = if (isAlarmSet) IslamicGold else Color.White.copy(alpha = 0.7f),
                                backgroundColor = if (isAlarmSet) IslamicGold.copy(alpha = 0.25f)
                                else Color.White.copy(alpha = 0.15f),
                                tooltip = if (isAlarmSet) "Alarm Set" else "Set Alarm"
                            )

                            PremiumIconButton(
                                icon = Icons.Default.MyLocation,
                                onClick = onDetectLocationClick,
                                tint = Color.White,
                                backgroundColor = Color.White.copy(alpha = 0.15f),
                                tooltip = "Detect Location"
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    IslamicGold.copy(alpha = 0.3f),
                                    IslamicGold.copy(alpha = 0.1f),
                                    Color.Transparent
                                ),
                                center = Offset(0.5f, 0.5f),
                                radius = 1.5f
                            )
                        )
                        .border(
                            width = 1.dp,
                            color = IslamicGold.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(28.dp)
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onPrayerInfoClick
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp, horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🕌 NEXT PRAYER",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White.copy(alpha = 0.8f),
                            letterSpacing = 1.2.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = nextPrayerName,
                                    fontSize = 36.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    letterSpacing = 1.sp,
                                    style = TextStyle(
                                        shadow = Shadow(
                                            color = IslamicGold.copy(alpha = 0.5f),
                                            offset = Offset(2f, 2f),
                                            blurRadius = 8f
                                        )
                                    )
                                )
                                if (nextPrayerArabicName.isNotEmpty()) {
                                    Text(
                                        text = nextPrayerArabicName,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = IslamicGold.copy(alpha = 0.8f),
                                        textAlign = TextAlign.Start,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                                Text(
                                    text = "Starting in",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White.copy(alpha = 0.6f),
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }

                            Column(
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(
                                    text = prayerTime,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = IslamicGold,
                                    style = TextStyle(
                                        shadow = Shadow(
                                            color = IslamicGold.copy(alpha = 0.3f),
                                            offset = Offset(1f, 1f),
                                            blurRadius = 4f
                                        )
                                    )
                                )
                                Text(
                                    text = "Local Time",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White.copy(alpha = 0.6f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        LinearProgressIndicator(
                            progress = prayerProgress,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(CircleShape),
                            color = IslamicGold,
                            trackColor = IslamicGold.copy(alpha = 0.2f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.08f),
                                    Color.White.copy(alpha = 0.03f)
                                )
                            )
                        )
                        .border(
                            width = 1.dp,
                            color = Color.White.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(32.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "⏳ TIME REMAINING",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White.copy(alpha = 0.7f),
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text = buildAnnotatedString {
                                countdown.forEachIndexed { index, char ->
                                    if (char == ':') {
                                        append(char.toString())
                                    } else {
                                        withStyle(
                                            style = SpanStyle(
                                                fontSize = 42.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = Color.White,
                                                shadow = Shadow(
                                                    color = IslamicGold.copy(alpha = 0.3f),
                                                    blurRadius = 8f
                                                )
                                            )
                                        ) {
                                            append(char.toString())
                                        }
                                    }
                                }
                            },
                            letterSpacing = 4.sp,
                            lineHeight = 56.sp
                        )

                        LiveSecondsIndicator()
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                val screenWidth = LocalDensity.current.run {
                    LocalContext.current.resources.displayMetrics.widthPixels.toDp()
                }
                val isCompact = screenWidth < 360.dp

                if (isCompact) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        InfoCardRow(
                            sunriseTime = sunriseTime,
                            qiblaDirection = qiblaDirection,
                            cityName = cityName,
                            onLocationClick = onLocationClick,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                } else {
                    InfoCardRow(
                        sunriseTime = sunriseTime,
                        qiblaDirection = qiblaDirection,
                        cityName = cityName,
                        onLocationClick = onLocationClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = gregorianDate,
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f)
                    )

                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = IslamicGold
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Updated",
                            tint = IslamicGold,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                AnimatedHadithQuote()
            }
        }
    }
}

@Composable
fun OptimizedPremiumHeroCard(
    nextPrayerName: String = "Fajr",
    countdown: String = "02:45:18",
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
    val configuration = LocalConfiguration.current
    val lazyListState = rememberLazyListState()
    
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 580.dp, max = 680.dp),
        state = lazyListState,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            PremiumHeroCard(
                nextPrayerName = nextPrayerName,
                countdown = countdown,
                sunriseTime = sunriseTime,
                hijriDate = hijriDate,
                gregorianDate = gregorianDate,
                cityName = cityName,
                prayerTime = prayerTime,
                isAlarmSet = isAlarmSet,
                qiblaDirection = qiblaDirection,
                prayerProgress = prayerProgress,
                isLoading = isLoading,
                currentPrayerName = currentPrayerName,
                nextPrayerArabicName = nextPrayerArabicName,
                temperature = temperature,
                humidity = humidity,
                windDirection = windDirection,
                isPremiumUser = isPremiumUser,
                onDetectLocationClick = onDetectLocationClick,
                onAlarmClick = onAlarmClick,
                onCalendarClick = onCalendarClick,
                onLocationClick = onLocationClick,
                onPrayerInfoClick = onPrayerInfoClick
            )
        }
    }
}

@Composable
fun ErrorHeroCard(
    errorMessage: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .heightIn(min = 200.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A237E))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.WifiOff,
                contentDescription = "Connection Error",
                tint = IslamicGold,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Connection Error",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = errorMessage,
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = IslamicGold,
                    contentColor = Color.White
                )
            ) {
                Text("Retry", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun PremiumIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    tint: Color,
    backgroundColor: Color,
    tooltip: String
) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(backgroundColor)
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.2f),
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = icon,
                contentDescription = tooltip,
                tint = tint,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun LiveSecondsIndicator() {
    var seconds by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            seconds = (seconds + 1) % 60
        }
    }

    Row(
        modifier = Modifier.padding(top = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(3) { index ->
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(
                        if (seconds % 3 == index) IslamicGold
                        else IslamicGold.copy(alpha = 0.3f)
                    )
            )
        }
        Text(
            text = "LIVE",
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = IslamicGold,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(start = 8.dp)
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
            backgroundColor = Color.White.copy(alpha = 0.1f),
            modifier = Modifier.weight(1f)
        )

        InfoCard(
            title = "QIBLA",
            value = qiblaDirection,
            icon = Icons.Outlined.LocationOn,
            iconTint = Color(0xFF4ECDC4),
            backgroundColor = Color.White.copy(alpha = 0.1f),
            modifier = Modifier
                .weight(1f)
                .clickable(onClick = onLocationClick)
        )

        InfoCard(
            title = "LOCATION",
            value = cityName,
            icon = Icons.Default.Place,
            iconTint = Color(0xFF9575CD),
            backgroundColor = Color.White.copy(alpha = 0.1f),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun InfoCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .padding(vertical = 16.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = iconTint,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontSize = 10.sp,
            fontWeight = FontWeight.Black,
            color = Color.White.copy(alpha = 0.6f),
            letterSpacing = 0.8.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            maxLines = 2,
            lineHeight = 18.sp
        )
    }
}

@Composable
fun AnimatedHadithQuote() {
    val hadiths = listOf(
        "الصلاة عماد الدين" to "Prayer is the pillar of religion",
        "أقرب ما يكون العبد من ربه وهو ساجد" to "The closest a servant is to his Lord is while prostrating",
        "الصلاة نور" to "Prayer is light"
    )

    var currentIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(10000)
            currentIndex = (currentIndex + 1) % hadiths.size
        }
    }

    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = hadiths[currentIndex].first,
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraBold,
            color = IslamicGold.copy(alpha = alpha),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = hadiths[currentIndex].second,
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        )
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
        onDetectLocationClick = onDetectLocationClick
    )
}
