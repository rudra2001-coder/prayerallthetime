package com.rudra.prayerallthetime.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rudra.prayerallthetime.data.*
import com.rudra.prayerallthetime.ui.theme.IslamicGold
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.cos
import kotlin.math.sin

/**
 * Consolidated UI components for Islamic features.
 */

// 1. Smart Prayer Reminders
@Composable
fun SmartReminderCard(
    prayerName: String,
    timeRemaining: String,
    reminderType: ReminderType,
    onSnooze: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A237E))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(IslamicGold.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when(reminderType) {
                        ReminderType.ADHAN_TIME -> Icons.Default.NotificationsActive
                        ReminderType.PREP_TIME -> Icons.Default.Timer
                        ReminderType.QIBLA_TIME -> Icons.Default.Explore
                        ReminderType.TAHARA -> Icons.Default.Opacity
                        ReminderType.SUNNAH_RAKAAT -> Icons.Default.AutoAwesome
                    },
                    contentDescription = null,
                    tint = IslamicGold
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = prayerName,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = when(reminderType) {
                        ReminderType.ADHAN_TIME -> "It's time for prayer"
                        ReminderType.PREP_TIME -> "Starts in $timeRemaining"
                        else -> "Time to prepare"
                    },
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }
            
            IconButton(onClick = onSnooze) {
                Icon(Icons.Default.Snooze, contentDescription = "Snooze", tint = Color.White)
            }
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = "Dismiss", tint = Color.White)
            }
        }
    }
}

// 2. Qibla Compass with AR UI Shell
@Composable
fun QiblaCompassAR(
    currentDirection: Float,
    targetDirection: Float,
    accuracy: Float,
    showAROverlay: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color.LightGray.copy(alpha = 0.3f),
                style = Stroke(width = 4.dp.toPx())
            )
        }
        
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { rotationZ = targetDirection - currentDirection },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Navigation,
                contentDescription = null,
                tint = IslamicGold,
                modifier = Modifier.size(64.dp)
            )
            
            Text(
                "KAABA",
                modifier = Modifier.offset(y = (-100).dp),
                fontWeight = FontWeight.Bold,
                color = IslamicGold
            )
        }
        
        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Accuracy: ${accuracy.toInt()}%",
                fontSize = 12.sp,
                color = Color.Gray
            )
            if (showAROverlay) {
                Button(onClick = { /* Open Camera */ }) {
                    Text("Switch to AR View")
                }
            }
        }
    }
}

// 3. Islamic Calendar
@Composable
fun IslamicCalendarView(
    month: IslamicMonth,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(month.name, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = IslamicGold)
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                (1..7).forEach { day ->
                    Text("D$day", fontSize = 12.sp, color = Color.Gray)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text("Special Days", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            month.specialDays.forEach { day ->
                Row(modifier = Modifier.padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).background(IslamicGold, CircleShape))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("${day.name} (${day.date})", fontSize = 13.sp)
                }
            }
        }
    }
}

