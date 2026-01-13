package com.rudra.prayerallthetime.ui.screen.dashboard.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rudra.prayerallthetime.ui.screen.prayer.PrayerViewModel
import com.rudra.prayerallthetime.ui.screen.prayer.TimePeriod

@Composable
fun PremiumWeeklyMonthlyStatsCard(
    timePeriod: TimePeriod = TimePeriod.WEEKLY,
    onTimePeriodChange: (TimePeriod) -> Unit = {},
    weeklyData: List<DayData> = getWeeklyData(),
    monthlyData: List<MonthData> = getMonthlyData(),
    comparisonData: ComparisonData = getComparisonData(),
    onViewDetails: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 12.dp),
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 12.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF0F4FF),
                            Color(0xFFE8F0FF)
                        )
                    )
                )
                .padding(24.dp)
        ) {
            // Header with period selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.BarChart,
                            contentDescription = "Statistics",
                            tint = Color(0xFF8B4513),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "📈 Weekly & Monthly Stats",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2C3E50)
                        )
                    }
                    Text(
                        text = "Track your spiritual progress over time",
                        fontSize = 12.sp,
                        color = Color(0xFF2C3E50).copy(alpha = 0.6f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Time Period Selector
                TimePeriodDropdown(
                    selectedPeriod = timePeriod,
                    onPeriodSelected = onTimePeriodChange
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Summary Stats
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White.copy(alpha = 0.5f))
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(
                    title = "Avg. Completion",
                    value = if (timePeriod == TimePeriod.WEEKLY) "92%" else "88%",
                    change = "+4.2%",
                    isPositive = true,
                    icon = Icons.Default.TrendingUp,
                    color = Color(0xFF4ECDC4)
                )
                StatItem(
                    title = "Total Prayers",
                    value = if (timePeriod == TimePeriod.WEEKLY) "35" else "150",
                    change = "+12",
                    isPositive = true,
                    icon = Icons.Default.CalendarToday,
                    color = Color(0xFF45B7D1)
                )
                StatItem(
                    title = "Best Day",
                    value = if (timePeriod == TimePeriod.WEEKLY) "Friday" else "Week 2",
                    icon = Icons.Default.ArrowDropUp,
                    color = Color(0xFFD4AF37)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Comparison Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF2C3E50).copy(alpha = 0.05f))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Compared to Last ${if (timePeriod == TimePeriod.WEEKLY) "Week" else "Month"}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF2C3E50)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            val comparisonValue = if (timePeriod == TimePeriod.WEEKLY)
                                comparisonData.weeklyComparison else comparisonData.monthlyComparison
                            Icon(
                                imageVector = if (comparisonValue > 0) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                contentDescription = "Change",
                                tint = if (comparisonValue > 0) Color(0xFF4ECDC4) else Color(0xFFFF6B6B),
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = "${if (comparisonValue > 0) "+" else ""}$comparisonValue%",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (comparisonValue > 0) Color(0xFF4ECDC4) else Color(0xFFFF6B6B)
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF8B4513).copy(alpha = 0.1f))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShowChart,
                            contentDescription = "Comparison",
                            tint = Color(0xFF8B4513),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Chart Section
            Text(
                text = if (timePeriod == TimePeriod.WEEKLY) "Weekly Performance" else "Monthly Trends",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2C3E50),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (timePeriod == TimePeriod.WEEKLY) {
                // Weekly Bar Chart
                WeeklyBarChart(data = weeklyData)
            } else {
                // Monthly Line Chart
                MonthlyLineChart(data = monthlyData)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Day-wise Performance (Weekly) or Month-wise (Monthly)
            Text(
                text = if (timePeriod == TimePeriod.WEEKLY) "Daily Breakdown" else "Monthly Overview",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2C3E50),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.height(100.dp)
            ) {
                if (timePeriod == TimePeriod.WEEKLY) {
                    items(weeklyData) { dayData ->
                        DayPerformanceCard(dayData = dayData)
                    }
                } else {
                    items(monthlyData) { monthData ->
                        MonthPerformanceCard(monthData = monthData)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Prayer Distribution Pie Chart
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .drawBehind {
                            // Draw pie chart for prayer distribution
                            val distribution = if (timePeriod == TimePeriod.WEEKLY)
                                listOf(0.25f, 0.20f, 0.18f, 0.22f, 0.15f)
                            else listOf(0.22f, 0.21f, 0.19f, 0.23f, 0.15f)
                            val colors = listOf(
                                Color(0xFF4ECDC4),  // Fajr
                                Color(0xFF45B7D1),  // Dhuhr
                                Color(0xFF96CEB4),  // Asr
                                Color(0xFFFFD93D),  // Maghrib
                                Color(0xFFFF6B6B)   // Isha
                            )

                            var startAngle = 0f
                            for (i in distribution.indices) {
                                val sweepAngle = distribution[i] * 360f
                                drawArc(
                                    color = colors[i],
                                    startAngle = startAngle,
                                    sweepAngle = sweepAngle,
                                    useCenter = true
                                )
                                startAngle += sweepAngle
                            }

                            // Draw center circle
                            drawCircle(
                                color = Color.White,
                                radius = size.minDimension * 0.3f,
                                center = Offset(size.width / 2, size.height / 2)
                            )
                        }
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PrayerDistributionItem(
                        prayer = "Fajr",
                        percentage = if (timePeriod == TimePeriod.WEEKLY) "25%" else "22%",
                        color = Color(0xFF4ECDC4)
                    )
                    PrayerDistributionItem(
                        prayer = "Dhuhr",
                        percentage = if (timePeriod == TimePeriod.WEEKLY) "20%" else "21%",
                        color = Color(0xFF45B7D1)
                    )
                    PrayerDistributionItem(
                        prayer = "Asr",
                        percentage = if (timePeriod == TimePeriod.WEEKLY) "18%" else "19%",
                        color = Color(0xFF96CEB4)
                    )
                    PrayerDistributionItem(
                        prayer = "Maghrib",
                        percentage = if (timePeriod == TimePeriod.WEEKLY) "22%" else "23%",
                        color = Color(0xFFFFD93D)
                    )
                    PrayerDistributionItem(
                        prayer = "Isha",
                        percentage = if (timePeriod == TimePeriod.WEEKLY) "15%" else "15%",
                        color = Color(0xFFFF6B6B)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Insights Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF8B4513).copy(alpha = 0.1f))
                    .padding(16.dp)
                    .clickable { onViewDetails() }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFD4AF37).copy(alpha = 0.2f))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PieChart,
                            contentDescription = "Insights",
                            tint = Color(0xFFD4AF37),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Key Insight",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF8B4513)
                        )
                        Text(
                            text = if (timePeriod == TimePeriod.WEEKLY)
                                "You're most consistent with Fajr prayers. Keep up the morning discipline!"
                            else "Monthly trend shows improvement in Dhuhr prayer consistency.",
                            fontSize = 12.sp,
                            color = Color(0xFF2C3E50).copy(alpha = 0.8f),
                            lineHeight = 16.sp
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "View Details",
                        tint = Color(0xFF8B4513).copy(alpha = 0.5f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TimePeriodDropdown(
    selectedPeriod: TimePeriod,
    onPeriodSelected: (TimePeriod) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF8B4513).copy(alpha = 0.1f))
                .clickable { expanded = true }
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Period",
                    tint = Color(0xFF8B4513),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = selectedPeriod.displayName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF8B4513)
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown",
                    tint = Color(0xFF8B4513),
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            TimePeriod.values().forEach { period ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = period.displayName,
                            color = Color(0xFF2C3E50)
                        )
                    },
                    onClick = {
                        onPeriodSelected(period)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun StatItem(
    title: String,
    value: String,
    change: String? = null,
    isPositive: Boolean = true,
    icon: ImageVector,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(color.copy(alpha = 0.2f))
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2C3E50)
        )
        if (change != null) {
            Text(
                text = change,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isPositive) Color(0xFF4ECDC4) else Color(0xFFFF6B6B)
            )
        }
        Text(
            text = title,
            fontSize = 11.sp,
            color = Color(0xFF2C3E50).copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun WeeklyBarChart(data: List<DayData>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.5f))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            data.forEach { day ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(40.dp)
                ) {
                    // Bar
                    Box(
                        modifier = Modifier
                            .width(24.dp)
                            .height(100.dp * day.completionRate)
                            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                            .background(
                                if (day.isToday) Color(0xFFD4AF37)
                                else Color(0xFF8B4513).copy(alpha = 0.8f)
                            )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = day.dayAbbr,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2C3E50)
                    )
                    Text(
                        text = "${(day.completionRate * 100).toInt()}%",
                        fontSize = 10.sp,
                        color = Color(0xFF2C3E50).copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@Composable
fun MonthlyLineChart(data: List<MonthData>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.5f))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Simplified line chart simulation
        Text(
            text = "Monthly Trend Chart",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF2C3E50).copy(alpha = 0.5f)
        )
    }
}

