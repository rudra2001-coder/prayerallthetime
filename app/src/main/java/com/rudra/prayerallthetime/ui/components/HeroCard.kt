package com.rudra.prayerallthetime.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rudra.prayerallthetime.ui.theme.*

@Composable
fun PremiumHeroCard(
    nextPrayerName: String = "Fajr",
    countdown: String = "02:45:18",
    sunriseTime: String = "06:45 AM",
    hijriDate: String = "25 Ramadan 1445 AH",
    gregorianDate: String = "Monday, April 15, 2024",
    cityName: String = "Mecca, Saudi Arabia",
    prayerTime: String = "05:15 AM",
    isAlarmSet: Boolean = true,
    qiblaDirection: String = "90°",
    onDetectLocationClick: () -> Unit = {},
    onAlarmClick: () -> Unit = {},
    onCalendarClick: () -> Unit = {},
    onLocationClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.3f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF1A237E),
                            Color(0xFF283593),
                            Color(0xFF303F9F)
                        ),
                        center = Offset(0.5f, 0.2f),
                        radius = 1.5f
                    )
                )
                .drawBehind {
                    // Draw Islamic pattern in background
                    for (i in 0..10) {
                        val x = (i + 1) * (size.width / 12)
                        drawCircle(
                            color = IslamicGold.copy(alpha = 0.1f),
                            radius = 8f,
                            center = Offset(x, size.height * 0.8f)
                        )
                    }

                    // Draw crescent moon
                    drawCircle(
                        color = IslamicGold.copy(alpha = 0.2f),
                        radius = 40f,
                        center = Offset(size.width * 0.9f, size.height * 0.2f)
                    )
                }
                .padding(28.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Row - Date & Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Hijri Date
                    Column {
                        Text(
                            text = "📅 ISLAMIC DATE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.7f),
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = hijriDate,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = IslamicGold,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    // Control Buttons
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Calendar Button
                        IconButton(
                            onClick = onCalendarClick,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.1f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = "Calendar",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Alarm Button
                        IconButton(
                            onClick = onAlarmClick,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isAlarmSet) IslamicGold.copy(alpha = 0.2f)
                                    else Color.White.copy(alpha = 0.1f)
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Alarm",
                                tint = if (isAlarmSet) IslamicGold else Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Location Button
                        IconButton(
                            onClick = onDetectLocationClick,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.1f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.MyLocation,
                                contentDescription = "Detect Location",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Next Prayer Highlight
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🕌 NEXT PRAYER",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.8f),
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Prayer Name with decorative elements
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(IslamicGold.copy(alpha = 0.2f))
                            .padding(horizontal = 32.dp, vertical = 12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = nextPrayerName,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                letterSpacing = 2.sp
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = prayerTime,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = IslamicGold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Main Countdown Timer
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(IslamicGold.copy(alpha = 0.1f))
                        .padding(vertical = 20.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "⏳ TIME UNTIL",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.7f),
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        Text(
                            text = countdown,
                            fontSize = 64.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            letterSpacing = 4.sp,
                            lineHeight = 64.sp
                        )

                        Text(
                            text = "Hours    Minutes    Seconds",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White.copy(alpha = 0.6f),
                            letterSpacing = 2.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Bottom Info Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Sunrise Info
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White.copy(alpha = 0.1f))
                            .padding(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.WbSunny,
                                contentDescription = "Sunrise",
                                tint = Color(0xFFFFD93D),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Sunrise",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                        Text(
                            text = sunriseTime,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFD93D),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    // Location Info
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White.copy(alpha = 0.1f))
                            .clickable(onClick = onLocationClick)
                            .padding(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Location",
                                tint = Color(0xFF4ECDC4),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Qibla",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                        Text(
                            text = qiblaDirection,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4ECDC4),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    // City Info
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White.copy(alpha = 0.1f))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "📍 LOCATION",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.6f),
                            letterSpacing = 0.5.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            text = cityName,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            maxLines = 2
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Gregorian Date
                Text(
                    text = gregorianDate,
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.6f),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 8.dp)
                )

                // Motivational Quote
                Text(
                    text = "الصلاة عماد الدين",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = IslamicGold.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                )
                Text(
                    text = "Prayer is the pillar of religion",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

// Original version for backward compatibility
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