// 4. Digital Tasbih Counter
@Composable
fun DigitalTasbihCounter(
    dhikrType: DhikrType = DhikrType.SUBHANALLAH,
    targetCount: Int = 33,
    onComplete: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var count by remember { mutableIntStateOf(0) }
    val progress = count.toFloat() / targetCount
    
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Card(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF2C3E50), Color(0xFF000000))
                    )
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = dhikrType.name.replace("_", " "),
                color = IslamicGold,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(200.dp)
                    .clickable {
                        if (count < targetCount) {
                            count++
                            if (count == targetCount) onComplete()
                        }
                    }
                    .scale(if (count > 0 && count < targetCount) pulseScale else 1f)
            ) {
                // Circular Progress
                CircularProgressIndicator(
                    progress = progress,
                    modifier = Modifier.fillMaxSize(),
                    color = IslamicGold,
                    strokeWidth = 12.dp,
                    trackColor = Color.White.copy(alpha = 0.1f)
                )
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = count.toString(),
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "OF $targetCount",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(
                    onClick = { count = 0 },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.1f))
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Reset", tint = Color.White)
                }
                
                Button(
                    onClick = { 
                        if (count < targetCount) {
                            count++
                            if (count == targetCount) onComplete()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = IslamicGold),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text("TAP TO COUNT", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// 5. Prayer Time Visualizations
@Composable
fun PrayerTimelineView(
    prayerTimes: List<PrayerTime>,
    currentTime: LocalTime,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                "Prayer Timeline",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))
            
            Box(modifier = Modifier.fillMaxWidth().height(80.dp)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawLine(
                        color = Color.LightGray,
                        start = Offset(0f, size.height / 2),
                        end = Offset(size.width, size.height / 2),
                        strokeWidth = 2.dp.toPx()
                    )
                }
                
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    prayerTimes.forEach { prayer ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            val isPast = prayer.time.isBefore(currentTime)
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(if (isPast) IslamicGold else Color.LightGray)
                            )
                            Text(
                                prayer.name,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isPast) IslamicGold else Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}

// 6. Prayer Preparation Flow
@Composable
fun PrayerPreparationFlow(
    prayerName: String,
    steps: List<String>,
    modifier: Modifier = Modifier
) {
    var currentStep by remember { mutableIntStateOf(0) }
    
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Preparation: $prayerName", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))
            
            LinearProgressIndicator(
                progress = (currentStep + 1).toFloat() / steps.size,
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                color = IslamicGold
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            Text(steps[currentStep], fontSize = 16.sp, minLines = 3)
            
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                TextButton(onClick = { if (currentStep > 0) currentStep-- }, enabled = currentStep > 0) {
                    Text("Previous")
                }
                Button(onClick = { if (currentStep < steps.size - 1) currentStep++ }, colors = ButtonDefaults.buttonColors(containerColor = IslamicGold)) {
                    Text(if (currentStep == steps.size - 1) "Done" else "Next Step")
                }
            }
        }
    }
}

// 8. Learning & Education
@Composable
fun PrayerTutorialView(
    prayerType: PrayerType,
    stepDescription: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4F8))
    ) {
        Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Learning ${prayerType.name}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Placeholder for 3D/Image content
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.White, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.AccessibilityNew, contentDescription = null, modifier = Modifier.size(100.dp), tint = IslamicGold)
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            Text(
                stepDescription,
                textAlign = TextAlign.Center,
                fontSize = 15.sp
            )
        }
    }
}

// 9. Personal Prayer Analytics
@Composable
fun PrayerAnalyticsDashboard(
    analytics: PrayerAnalytics,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                "Prayer Performance",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                AnalyticsItem("Consistency", "${(analytics.consistencyScore * 100).toInt()}%", Icons.Default.TrendingUp)
                AnalyticsItem("Streak", "${analytics.streak} Days", Icons.Default.Whatshot)
                AnalyticsItem("Total", "${analytics.totalPrayers}", Icons.Default.ListAlt)
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            Text("Suggested Improvements", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            analytics.improvementAreas.forEach { area ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = IslamicGold, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(area, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
private fun AnalyticsItem(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = IslamicGold)
        Text(value, fontWeight = FontWeight.Black, fontSize = 18.sp)
        Text(label, fontSize = 12.sp, color = Color.Gray)
    }
}

// 11. Family Prayer Dashboard
@Composable
fun FamilyPrayerDashboard(
    familyGroup: FamilyPrayerGroup,
    onMemberClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Family Spiritual Circle", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(familyGroup.members) { member ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { onMemberClick(member.id) }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .background(IslamicGold.copy(alpha = 0.1f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(member.avatarEmoji, fontSize = 30.sp)
                        }
                        Text(member.name, fontWeight = FontWeight.Medium, fontSize = 12.sp)
                        Text("${member.prayersCompleted}/5", fontSize = 10.sp, color = IslamicGold)
                    }
                }
            }
        }
    }
}

// 16. Zakat Calculator
@Composable
fun ZakatCalculator(
    modifier: Modifier = Modifier
) {
    var cashAmount by remember { mutableStateOf("") }
    
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Zakat Calculator", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = cashAmount,
                onValueChange = { cashAmount = it },
                label = { Text("Total Savings") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            val total = cashAmount.toDoubleOrNull() ?: 0.0
            val zakat = total * 0.025
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(IslamicGold.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Payable Zakat:", fontWeight = FontWeight.Bold)
                    Text("$${String.format("%.2f", zakat)}", color = IslamicGold, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}