@Composable
fun DayPerformanceCard(dayData: DayData) {
    Box(
        modifier = Modifier
            .width(140.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (dayData.isToday) Color(0xFFD4AF37).copy(alpha = 0.1f)
                else Color.White.copy(alpha = 0.3f)
            )
            .padding(12.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Text(
                    text = dayData.dayName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (dayData.isToday) Color(0xFFD4AF37) else Color(0xFF2C3E50)
                )
                if (dayData.isToday) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFD4AF37).copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Today",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFD4AF37)
                        )
                    }
                }
            }
            Text(
                text = "${dayData.completedPrayers}/5 prayers",
                fontSize = 13.sp,
                color = Color(0xFF2C3E50)
            )
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = dayData.completionRate,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape),
                color = if (dayData.isToday) Color(0xFFD4AF37) else Color(0xFF8B4513),
                trackColor = Color(0xFF2C3E50).copy(alpha = 0.1f)
            )
        }
    }
}

@Composable
fun MonthPerformanceCard(monthData: MonthData) {
    Box(
        modifier = Modifier
            .width(160.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.3f))
            .padding(12.dp)
    ) {
        Column {
            Text(
                text = monthData.monthName,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2C3E50),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "${monthData.totalPrayers} prayers",
                fontSize = 13.sp,
                color = Color(0xFF2C3E50)
            )
            Text(
                text = "Avg: ${(monthData.averageCompletion * 100).toInt()}%",
                fontSize = 12.sp,
                color = Color(0xFF2C3E50).copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (monthData.trend == Trend.UP) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = "Trend",
                    tint = if (monthData.trend == Trend.UP) Color(0xFF4ECDC4) else Color(0xFFFF6B6B),
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "${monthData.changePercentage}%",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (monthData.trend == Trend.UP) Color(0xFF4ECDC4) else Color(0xFFFF6B6B)
                )
            }
        }
    }
}

@Composable
fun PrayerDistributionItem(
    prayer: String,
    percentage: String,
    color: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(color)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = prayer,
            fontSize = 12.sp,
            color = Color(0xFF2C3E50),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = percentage,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF2C3E50)
        )
    }
}

enum class Trend {
    UP,
    DOWN,
    STABLE
}

data class DayData(
    val dayName: String,
    val dayAbbr: String,
    val completedPrayers: Int,
    val completionRate: Float,
    val isToday: Boolean = false
)

data class MonthData(
    val monthName: String,
    val totalPrayers: Int,
    val averageCompletion: Float,
    val changePercentage: Int,
    val trend: Trend
)

data class ComparisonData(
    val weeklyComparison: Int,
    val monthlyComparison: Int
)

@Composable
fun getWeeklyData(): List<DayData> {
    return listOf(
        DayData("Monday", "Mon", 5, 1.0f),
        DayData("Tuesday", "Tue", 4, 0.8f),
        DayData("Wednesday", "Wed", 5, 1.0f),
        DayData("Thursday", "Thu", 3, 0.6f),
        DayData("Friday", "Fri", 5, 1.0f, true),
        DayData("Saturday", "Sat", 4, 0.8f),
        DayData("Sunday", "Sun", 5, 1.0f)
    )
}

@Composable
fun getMonthlyData(): List<MonthData> {
    return listOf(
        MonthData("January", 145, 0.93f, 12, Trend.UP),
        MonthData("February", 150, 0.96f, 8, Trend.UP),
        MonthData("March", 155, 1.0f, 15, Trend.UP),
        MonthData("April", 148, 0.95f, -4, Trend.DOWN)
    )
}

@Composable
fun getComparisonData(): ComparisonData {
    return ComparisonData(weeklyComparison = 8, monthlyComparison = 12)
}

// Fixed version to accept PrayerViewModel and removed the parameterless overload
@Composable
fun WeeklyStats(prayerViewModel: PrayerViewModel) {
    val weeklyData by prayerViewModel.weeklyDayData.collectAsState()
    PremiumWeeklyMonthlyStatsCard(weeklyData = weeklyData)
}
